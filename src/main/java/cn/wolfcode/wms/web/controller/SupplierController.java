package cn.wolfcode.wms.web.controller;

import cn.wolfcode.wms.domain.Supplier;
import cn.wolfcode.wms.query.QueryObject;
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
@RequestMapping("supplier")
public class SupplierController {
    @Autowired
    private ISupplierService supplierService;

    @RequestMapping("list")
    public String list(@ModelAttribute("qo") QueryObject qo, Model model) {
        model.addAttribute("result", supplierService.query(qo));
        return "supplier/list";
    }

    @RequestMapping("input")
    public String input(Long id, Model model) {
        if (Optional.ofNullable(id).isPresent()) {
            model.addAttribute("entity", supplierService.get(id)); //有ID
        }
        return "supplier/input";
    }

    @RequestMapping("saveOrUpdate")
    @ResponseBody
    public Object saveOrUpdate(Supplier entity) {
        supplierService.saveOrUpdate(entity);
        return new JSONResult();
    }

    @RequestMapping("delete")
    @ResponseBody
    public Object delete(Long id) {
        if (Optional.ofNullable(id).isPresent()) {
            supplierService.delete(id); //有ID
        }
        return new JSONResult(); //象征性返回
    }
}
