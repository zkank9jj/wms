package cn.wolfcode.wms.mapper;

import cn.wolfcode.wms.domain.Depot;
import cn.wolfcode.wms.query.QueryObject;

import java.util.List;

public interface DepotMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Depot record);

    Depot selectByPrimaryKey(Long id);

    List<Depot> selectAll();

    int updateByPrimaryKey(Depot record);

    Integer selectForCount(QueryObject qo);

    List<?> selectForList(QueryObject qo);
}