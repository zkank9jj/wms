package cn.wolfcode.wms.query;

import lombok.Getter;
import lombok.Setter;

//普通的查询对象,仅仅包含分页操作
@Setter@Getter
public class QueryObject {
    private int currentPage = 1;
    private int pageSize = 5;

    public Integer getStart() {
        return (currentPage - 1) * pageSize;
    }
}
