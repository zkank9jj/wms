package cn.wolfcode.wms.service;

import cn.wolfcode.wms.domain.Employee;
import cn.wolfcode.wms.query.QueryObject;
import cn.wolfcode.wms.util.PageResult;

import java.util.List;

public interface IEmployeeService {
    void saveOrUpdate(Employee entity, Long[] roleIds);
    void delete(Long id);
    Employee get(Long id);
    PageResult query(QueryObject qo);

    void login(String username, String password);

    void batchDelete(Long[] ids);
}
