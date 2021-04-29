package cn.wolfcode.wms.web.controller;

import cn.wolfcode.wms.domain.StockOutcomeBill;
import cn.wolfcode.wms.query.StockOutcomeBillQueryObject;
import cn.wolfcode.wms.service.IClientService;
import cn.wolfcode.wms.service.IDepotService;
import cn.wolfcode.wms.service.IStockOutcomeBillService;
import cn.wolfcode.wms.util.JSONResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Optional;

@Controller
@RequestMapping("stockOutcomeBill")
public class StockOutcomeBillController {
    @Autowired
    private IStockOutcomeBillService stockOutcomeBillService;
    @Autowired
    private IDepotService depotService;
    @Autowired
    private IClientService clientService;

    @RequestMapping("list")
    public String list(@ModelAttribute("qo") StockOutcomeBillQueryObject qo, Model model) {
        model.addAttribute("depots", depotService.list());
        model.addAttribute("clients", clientService.list());
        model.addAttribute("result", stockOutcomeBillService.query(qo));
        return "stockOutcomeBill/list";
    }

    @RequestMapping("input")
    public String input(Long id, Model model) {
        model.addAttribute("depots", depotService.list());
        model.addAttribute("clients", clientService.list());
        if (Optional.ofNullable(id).isPresent()) {
            model.addAttribute("entity", stockOutcomeBillService.get(id)); //有ID
        }
        return "stockOutcomeBill/input";
    }

    //查看单据信息
    @RequestMapping("billView")
    public String billView(Long id, Model model) {
        if (Optional.ofNullable(id).isPresent()) {
            model.addAttribute("entity", stockOutcomeBillService.get(id));
        }
        return "stockOutcomeBill/billView";
    }

    @RequestMapping("saveOrUpdate")
    @ResponseBody
    public Object saveOrUpdate(StockOutcomeBill entity) {
        stockOutcomeBillService.saveOrUpdate(entity);
        return new JSONResult();
    }

    @RequestMapping("delete")
    @ResponseBody
    public Object delete(Long id) {
        if (Optional.ofNullable(id).isPresent()) {
            stockOutcomeBillService.delete(id); //有ID
        }
        return new JSONResult(); //象征性返回
    }

    //单据的审核操作
    @RequestMapping("audit")
    @ResponseBody
    public Object audit(Long id) {
        JSONResult result = new JSONResult();
        try {
            if (Optional.ofNullable(id).isPresent()) {
                stockOutcomeBillService.audit(id);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.mark(e.getMessage());
        }
        return result;
    }
}
