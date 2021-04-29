package cn.wolfcode.wms.web.controller;

import cn.wolfcode.wms.domain.Brand;
import cn.wolfcode.wms.query.QueryObject;
import cn.wolfcode.wms.service.IBrandService;
import cn.wolfcode.wms.util.JSONResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Optional;

@Controller
@RequestMapping("brand")
public class BrandController {
    @Autowired
    private IBrandService brandService;

    @RequestMapping("list")
    public String list(@ModelAttribute("qo") QueryObject qo, Model model) {
        model.addAttribute("result", brandService.query(qo));
        return "brand/list";
    }

    @RequestMapping("input")
    public String input(Long id, Model model) {
        if (Optional.ofNullable(id).isPresent()) {
            model.addAttribute("entity", brandService.get(id)); //有ID
        }
        return "brand/input";
    }

    @RequestMapping("saveOrUpdate")
    @ResponseBody
    public Object saveOrUpdate(Brand entity) {
        brandService.saveOrUpdate(entity);
        return new JSONResult();
    }

    @RequestMapping("delete")
    @ResponseBody
    public Object delete(Long id) {
        if (Optional.ofNullable(id).isPresent()) {
            brandService.delete(id); //有ID
        }
        return new JSONResult(); //象征性返回
    }
}
