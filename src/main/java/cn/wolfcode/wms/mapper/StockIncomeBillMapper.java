package cn.wolfcode.wms.mapper;

import cn.wolfcode.wms.domain.StockIncomeBill;
import cn.wolfcode.wms.query.QueryObject;

import java.util.List;

public interface StockIncomeBillMapper {
    int deleteByPrimaryKey(Long id);

    int insert(StockIncomeBill record);

    StockIncomeBill selectByPrimaryKey(Long id);

    int updateByPrimaryKey(StockIncomeBill record);

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
    void audit(StockIncomeBill bill);
}