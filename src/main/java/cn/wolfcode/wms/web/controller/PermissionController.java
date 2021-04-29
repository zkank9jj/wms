package cn.wolfcode.wms.web.controller;

import cn.wolfcode.wms.query.QueryObject;
import cn.wolfcode.wms.service.IPermissionService;
import cn.wolfcode.wms.util.JSONResult;
import cn.wolfcode.wms.util.RequiredPermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Optional;

@Controller
@RequestMapping("permission")
public class PermissionController {
    @Autowired
    private IPermissionService permissionService;

    @RequiredPermission("权限列表")
    @RequestMapping("list")
    public String list(@ModelAttribute("qo") QueryObject qo, Model model) {
        model.addAttribute("result", permissionService.query(qo));
        return "permission/list";
    }

    @RequestMapping("reload")
    @ResponseBody
    public Object reload() {
        permissionService.reload();
        return new JSONResult();
    }

    @RequestMapping("delete")
    @ResponseBody
    public Object delete(Long id) {
        if (Optional.ofNullable(id).isPresent()) {
            permissionService.delete(id); //有ID
        }
        return new JSONResult(); //象征性返回
    }
}
