package cn.wolfcode.wms.query;

import cn.wolfcode.wms.util.DateUtil;
import cn.wolfcode.wms.util.StringUtil;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Setter@Getter
public class SaleChartQueryObject extends QueryObject {
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date beginDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;
    private String keyword;
    private Long clientId = -1L;
    private Long brandId = -1L;
    private String groupType = "e.name"; //默认按照订货员做查询

    public String getKeyword() {
        return StringUtil.empty2Null(keyword);
    }

    public Date getEndDate() {
        return DateUtil.getEndDate(endDate);
    }
}
