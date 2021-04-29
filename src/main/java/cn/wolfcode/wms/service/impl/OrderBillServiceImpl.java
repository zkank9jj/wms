package cn.wolfcode.wms.service.impl;

import cn.wolfcode.wms.domain.OrderBill;
import cn.wolfcode.wms.domain.OrderBillItem;
import cn.wolfcode.wms.mapper.OrderBillItemMapper;
import cn.wolfcode.wms.mapper.OrderBillMapper;
import cn.wolfcode.wms.query.QueryObject;
import cn.wolfcode.wms.service.IOrderBillService;
import cn.wolfcode.wms.util.PageResult;
import cn.wolfcode.wms.util.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class OrderBillServiceImpl implements IOrderBillService {
    @Autowired
    private OrderBillMapper billMapper; //主对象的mapper
    @Autowired
    private OrderBillItemMapper itemMapper; //从对象的mapper

    public void audit(Long id) {
        //先检查当前单据的状态
        //判断当前单据必须是未审核状态
        OrderBill bill = billMapper.selectByPrimaryKey(id);
        if (bill.getStatus() == OrderBill.NO_AUDIT) {
            bill.setStatus(OrderBill.AUDITED); //设置已审核状态
            //设置审核人和审核的时间
            bill.setAuditor(UserContext.getCurrentUser());
            bill.setAuditTime(new Date());
            //完成审核更新
            billMapper.audit(bill);
        }
    }

    public void saveOrUpdate(OrderBill entity) {
        if (entity.getItems().size() == 0) {
            throw new RuntimeException("至少有1条商品明显");
        }
        if (!Optional.ofNullable(entity.getId()).isPresent()) {
            saveBill(entity);
        } else {
            updateBill(entity);
        }
    }

    //更新单据的业务操作
    private void updateBill(OrderBill entity) {
        //判断当前单据必须是未审核状态
        OrderBill old = billMapper.selectByPrimaryKey(entity.getId());
        if (old.getStatus() == OrderBill.NO_AUDIT) {
            //1:删除旧的明显
            billMapper.deleteItemsByBillId(entity.getId());
            //2:重新计算新的小计和总计
            BigDecimal totalNumber = BigDecimal.ZERO;
            BigDecimal totalAmount = BigDecimal.ZERO;
            for (OrderBillItem item : entity.getItems()) {
                //重新计算小计
                BigDecimal amount = item.getCostPrice()
                        .multiply(item.getNumber()).setScale(2, RoundingMode.HALF_UP);
                item.setAmount(amount);
                //累加到总计上
                totalNumber = totalNumber.add(item.getNumber());
                totalAmount = totalAmount.add(amount);
                //5:直接保存新的明细
                item.setBillId(entity.getId());
                itemMapper.insert(item);
            }
            /*entity.setTotalNumber(totalNumber);
            entity.setTotalAmount(totalAmount);
            //3:新的数据同步过来
            entity.setInputUser(old.getInputUser());
            entity.setInputTime(old.getInputTime());
            //4:更新单据
            billMapper.updateByPrimaryKey(entity);*/
            old.setTotalNumber(totalNumber);
            old.setTotalAmount(totalAmount);
            //3:新的数据同步过来
            old.setSn(entity.getSn());
            old.setVdate(entity.getVdate());
            old.setSupplier(entity.getSupplier());
            //4:更新单据
            billMapper.updateByPrimaryKey(old);
        }
    }

    //新增单据的业务操作
    private void saveBill(OrderBill entity) {
        //1:所有的关键信息必须手动设置一遍
        //单据状态 / 录入人 / 录入时间
        entity.setStatus(OrderBill.NO_AUDIT); //设置未审核
        entity.setInputUser(UserContext.getCurrentUser()); //设置录入人
        entity.setInputTime(new Date()); //设置录入时间
        //2:重新后台计算明显的小计和单据的总计
        BigDecimal totalNumber = BigDecimal.ZERO;
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (OrderBillItem item : entity.getItems()) {
            //重新计算小计
            BigDecimal amount = item.getCostPrice()
                    .multiply(item.getNumber()).setScale(2, RoundingMode.HALF_UP);
            item.setAmount(amount);
            //累加到总计上
            totalNumber = totalNumber.add(item.getNumber());
            totalAmount = totalAmount.add(amount);
        }
        entity.setTotalNumber(totalNumber);
        entity.setTotalAmount(totalAmount);
        //3:保存到数据库
        billMapper.insert(entity);
        for (OrderBillItem item : entity.getItems()) {
            //给每个明显设置单据ID
            item.setBillId(entity.getId());
            itemMapper.insert(item);
        }
    }

    public void delete(Long id) {
        //判断当前单据必须是未审核状态
        OrderBill old = billMapper.selectByPrimaryKey(id);
        if (old.getStatus() == OrderBill.NO_AUDIT) {
            //先删除明显再删除单据
            billMapper.deleteItemsByBillId(id);
            billMapper.deleteByPrimaryKey(id);
        }
    }

    public OrderBill get(Long id) {
        return billMapper.selectByPrimaryKey(id);
    }

    public PageResult query(QueryObject qo) {
        Integer rows = billMapper.selectForCount(qo);
        if (rows == 0) {
            return PageResult.EMPTY_PAGE;
        }
        List<?> data = billMapper.selectForList(qo);
        return new PageResult(qo.getCurrentPage(), qo.getPageSize(), data, rows);
    }
}
