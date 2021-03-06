package cn.wolfcode.wms.domain;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

//入库单明细对象
@Setter@Getter
public class StockIncomeBillItem extends BaseDomain {
    private BigDecimal costPrice; //成本价
    private BigDecimal number; //数量
    private BigDecimal amount; //小计
    private String remark; //备注
    private Long billId; //单据ID

    //关联对象
    private Product product;
}




























































































































































































































































































































