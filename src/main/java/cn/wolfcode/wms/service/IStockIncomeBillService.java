package cn.wolfcode.wms.service;

import cn.wolfcode.wms.domain.StockIncomeBill;
import cn.wolfcode.wms.query.QueryObject;
import cn.wolfcode.wms.util.PageResult;

public interface IStockIncomeBillService {
    void saveOrUpdate(StockIncomeBill entity);
    void delete(Long id);
    StockIncomeBill get(Long id);
    PageResult query(QueryObject qo);

    /**
     * 根据单据的ID来审核单据
     * @param id
     */
    void audit(Long id);
}
