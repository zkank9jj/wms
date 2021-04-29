package cn.wolfcode.wms.service;

import cn.wolfcode.wms.query.QueryObject;
import cn.wolfcode.wms.util.PageResult;

public interface IProductStockService {
    PageResult query(QueryObject qo);
}
