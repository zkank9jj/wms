package cn.wolfcode.wms.service;

import cn.wolfcode.wms.domain.StockOutcomeBill;
import cn.wolfcode.wms.query.QueryObject;
import cn.wolfcode.wms.util.PageResult;

public interface IStockOutcomeBillService {
    void saveOrUpdate(StockOutcomeBill entity);
    void delete(Long id);
    StockOutcomeBill get(Long id);
    PageResult query(QueryObject qo);

    /**
     * 根据单据的ID来审核单据
     * @param id
     */
    void audit(Long id);
}
