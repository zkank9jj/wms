package cn.wolfcode.wms.domain;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter@Getter
public class ProductStock extends BaseDomain {
    private BigDecimal price; //库存价格
    private BigDecimal storeNumber; //库存数量
    private BigDecimal amount; //库存商品小计

    private Product product; //商品
    private Depot depot; //仓库
}