package cn.wolfcode.wms.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//入库单对象
@Setter@Getter
public class StockIncomeBill extends BaseDomain {
    //单据的状态码是固定的值,做成常量
    public static final Integer NO_AUDIT = 1; //未审核
    public static final Integer AUDITED = 2; //已审核

    private String sn; //单据编码
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date vdate; //业务时间
    private Integer status = NO_AUDIT; //审核状态,默认没有审核
    private BigDecimal totalAmount; //总计金额
    private BigDecimal totalNumber; //总计数量
    private Date auditTime; //审核时间
    private Date inputTime; //录入时间

    //关联对象
    private Employee inputUser; //录入人
    private Employee auditor; //审核人
    private Depot depot; //仓库

    //包含的明细
    private List<StockIncomeBillItem> items = new ArrayList<>();
}
