package cn.wolfcode.wms.web.controller;

import cn.wolfcode.wms.domain.StockIncomeBill;
import cn.wolfcode.wms.query.StockIncomeBillQueryObject;
import cn.wolfcode.wms.service.IStockIncomeBillService;
import cn.wolfcode.wms.service.IDepotService;
import cn.wolfcode.wms.util.JSONResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Optional;

@Controller
@RequestMapping("stockIncomeBill")
public class StockIncomeBillController {
    @Autowired
    private IStockIncomeBillService stockIncomeBillService;
    @Autowired
    private IDepotService depotService;

    @RequestMapping("list")
    public String list(@ModelAttribute("qo") StockIncomeBillQueryObject qo, Model model) {
        model.addAttribute("depots", depotService.list());
        model.addAttribute("result", stockIncomeBillService.query(qo));
        return "stockIncomeBill/list";
    }

    @RequestMapping("input")
    public String input(Long id, Model model) {
        model.addAttribute("depots", depotService.list());
        if (Optional.ofNullable(id).isPresent()) {
            model.addAttribute("entity", stockIncomeBillService.get(id)); //有ID
        }
        return "stockIncomeBill/input";
    }

    //查看单据信息
    @RequestMapping("billView")
    public String billView(Long id, Model model) {
        model.addAttribute("depots", depotService.list());
        if (Optional.ofNullable(id).isPresent()) {
            model.addAttribute("entity", stockIncomeBillService.get(id));
        }
        return "stockIncomeBill/billView";
    }

    @RequestMapping("saveOrUpdate")
    @ResponseBody
    public Object saveOrUpdate(StockIncomeBill entity) {
        stockIncomeBillService.saveOrUpdate(entity);
        return new JSONResult();
    }

    @RequestMapping("delete")
    @ResponseBody
    public Object delete(Long id) {
        if (Optional.ofNullable(id).isPresent()) {
            stockIncomeBillService.delete(id); //有ID
        }
        return new JSONResult(); //象征性返回
    }

    //单据的审核操作
    @RequestMapping("audit")
    @ResponseBody
    public Object audit(Long id) {
        if (Optional.ofNullable(id).isPresent()) {
            stockIncomeBillService.audit(id);
        }
        return new JSONResult();
    }
}
