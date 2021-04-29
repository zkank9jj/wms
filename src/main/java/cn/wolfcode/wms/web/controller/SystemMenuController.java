package cn.wolfcode.wms.web.controller;

import cn.wolfcode.wms.domain.SystemMenu;
import cn.wolfcode.wms.query.QueryObject;
import cn.wolfcode.wms.query.SystemMenuQueryObject;
import cn.wolfcode.wms.service.ISystemMenuService;
import cn.wolfcode.wms.util.JSONResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("systemMenu")
public class SystemMenuController {
    @Autowired
    private ISystemMenuService systemMenuService;

    @RequestMapping("getMenusBySn")
    @ResponseBody
    public Object getMenusBySn(String menuSn) {
        //[{id:xx, pId:xx, name: "部门管理", action: "department/list"},...]
        return systemMenuService.getMenusBySn(menuSn);
    }

    @RequestMapping("list")
    public String list(@ModelAttribute("qo") SystemMenuQueryObject qo, Model model) {
        //如果不是顶级菜单,再查询出面包屑菜单
        if (Optional.ofNullable(qo.getParentId()).isPresent()) {
            List<SystemMenu> parentMenus = systemMenuService.getParentMenus(qo.getParentId());
            model.addAttribute("parentMenus", parentMenus);
        }
        model.addAttribute("result", systemMenuService.query(qo));
        return "systemMenu/list";
    }

    @RequestMapping("input")
    public String input(Long id, Long parentId, Model model) {
        if (Optional.ofNullable(parentId).isPresent()) {
            //查询当前父菜单
            model.addAttribute("parent", systemMenuService.get(parentId));
        }
        if (Optional.ofNullable(id).isPresent()) {
            model.addAttribute("entity", systemMenuService.get(id)); //有ID
        }
        return "systemMenu/input";
    }

    @RequestMapping("saveOrUpdate")
    @ResponseBody
    public Object saveOrUpdate(SystemMenu entity) {
        systemMenuService.saveOrUpdate(entity);
        return new JSONResult();
    }

    @RequestMapping("delete")
    @ResponseBody
    public Object delete(Long id) {
        if (Optional.ofNullable(id).isPresent()) {
            systemMenuService.delete(id); //有ID
        }
        return new JSONResult(); //象征性返回
    }
}
