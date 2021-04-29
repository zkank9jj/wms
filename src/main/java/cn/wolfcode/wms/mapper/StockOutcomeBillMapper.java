package cn.wolfcode.wms.mapper;

import cn.wolfcode.wms.domain.StockOutcomeBill;
import cn.wolfcode.wms.query.QueryObject;

import java.util.List;

public interface StockOutcomeBillMapper {
    int deleteByPrimaryKey(Long id);

    int insert(StockOutcomeBill record);

    StockOutcomeBill selectByPrimaryKey(Long id);

    int updateByPrimaryKey(StockOutcomeBill record);

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
    void audit(StockOutcomeBill bill);
}