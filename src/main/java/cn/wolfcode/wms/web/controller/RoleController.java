package cn.wolfcode.wms.web.controller;

import cn.wolfcode.wms.domain.Role;
import cn.wolfcode.wms.query.QueryObject;
import cn.wolfcode.wms.service.IPermissionService;
import cn.wolfcode.wms.service.IRoleService;
import cn.wolfcode.wms.service.ISystemMenuService;
import cn.wolfcode.wms.util.JSONResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Optional;

@Controller
@RequestMapping("role")
public class RoleController {
    @Autowired
    private IRoleService roleService;
    @Autowired
    private IPermissionService permissionService;
    @Autowired
    private ISystemMenuService systemMenuService;

    @RequestMapping("list")
    public String list(@ModelAttribute("qo") QueryObject qo, Model model) {
        model.addAttribute("result", roleService.query(qo));
        return "role/list";
    }

    @RequestMapping("input")
    public String input(Long id, Model model) {
        //查询所有的权限
        model.addAttribute("permissions", permissionService.list());
        model.addAttribute("menus", systemMenuService.list());
        if (Optional.ofNullable(id).isPresent()) {
            model.addAttribute("entity", roleService.get(id)); //有ID
        }
        return "role/input";
    }

    @RequestMapping("saveOrUpdate")
    @ResponseBody
    public Object saveOrUpdate(Role entity, Long[] permissionIds, Long[] menuIds) {
        roleService.saveOrUpdate(entity, permissionIds, menuIds);
        return new JSONResult();
    }

    @RequestMapping("delete")
    @ResponseBody
    public Object delete(Long id) {
        if (Optional.ofNullable(id).isPresent()) {
            roleService.delete(id); //有ID
        }
        return new JSONResult(); //象征性返回
    }
}
