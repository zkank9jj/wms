package cn.wolfcode.wms.web.controller;

import cn.wolfcode.wms.domain.Employee;
import cn.wolfcode.wms.query.EmployeeQueryObject;
import cn.wolfcode.wms.query.QueryObject;
import cn.wolfcode.wms.service.IDepartmentService;
import cn.wolfcode.wms.service.IEmployeeService;
import cn.wolfcode.wms.service.IRoleService;
import cn.wolfcode.wms.util.JSONResult;
import cn.wolfcode.wms.util.RequiredPermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.Optional;

@Controller
@RequestMapping("employee")
public class EmployeeController {
    @Autowired
    private IEmployeeService employeeService;
    @Autowired
    private IDepartmentService departmentService;
    @Autowired
    private IRoleService roleService;

    @RequiredPermission("员工列表")
    @RequestMapping("list")
    public String list(@ModelAttribute("qo") EmployeeQueryObject qo, Model model) {
        model.addAttribute("depts", departmentService.list());
        model.addAttribute("result", employeeService.query(qo));
        return "employee/list";
    }

    @RequiredPermission("编辑员工")
    @RequestMapping("input")
    public String input(Long id, Model model) {
        //查询所有的部门对象,存入模型中
        model.addAttribute("depts", departmentService.list());
        //查询所有的角色对象,存入模型中
        model.addAttribute("roles", roleService.list());
        if (Optional.ofNullable(id).isPresent()) {
            model.addAttribute("entity", employeeService.get(id)); //有ID
        }
        return "employee/input";
    }

    @RequiredPermission("保存/更新员工")
    @RequestMapping("saveOrUpdate")
    @ResponseBody
    public Object saveOrUpdate(Employee entity, Long[] roleIds) {
        employeeService.saveOrUpdate(entity, roleIds);
        return new JSONResult();
    }

    @RequiredPermission("删除员工")
    @RequestMapping("delete")
    @ResponseBody
    public Object delete(Long id) {
        if (Optional.ofNullable(id).isPresent()) {
            employeeService.delete(id); //有ID
        }
        return new JSONResult();
    }

    @RequestMapping("batchDelete")
    @ResponseBody
    public Object batchDelete(Long[] ids) {
        employeeService.batchDelete(ids);
        return new JSONResult();
    }
}
