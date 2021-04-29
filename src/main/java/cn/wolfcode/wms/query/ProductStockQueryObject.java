package cn.wolfcode.wms.query;

import cn.wolfcode.wms.util.StringUtil;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter@Getter
public class ProductStockQueryObject extends QueryObject {
    private String keyword;
    private Long depotId = -1L;
    private Long brandId = -1L;
    private BigDecimal warnNum;

    public String getKeyword() {
        return StringUtil.empty2Null(keyword);
    }
}
