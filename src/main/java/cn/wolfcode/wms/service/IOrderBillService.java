package cn.wolfcode.wms.service;

import cn.wolfcode.wms.domain.OrderBill;
import cn.wolfcode.wms.query.QueryObject;
import cn.wolfcode.wms.util.PageResult;

import java.util.List;

public interface IOrderBillService {
    void saveOrUpdate(OrderBill entity);
    void delete(Long id);
    OrderBill get(Long id);
    PageResult query(QueryObject qo);

    /**
     * 根据单据的ID来审核单据
     * @param id
     */
    void audit(Long id);
}
