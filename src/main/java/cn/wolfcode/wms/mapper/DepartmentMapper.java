package cn.wolfcode.wms.mapper;

import cn.wolfcode.wms.domain.Department;
import cn.wolfcode.wms.query.QueryObject;

import java.util.List;

public interface DepartmentMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Department record);

    Department selectByPrimaryKey(Long id);

    List<Department> selectAll();

    int updateByPrimaryKey(Department record);

    Integer selectForCount(QueryObject qo);

    List<?> selectForList(QueryObject qo);
}