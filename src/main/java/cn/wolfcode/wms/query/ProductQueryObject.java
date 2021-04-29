package cn.wolfcode.wms.query;

import cn.wolfcode.wms.util.StringUtil;
import lombok.Getter;
import lombok.Setter;

@Setter@Getter
public class ProductQueryObject extends QueryObject {
    private String keyword;
    private Long brandId = -1L;

    public String getKeyword() {
        return StringUtil.empty2Null(keyword);
    }
}
