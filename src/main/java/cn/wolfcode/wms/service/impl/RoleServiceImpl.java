package cn.wolfcode.wms.service.impl;

import cn.wolfcode.wms.domain.Role;
import cn.wolfcode.wms.mapper.RoleMapper;
import cn.wolfcode.wms.query.QueryObject;
import cn.wolfcode.wms.service.IRoleService;
import cn.wolfcode.wms.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImpl implements IRoleService {
    @Autowired
    private RoleMapper roleMapper;

    public void saveOrUpdate(Role entity, Long[] permissionIds, Long[] menuIds) {
        if (!Optional.ofNullable(entity.getId()).isPresent()) {
            roleMapper.insert(entity);//没有ID
        } else {
            //先删除旧的关系
            roleMapper.deleteMenuRelation(entity.getId());
            roleMapper.deleteRelation(entity.getId());
            roleMapper.updateByPrimaryKey(entity);
        }
        //维护关系
        if (Optional.ofNullable(permissionIds).isPresent()) {
            for (Long permissionId : permissionIds) {
                roleMapper.insertRelation(entity.getId(), permissionId);
            }
        }
        if (Optional.ofNullable(menuIds).isPresent()) {
            for (Long menuId : menuIds) {
                roleMapper.insertMenuRelation(entity.getId(), menuId);
            }
        }
    }


    public void delete(Long id) {
        roleMapper.deleteRelation(id);
        roleMapper.deleteMenuRelation(id);
        roleMapper.deleteByPrimaryKey(id);
    }

    public Role get(Long id) {
        return roleMapper.selectByPrimaryKey(id);
    }

    public List<Role> list() {
        return roleMapper.selectAll();
    }

    public PageResult query(QueryObject qo) {
        Integer rows = roleMapper.selectForCount(qo);
        if (rows == 0) {
            return PageResult.EMPTY_PAGE;
        }
        List<?> data = roleMapper.selectForList(qo);
        return new PageResult(qo.getCurrentPage(), qo.getPageSize(), data, rows);
    }
}
