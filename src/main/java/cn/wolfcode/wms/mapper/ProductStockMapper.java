package cn.wolfcode.wms.mapper;

import cn.wolfcode.wms.domain.ProductStock;
import cn.wolfcode.wms.query.QueryObject;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductStockMapper {
    int insert(ProductStock record);

    ProductStock selectByDepotIdAndProductId(@Param("depotId") Long depotId,
                                             @Param("productId") Long productId);

    int updateByPrimaryKey(ProductStock record);

    Integer selectForCount(QueryObject qo);

    List<?> selectForList(QueryObject qo);
}