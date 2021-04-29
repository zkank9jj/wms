package cn.wolfcode.wms.mapper;

import cn.wolfcode.wms.domain.OrderBill;
import cn.wolfcode.wms.query.QueryObject;

import java.util.List;

public interface OrderBillMapper {
    int deleteByPrimaryKey(Long id);

    int insert(OrderBill record);

    OrderBill selectByPrimaryKey(Long id);

    int updateByPrimaryKey(OrderBill record);

    Integer selectForCount(QueryObject qo);

    List<?> selectForList(QueryObject qo);

    /**
     * 根据单据ID删除明细
     */
    void deleteItemsByBillId(Long billId);

    /**
     * 审核单据
     * @param bill
     */
    void audit(OrderBill bill);
}