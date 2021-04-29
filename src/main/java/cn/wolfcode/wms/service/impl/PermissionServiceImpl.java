package cn.wolfcode.wms.service.impl;

import cn.wolfcode.wms.domain.Permission;
import cn.wolfcode.wms.mapper.PermissionMapper;
import cn.wolfcode.wms.query.QueryObject;
import cn.wolfcode.wms.service.IPermissionService;
import cn.wolfcode.wms.util.PageResult;
import cn.wolfcode.wms.util.PermissionUtil;
import cn.wolfcode.wms.util.RequiredPermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class PermissionServiceImpl implements IPermissionService {
    @Autowired
    private PermissionMapper permissionMapper;
    @Autowired
    private ApplicationContext ctx;

    public void reload() {
        //先从数据库中查询所有的权限表达式
        List<String> exps = permissionMapper.selectAllExpressions();
        //1:从容器中拿到所有的控制器(根据注解)
        Collection<Object> ctrls = ctx.getBeansWithAnnotation(Controller.class).values();
        //2:遍历所有的控制器上的方法
        ctrls.forEach(ctrl -> {
            Method[] ms = ctrl.getClass().getDeclaredMethods();
            for (Method m : ms) {
                //3:找到所有贴有权限注解的方法
                //4:贴有权限的方法生成权限表达式
                String exp = PermissionUtil.buildExpression(m);
                RequiredPermission anno = m.getAnnotation(RequiredPermission.class);
                if (Optional.ofNullable(anno).isPresent() && !exps.contains(exp)) {
                    //5:当前数据库中不存在该权限,创建权限对象保存到数据库
                    Permission p = new Permission();
                    p.setName(anno.value());
                    p.setExpression(exp);
                    permissionMapper.insert(p);
                }
            }
        });
    }

    public void delete(Long id) {
        permissionMapper.deleteByPrimaryKey(id);
    }

    public List<Permission> list() {
        return permissionMapper.selectAll();
    }

    public PageResult query(QueryObject qo) {
        Integer rows = permissionMapper.selectForCount(qo);
        if (rows == 0) {
            return PageResult.EMPTY_PAGE;
        }
        List<?> data = permissionMapper.selectForList(qo);
        return new PageResult(qo.getCurrentPage(), qo.getPageSize(), data, rows);
    }
}
