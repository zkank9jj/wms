package cn.wolfcode.wms.service.impl;

import cn.wolfcode.wms.domain.Employee;
import cn.wolfcode.wms.mapper.EmployeeMapper;
import cn.wolfcode.wms.mapper.PermissionMapper;
import cn.wolfcode.wms.query.QueryObject;
import cn.wolfcode.wms.service.IEmployeeService;
import cn.wolfcode.wms.util.MD5;
import cn.wolfcode.wms.util.PageResult;
import cn.wolfcode.wms.util.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements IEmployeeService {
    @Autowired
    private EmployeeMapper employeeMapper;
    @Autowired
    private PermissionMapper permissionMapper;


    public void login(String username, String password) {
        //密码进行加密再传给持久层执行
        Employee emp = employeeMapper.selectByInfo(username, MD5.encoder(password));
        if (!Optional.ofNullable(emp).isPresent()) { //登录失败
            throw new RuntimeException("账号和密码不匹配");
        }
        //登录成功,把当前登录成功的用户信息存入session
        UserContext.setCurrentUser(emp);
        //非超管,查询出当前用户拥有的权限,存入session
        if (!emp.isAdmin()) {
            List<String> exps = permissionMapper.selectByEmployeeId(emp.getId());
            UserContext.setCurrentExpressions(exps);
        }
    }

    @Override
    public void batchDelete(Long[] ids) {
        employeeMapper.batchDelete(ids);
    }

    public void saveOrUpdate(Employee entity, Long[] roleIds) {
        if (!Optional.ofNullable(entity.getId()).isPresent()) {
            //保存员工之前,先对密码进行加密处理
            entity.setPassword(MD5.encoder(entity.getPassword()));
            employeeMapper.insert(entity);//没有ID
        } else {
            employeeMapper.deleteRelaction(entity.getId());
            employeeMapper.updateByPrimaryKey(entity);
        }
        //维护关系
        if (Optional.ofNullable(roleIds).isPresent()) {
            for (Long roleId : roleIds) {
                employeeMapper.insertRelaction(entity.getId(), roleId);
            }
        }
    }

    public void delete(Long id) {
        employeeMapper.deleteRelaction(id);
        employeeMapper.deleteByPrimaryKey(id);
    }

    public Employee get(Long id) {
        return employeeMapper.selectByPrimaryKey(id);
    }

    public PageResult query(QueryObject qo) {
        Integer rows = employeeMapper.selectForCount(qo);
        if (rows == 0) {
            return PageResult.EMPTY_PAGE;
        }
        List<?> data = employeeMapper.selectForList(qo);
        return new PageResult(qo.getCurrentPage(), qo.getPageSize(), data, rows);
    }
}
