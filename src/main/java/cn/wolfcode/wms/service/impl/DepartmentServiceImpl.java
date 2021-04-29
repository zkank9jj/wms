package cn.wolfcode.wms.service.impl;

import cn.wolfcode.wms.domain.Department;
import cn.wolfcode.wms.mapper.DepartmentMapper;
import cn.wolfcode.wms.query.QueryObject;
import cn.wolfcode.wms.service.IDepartmentService;
import cn.wolfcode.wms.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DepartmentServiceImpl implements IDepartmentService {
    @Autowired
    private DepartmentMapper departmentMapper;

    public void saveOrUpdate(Department entity) {
        if (!Optional.ofNullable(entity.getId()).isPresent()) {
            departmentMapper.insert(entity);//没有ID
        } else {
            departmentMapper.updateByPrimaryKey(entity);
        }
    }

    public void delete(Long id) {
        departmentMapper.deleteByPrimaryKey(id);
    }

    public Department get(Long id) {
        return departmentMapper.selectByPrimaryKey(id);
    }

    public List<Department> list() {
        return departmentMapper.selectAll();
    }

    public PageResult query(QueryObject qo) {
        Integer rows = departmentMapper.selectForCount(qo);
        if (rows == 0) {
            return PageResult.EMPTY_PAGE;
        }
        List<?> data = departmentMapper.selectForList(qo);
        return new PageResult(qo.getCurrentPage(), qo.getPageSize(), data, rows);
    }
}
