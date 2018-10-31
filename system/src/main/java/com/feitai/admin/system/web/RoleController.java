package com.feitai.admin.system.web;

import com.alibaba.fastjson.JSON;
import com.feitai.admin.core.annotation.LogAnnotation;
import com.feitai.admin.core.service.*;
import com.feitai.admin.core.vo.ListItem;
import com.feitai.admin.core.web.BaseListableController;
import com.feitai.admin.system.model.Role;
import com.feitai.admin.system.model.RoleAuth;
import com.feitai.admin.system.service.RoleAuthService;
import com.feitai.admin.system.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/system/role")
@Slf4j
public class RoleController extends BaseListableController<Role> {

    @Autowired
    private RoleService roleService;
    @Autowired
    private RoleAuthService roleAuthService;

    @RequiresUser
    @RequestMapping(value = "")
    public String index() {
        return "/system/role/index";
    }

    @RequestMapping(value = "auth/{id}", method = RequestMethod.GET)
    @RequiresPermissions("/system/role:auth")
    public String auth(@PathVariable("id") Long id, Model model) {
        model.addAttribute("id", id);
        return "/system/role/auth";
    }

    @RequestMapping(value = "auth/{id}", method = RequestMethod.POST)
    @RequiresPermissions("/system/role:auth")
    @ResponseBody
    public Object auth(@PathVariable("id") Long id, @RequestParam(value = "jsonRole") String jsonRole) {
        Role role = JSON.parseObject(jsonRole, Role.class);
        this.roleService.authRole(role);
        return successResult;
    }

    @RequiresPermissions("/system/role:list")
    @RequestMapping(value = "list")
    @ResponseBody
    @LogAnnotation(value = true, writeRespBody = false)// 写日志但是不打印请求的params,但不打印ResponseBody的内容
    public Object listPage(ServletRequest request) {
        Page<Role> listPage = super.list(request, getSql());
        return listPage;
    }

    @RequiresPermissions("/system/role:add")
    @RequestMapping(value = "add", method = RequestMethod.POST)
    @ResponseBody
    public Object add(@Valid Role role) {
        this.roleService.save(role);
        return successResult;
    }

    // 特别设定多个ReuireRoles之间为Or关系，而不是默认的And.
    @RequiresPermissions(value = {"/system/role:update", "/system/role:auth"}, logical = Logical.OR)
    @RequestMapping(value = "update/{id}", method = RequestMethod.GET)
    @ResponseBody
    @LogAnnotation(value = true, writeRespBody = false)// 写日志但是不打印请求的params,但不打印ResponseBody的内容
    public Object editFrom(@PathVariable("id") Long id) {
        Role role = this.roleService.findOne(id);
        return role;
    }

    @RequiresPermissions("/system/role:update")
    @RequestMapping(value = "update", method = RequestMethod.POST)
    @ResponseBody
    public Object update(@Valid @ModelAttribute("role") Role role) {
        this.roleService.save(role);
        return successResult;
    }

    @RequiresPermissions("/system/role:del")
    @RequestMapping(value = "del")
    @ResponseBody
    public Object del(@RequestParam(value = "ids[]") Long[] ids) {
        this.roleService.delete(ids);
        return successResult;
    }

    @RequiresPermissions(value = {"/system/user:update", "/system/user:add"}, logical = Logical.OR)
    @RequestMapping(value = "roleCheckList")
    @ResponseBody
    @LogAnnotation(value = true, writeRespBody = false)// 写日志但是不打印请求的params,但不打印ResponseBody的内容
    public Object roleCheckList() {
        List<Role> roles = this.roleService.findAll();
        List<ListItem> list = new ArrayList<ListItem>();
        for (Role role : roles) {
            list.add(new ListItem(role.getName(), role.getId().toString()));
        }
        return list;
    }

    /**
     * 所有RequestMapping方法调用前的Model准备方法, 实现Struts2
     * Preparable二次部分绑定的效果,先根据form的id从数据库查出User对象,再把Form提交的内容绑定到该对象上。
     * 因为仅update()方法的form中有id属性，因此仅在update时实际执行.
     */
    @ModelAttribute
    public void getRole(@RequestParam(value = "id", defaultValue = "-1") Long id, Model model) {
        if (id != -1) {
            model.addAttribute("role", this.roleService.findOne(id));
        }
    }

    @Override
    protected DynamitSupportService<Role> getService() {
        return this.roleService;
    }

    protected String getSql() {
        String sql = SelectMultiTable.builder(Role.class)
                .leftJoin(RoleAuth.class, "roleAuth", new OnCondition[]{
                        new OnCondition(SelectMultiTable.ConnectType.AND, "id", Operator.EQ, "roleId"),
                }).buildSqlString();
        return sql;
    }


    @InitBinder
    public void initDate(WebDataBinder webDataBinder) {
        webDataBinder.addCustomFormatter(new DateFormatter("yyyy-MM-dd HH:mm:ss"));
        webDataBinder.addCustomFormatter(new DateFormatter("yyyy-MM-dd"));
    }
}
