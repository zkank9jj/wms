package cn.wolfcode.wms.web.controller;

import cn.wolfcode.wms.domain.Depot;
import cn.wolfcode.wms.query.QueryObject;
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
@RequestMapping("depot")
public class DepotController {
    @Autowired
    private IDepotService depotService;

    @RequestMapping("list")
    public String list(@ModelAttribute("qo") QueryObject qo, Model model) {
        model.addAttribute("result", depotService.query(qo));
        return "depot/list";
    }

    @RequestMapping("input")
    public String input(Long id, Model model) {
        if (Optional.ofNullable(id).isPresent()) {
            model.addAttribute("entity", depotService.get(id)); //有ID
        }
        return "depot/input";
    }

    @RequestMapping("saveOrUpdate")
    @ResponseBody
    public Object saveOrUpdate(Depot entity) {
        depotService.saveOrUpdate(entity);
        return new JSONResult();
    }

    @RequestMapping("delete")
    @ResponseBody
    public Object delete(Long id) {
        if (Optional.ofNullable(id).isPresent()) {
            depotService.delete(id); //有ID
        }
        return new JSONResult(); //象征性返回
    }
}
