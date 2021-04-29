package cn.wolfcode.wms.service.impl;

import cn.wolfcode.wms.domain.Employee;
import cn.wolfcode.wms.domain.SystemMenu;
import cn.wolfcode.wms.mapper.SystemMenuMapper;
import cn.wolfcode.wms.query.QueryObject;
import cn.wolfcode.wms.service.ISystemMenuService;
import cn.wolfcode.wms.util.PageResult;
import cn.wolfcode.wms.util.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SystemMenuServiceImpl implements ISystemMenuService {
    @Autowired
    private SystemMenuMapper systemMenuMapper;

    public List<Map<String, Object>> getMenusBySn(String menuSn) {
        List<Map<String, Object>> menus = null;
        //超管显示所有菜单,非超管根据拥有的角色查询对应的菜单
        Employee emp = UserContext.getCurrentUser();
        if (emp.isAdmin()) {
            menus = systemMenuMapper.selectMenusBySn(menuSn);
        } else {
            menus = systemMenuMapper.selectMenusBySnAndEmployeeId(menuSn, emp.getId());
        }
        return menus;
    }

    public List<SystemMenu> getParentMenus(Long parentId) {
        List<SystemMenu> menus = new ArrayList<>();
        SystemMenu menu = systemMenuMapper.selectByPrimaryKey(parentId);
        menus.add(menu);
        while (Optional.ofNullable(menu.getParent()).isPresent()) {
            //有父菜单
            menu = menu.getParent();
            menus.add(menu);
        }
        Collections.reverse(menus); //颠倒元素的顺序
        return menus;
    }

    public void saveOrUpdate(SystemMenu entity) {
        if (!Optional.ofNullable(entity.getId()).isPresent()) {
            systemMenuMapper.insert(entity);//没有ID
        } else {
            systemMenuMapper.updateByPrimaryKey(entity);
        }
    }

    public void delete(Long id) {
        //先删除子菜单,然后再删除自己
        systemMenuMapper.deleteChildMenu(id);
        systemMenuMapper.deleteByPrimaryKey(id);
    }

    public SystemMenu get(Long id) {
        return systemMenuMapper.selectByPrimaryKey(id);
    }

    public List<SystemMenu> list() {
        return systemMenuMapper.selectAll();
    }

    public PageResult query(QueryObject qo) {
        List<?> data = systemMenuMapper.selectForList(qo);
        return new PageResult(qo.getCurrentPage(), qo.getPageSize(), data, 0);
    }
}
