package cn.wolfcode.wms.service;

import cn.wolfcode.wms.domain.SystemMenu;
import cn.wolfcode.wms.query.QueryObject;
import cn.wolfcode.wms.util.PageResult;

import java.util.List;
import java.util.Map;

public interface ISystemMenuService {
    void saveOrUpdate(SystemMenu entity);
    void delete(Long id);
    SystemMenu get(Long id);
    List<SystemMenu> list();
    PageResult query(QueryObject qo);

    /**
     * 根据菜单查询出面包屑
     * @param parentId
     * @return
     */
    List<SystemMenu> getParentMenus(Long parentId);

    /**
     * 根据父菜单的SN查询子菜单
     * @param menuSn
     * @return
     */
    List<Map<String, Object>> getMenusBySn(String menuSn);
}
