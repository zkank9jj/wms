package cn.wolfcode.wms.domain;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

//销售帐
@Setter@Getter
public class SaleAccount extends BaseDomain {
    private Date vdate; //业务时间
    private BigDecimal number; //数量
    private BigDecimal costPrice; //成本单价
    private BigDecimal costAmount; //成本总额
    private BigDecimal salePrice; //销售单价
    private BigDecimal saleAmount; //销售总额

    private Product product; //商品
    private Employee saleman; //销售员
    private Client client; //客户
}