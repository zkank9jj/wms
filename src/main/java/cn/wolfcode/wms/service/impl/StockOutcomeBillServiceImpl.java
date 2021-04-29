package cn.wolfcode.wms.service.impl;

import cn.wolfcode.wms.domain.*;
import cn.wolfcode.wms.mapper.ProductStockMapper;
import cn.wolfcode.wms.mapper.SaleAccountMapper;
import cn.wolfcode.wms.mapper.StockOutcomeBillItemMapper;
import cn.wolfcode.wms.mapper.StockOutcomeBillMapper;
import cn.wolfcode.wms.query.QueryObject;
import cn.wolfcode.wms.service.IStockOutcomeBillService;
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
public class StockOutcomeBillServiceImpl implements IStockOutcomeBillService {
    @Autowired
    private StockOutcomeBillMapper billMapper; //主对象的mapper
    @Autowired
    private StockOutcomeBillItemMapper itemMapper; //从对象的mapper
    @Autowired
    private ProductStockMapper psMapper; //库存mapper
    @Autowired
    private SaleAccountMapper saleAccountMapper; //销售帐mapper

    public void audit(Long id) {
        //判断当前单据必须是未审核状态
        StockOutcomeBill bill = billMapper.selectByPrimaryKey(id);
        if (bill.getStatus() == StockOutcomeBill.NO_AUDIT) {
            Depot depot = bill.getDepot(); //拿到仓库
            //拿到出库的明细
            for (StockOutcomeBillItem item : bill.getItems()) {
                //查询当前仓库中是否有该商品
                Product p = item.getProduct();
                ProductStock ps = psMapper.selectByDepotIdAndProductId(depot.getId(), p.getId());
                if (!Optional.ofNullable(ps).isPresent()) { //没有库存
                    String msg = "仓库:"+depot.getName()+"没有商品:"+p.getName()+"的库存";
                    throw new RuntimeException(msg);
                }
                //有库存,检查数量是否足够
                if (item.getNumber().compareTo(ps.getStoreNumber()) > 0) {
                    String msg = "仓库:"+depot.getName()+"中商品:"+p.getName()+"的库存是:"+
                            ps.getStoreNumber()+",不足出库的数量:"+item.getNumber();
                    throw new RuntimeException(msg);
                }
                //库存充足,更新库存数量和总价
                BigDecimal amount = ps.getAmount().subtract(ps.getPrice().multiply(item.getNumber()));
                ps.setAmount(amount.setScale(2, RoundingMode.HALF_UP));
                BigDecimal stockNumber = ps.getStoreNumber().subtract(item.getNumber());
                ps.setStoreNumber(stockNumber);
                psMapper.updateByPrimaryKey(ps);
                //记录销售帐
                SaleAccount sa = new SaleAccount();
                sa.setVdate(new Date());
                sa.setNumber(item.getNumber());
                sa.setCostPrice(ps.getPrice());
                sa.setCostAmount(ps.getPrice().multiply(item.getNumber())
                        .setScale(2, RoundingMode.HALF_UP));
                sa.setSalePrice(item.getSalePrice());
                sa.setSaleAmount(item.getAmount());
                sa.setProduct(item.getProduct());
                sa.setSaleman(bill.getInputUser());
                sa.setClient(bill.getClient());
                saleAccountMapper.insert(sa);
            }
            //更改当前单据的状态
            bill.setAuditor(UserContext.getCurrentUser());
            bill.setAuditTime(new Date());
            bill.setStatus(StockOutcomeBill.AUDITED);
            billMapper.audit(bill);
        }
    }

    public void saveOrUpdate(StockOutcomeBill entity) {
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
    private void updateBill(StockOutcomeBill entity) {
        //判断当前单据必须是未审核状态
        StockOutcomeBill old = billMapper.selectByPrimaryKey(entity.getId());
        if (old.getStatus() == StockOutcomeBill.NO_AUDIT) {
            //1:删除旧的明显
            billMapper.deleteItemsByBillId(entity.getId());
            //2:重新计算新的小计和总计
            BigDecimal totalNumber = BigDecimal.ZERO;
            BigDecimal totalAmount = BigDecimal.ZERO;
            for (StockOutcomeBillItem item : entity.getItems()) {
                //重新计算小计
                BigDecimal amount = item.getSalePrice().multiply(item.getNumber())
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
            old.setClient(entity.getClient());
            old.setTotalNumber(totalNumber);
            old.setTotalAmount(totalAmount);
            //4:更新单据
            billMapper.updateByPrimaryKey(old);
        }
    }

    //新增单据的业务操作
    private void saveBill(StockOutcomeBill entity) {
        //1:所有的关键信息必须手动设置一遍
        //单据状态 / 录入人 / 录入时间
        entity.setStatus(StockOutcomeBill.NO_AUDIT);
        entity.setInputUser(UserContext.getCurrentUser());
        entity.setInputTime(new Date());
        //2:重新后台计算明显的小计和单据的总计
        BigDecimal totalNumber = BigDecimal.ZERO;
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (StockOutcomeBillItem item : entity.getItems()) {
            //重新计算小计
            BigDecimal amount = item.getSalePrice().multiply(item.getNumber())
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
        for (StockOutcomeBillItem item : entity.getItems()) {
            //给每个明显设置单据ID
            item.setBillId(entity.getId());
            itemMapper.insert(item);
        }
    }

    public void delete(Long id) {
        //判断当前单据必须是未审核状态
        StockOutcomeBill bill = billMapper.selectByPrimaryKey(id);
        if (bill.getStatus() == StockOutcomeBill.NO_AUDIT) {
            //先删除明显再删除单据
            billMapper.deleteItemsByBillId(id);
            billMapper.deleteByPrimaryKey(id);
        }
    }

    public StockOutcomeBill get(Long id) {
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
