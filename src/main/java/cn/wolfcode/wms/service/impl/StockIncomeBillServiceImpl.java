package cn.wolfcode.wms.service.impl;

import cn.wolfcode.wms.domain.*;
import cn.wolfcode.wms.mapper.ProductStockMapper;
import cn.wolfcode.wms.mapper.StockIncomeBillItemMapper;
import cn.wolfcode.wms.mapper.StockIncomeBillMapper;
import cn.wolfcode.wms.query.QueryObject;
import cn.wolfcode.wms.service.IStockIncomeBillService;
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
public class StockIncomeBillServiceImpl implements IStockIncomeBillService {
    @Autowired
    private StockIncomeBillMapper billMapper; //主对象的mapper
    @Autowired
    private StockIncomeBillItemMapper itemMapper; //从对象的mapper
    @Autowired
    private ProductStockMapper psMapper;

    public void audit(Long id) {
        //判断当前单据必须是未审核状态
        StockIncomeBill bill = billMapper.selectByPrimaryKey(id);
        if (bill.getStatus() == StockIncomeBill.NO_AUDIT) {
            Depot depot = bill.getDepot(); //入的仓库
            //拿到当前单据的明细,存入数据库
            for (StockIncomeBillItem item : bill.getItems()) {
                //拿到当前明细入的商品
                Product p = item.getProduct();
                //当前仓库中是否有该商品
                ProductStock ps = psMapper.selectByDepotIdAndProductId(depot.getId(), p.getId());
                if (Optional.ofNullable(ps).isPresent()) {
                    //有库存,移动加权平均算法,重新计算库存价格
                    BigDecimal amount = ps.getAmount().add(item.getAmount());
                    BigDecimal num = ps.getStoreNumber().add(item.getNumber());
                    BigDecimal price = amount.divide(num, RoundingMode.HALF_UP);
                    //把新的数据设置到库存对象中
                    ps.setPrice(price);
                    ps.setStoreNumber(num);
                    ps.setAmount(amount);
                    //更新到数据库
                    psMapper.updateByPrimaryKey(ps);
                } else {
                    //没有库存,第一次进货
                    ps = new ProductStock();
                    ps.setProduct(p);
                    ps.setDepot(depot);
                    ps.setPrice(item.getCostPrice());
                    ps.setStoreNumber(item.getNumber());
                    ps.setAmount(item.getAmount());
                    //保存到数据库
                    psMapper.insert(ps);
                }
            }
            //更改当前单据的状态
            bill.setStatus(StockIncomeBill.AUDITED);
            bill.setAuditor(UserContext.getCurrentUser());
            bill.setAuditTime(new Date());
            billMapper.audit(bill);
        }
    }

    public void saveOrUpdate(StockIncomeBill entity) {
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
    private void updateBill(StockIncomeBill entity) {
        //判断当前单据必须是未审核状态
        StockIncomeBill old = billMapper.selectByPrimaryKey(entity.getId());
        if (old.getStatus() == StockIncomeBill.NO_AUDIT) {
            //1:删除旧的明显
            billMapper.deleteItemsByBillId(entity.getId());
            //2:重新计算新的小计和总计
            BigDecimal totalNumber = BigDecimal.ZERO;
            BigDecimal totalAmount = BigDecimal.ZERO;
            for (StockIncomeBillItem item : entity.getItems()) {
                //重新计算小计
                BigDecimal amount = item.getCostPrice().multiply(item.getNumber())
                        .setScale(2, RoundingMode.HALF_UP);
                item.setAmount(amount);
                //累加到总计上
                totalNumber = totalNumber.add(item.getNumber());
                totalAmount = totalAmount.add(amount);
                //直接保存新的明细
                item.setBillId(entity.getId());
                itemMapper.insert(item);
            }
            //3:新的数据同步过来
            old.setSn(entity.getSn());
            old.setDepot(entity.getDepot());
            old.setVdate(entity.getVdate());
            old.setTotalNumber(totalNumber);
            old.setTotalAmount(totalAmount);
            //4:更新单据
            billMapper.updateByPrimaryKey(old);
        }
    }

    //新增单据的业务操作
    private void saveBill(StockIncomeBill entity) {
        //1:所有的关键信息必须手动设置一遍
        //单据状态 / 录入人 / 录入时间
        entity.setStatus(StockIncomeBill.NO_AUDIT);
        entity.setInputUser(UserContext.getCurrentUser());
        entity.setInputTime(new Date());
        //2:重新后台计算明显的小计和单据的总计
        BigDecimal totalNumber = BigDecimal.ZERO;
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (StockIncomeBillItem item : entity.getItems()) {
            //重新计算小计
            BigDecimal amount = item.getCostPrice().multiply(item.getNumber())
                    .setScale(2, RoundingMode.HALF_UP);
            item.setAmount(amount);
            //累加到总计上
            totalNumber = totalNumber.add(item.getNumber());
            totalAmount = totalAmount.add(amount);
        }
        entity.setTotalNumber(totalNumber);
        entity.setTotalAmount(totalAmount);
        //3:保存到数据库
        billMapper.insert(entity);
        for (StockIncomeBillItem item : entity.getItems()) {
            //给每个明显设置单据ID
            item.setBillId(entity.getId());
            itemMapper.insert(item);
        }
    }

    public void delete(Long id) {
        //判断当前单据必须是未审核状态
        StockIncomeBill bill = billMapper.selectByPrimaryKey(id);
        if (bill.getStatus() == StockIncomeBill.NO_AUDIT) {
            //先删除明显再删除单据
            billMapper.deleteItemsByBillId(id);
            billMapper.deleteByPrimaryKey(id);
        }
    }

    public StockIncomeBill get(Long id) {
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
