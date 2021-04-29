package cn.wolfcode.wms.service.impl;

import cn.wolfcode.wms.domain.Brand;
import cn.wolfcode.wms.mapper.BrandMapper;
import cn.wolfcode.wms.query.QueryObject;
import cn.wolfcode.wms.service.IBrandService;
import cn.wolfcode.wms.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BrandServiceImpl implements IBrandService {
    @Autowired
    private BrandMapper brandMapper;

    public void saveOrUpdate(Brand entity) {
        if (!Optional.ofNullable(entity.getId()).isPresent()) {
            brandMapper.insert(entity);//没有ID
        } else {
            brandMapper.updateByPrimaryKey(entity);
        }
    }

    public void delete(Long id) {
        brandMapper.deleteByPrimaryKey(id);
    }

    public Brand get(Long id) {
        return brandMapper.selectByPrimaryKey(id);
    }

    public List<Brand> list() {
        return brandMapper.selectAll();
    }

    public PageResult query(QueryObject qo) {
        Integer rows = brandMapper.selectForCount(qo);
        if (rows == 0) {
            return PageResult.EMPTY_PAGE;
        }
        List<?> data = brandMapper.selectForList(qo);
        return new PageResult(qo.getCurrentPage(), qo.getPageSize(), data, rows);
    }
}
