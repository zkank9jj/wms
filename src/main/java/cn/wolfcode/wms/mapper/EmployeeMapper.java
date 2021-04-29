package cn.wolfcode.wms.mapper;

import cn.wolfcode.wms.domain.Employee;
import cn.wolfcode.wms.query.QueryObject;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EmployeeMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Employee record);

    Employee selectByPrimaryKey(Long id);

    int updateByPrimaryKey(Employee record);

    Integer selectForCount(QueryObject qo);

    List<?> selectForList(QueryObject qo);

    void insertRelaction(@Param("employeeId") Long employeeId,
                         @Param("roleId") Long roleId);

    void deleteRelaction(Long employeeId);

    Employee selectByInfo(@Param("username") String username,
                          @Param("password") String password);

    void batchDelete(Long[] ids);
}