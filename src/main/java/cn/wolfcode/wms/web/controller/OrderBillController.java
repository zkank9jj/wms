package cn.wolfcode.wms.web.controller;

import cn.wolfcode.wms.domain.OrderBill;
import cn.wolfcode.wms.query.OrderBillQueryObject;
import cn.wolfcode.wms.query.QueryObject;
import cn.wolfcode.wms.service.IOrderBillService;
import cn.wolfcode.wms.service.ISupplierService;
import cn.wolfcode.wms.util.JSONResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Optional;

@Controller
@RequestMapping("orderBill")
public class OrderBillController {
    @Autowired
    private IOrderBillService orderBillService;
    @Autowired
    private ISupplierService supplierService;

    @RequestMapping("list")
    public String list(@ModelAttribute("qo") OrderBillQueryObject qo, Model model) {
        model.addAttribute("suppliers", supplierService.list());
        model.addAttribute("result", orderBillService.query(qo));
        return "orderBill/list";
    }

    @RequestMapping("input")
    public String input(Long id, Model model) {
        model.addAttribute("suppliers", supplierService.list());
        if (Optional.ofNullable(id).isPresent()) {
            model.addAttribute("entity", orderBillService.get(id)); //有ID
        }
        return "orderBill/input";
    }

    //查看单据信息
    @RequestMapping("billView")
    public String billView(Long id, Model model) {
        model.addAttribute("suppliers", supplierService.list());
        if (Optional.ofNullable(id).isPresent()) {
            model.addAttribute("entity", orderBillService.get(id));
        }
        return "orderBill/billView";
    }

    @RequestMapping("saveOrUpdate")
    @ResponseBody
    public Object saveOrUpdate(OrderBill entity) {
        orderBillService.saveOrUpdate(entity);
        return new JSONResult();
    }

    @RequestMapping("delete")
    @ResponseBody
    public Object delete(Long id) {
        if (Optional.ofNullable(id).isPresent()) {
            orderBillService.delete(id); //有ID
        }
        return new JSONResult(); //象征性返回
    }

    //单据的审核操作
    @RequestMapping("audit")
    @ResponseBody
    public Object audit(Long id) {
        if (Optional.ofNullable(id).isPresent()) {
            orderBillService.audit(id);
        }
        return new JSONResult();
    }
}
