package cn.wolfcode.wms.mapper;

import cn.wolfcode.wms.domain.SystemMenu;
import cn.wolfcode.wms.query.QueryObject;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SystemMenuMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SystemMenu record);

    SystemMenu selectByPrimaryKey(Long id);

    List<SystemMenu> selectAll();

    int updateByPrimaryKey(SystemMenu record);

    List<?> selectForList(QueryObject qo);

    void deleteChildMenu(Long parentId);

    List<Map<String,Object>> selectMenusBySn(String menuSn);

    List<Map<String,Object>> selectMenusBySnAndEmployeeId(@Param("menuSn") String menuSn,
                                                          @Param("employeeId") Long employeeId);
}