package cn.wolfcode.wms.service.impl;

import cn.wolfcode.wms.domain.Product;
import cn.wolfcode.wms.mapper.BrandMapper;
import cn.wolfcode.wms.mapper.ProductMapper;
import cn.wolfcode.wms.query.QueryObject;
import cn.wolfcode.wms.service.IProductService;
import cn.wolfcode.wms.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements IProductService {
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private BrandMapper brandMapper;

    public void saveOrUpdate(Product entity) {
        //设置冗余数据的值,品牌名称
        String brandName = brandMapper.selectByPrimaryKey(entity.getBrandId()).getName();
        entity.setBrandName(brandName);
        if (!Optional.ofNullable(entity.getId()).isPresent()) {
            productMapper.insert(entity);//没有ID
        } else {
            productMapper.updateByPrimaryKey(entity);
        }
    }

    public void delete(Long id) {
        productMapper.deleteByPrimaryKey(id);
    }

    public Product get(Long id) {
        return productMapper.selectByPrimaryKey(id);
    }

    public PageResult query(QueryObject qo) {
        Integer rows = productMapper.selectForCount(qo);
        if (rows == 0) {
            return PageResult.EMPTY_PAGE;
        }
        List<?> data = productMapper.selectForList(qo);
        return new PageResult(qo.getCurrentPage(), qo.getPageSize(), data, rows);
    }
}
