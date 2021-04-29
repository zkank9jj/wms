package cn.wolfcode.wms.service.impl;

import cn.wolfcode.wms.domain.Depot;
import cn.wolfcode.wms.mapper.DepotMapper;
import cn.wolfcode.wms.query.QueryObject;
import cn.wolfcode.wms.service.IDepotService;
import cn.wolfcode.wms.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DepotServiceImpl implements IDepotService {
    @Autowired
    private DepotMapper depotMapper;

    public void saveOrUpdate(Depot entity) {
        if (!Optional.ofNullable(entity.getId()).isPresent()) {
            depotMapper.insert(entity);//没有ID
        } else {
            depotMapper.updateByPrimaryKey(entity);
        }
    }

    public void delete(Long id) {
        depotMapper.deleteByPrimaryKey(id);
    }

    public Depot get(Long id) {
        return depotMapper.selectByPrimaryKey(id);
    }

    public List<Depot> list() {
        return depotMapper.selectAll();
    }

    public PageResult query(QueryObject qo) {
        Integer rows = depotMapper.selectForCount(qo);
        if (rows == 0) {
            return PageResult.EMPTY_PAGE;
        }
        List<?> data = depotMapper.selectForList(qo);
        return new PageResult(qo.getCurrentPage(), qo.getPageSize(), data, rows);
    }
}
