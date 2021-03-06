package cn.wolfcode.wms.mapper;

import cn.wolfcode.wms.domain.Brand;
import cn.wolfcode.wms.query.QueryObject;

import java.util.List;

public interface BrandMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Brand record);

    Brand selectByPrimaryKey(Long id);

    List<Brand> selectAll();

    int updateByPrimaryKey(Brand record);

    Integer selectForCount(QueryObject qo);

    List<?> selectForList(QueryObject qo);
}