/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package com.feitai.admin.system.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.feitai.admin.core.annotation.LogAnnotation;
import com.feitai.admin.core.service.DynamitSupportService;
import com.feitai.admin.core.service.Page;
import com.feitai.admin.core.vo.ShiroUser;
import com.feitai.admin.core.web.BaseListableController;
import com.feitai.admin.system.model.Role;
import com.feitai.admin.system.model.User;
import com.feitai.admin.system.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
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
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/system/user")
@Slf4j
public class UserController extends BaseListableController<User> {

    @Autowired
    private UserService userService;

    @RequiresUser
    @RequestMapping(value = "")
    public String index() {
        return "/system/user/index";
    }

    /**
     * 写日志但是不打印请求的params,但不打印ResponseBody的内容
     *
     * @param request
     * @return
     */
    @RequiresPermissions("/system/user:list")
    @RequestMapping(value = "list")
    @ResponseBody
    @LogAnnotation(value = true, writeRespBody = false)
    public Object listPage(ServletRequest request) {
        Page<User> listPage = super.list(request);
        String list = JSON.toJSONString(listPage);
        List newList = new ArrayList();
        Map mapList = JSON.parseObject(list,Map.class);
        List<JSONObject> content = (List<JSONObject>)mapList.get("content");
        //遍历page中内容，修改或添加非数据库的自定义字段
        for (JSONObject json:
                content) {
            try{
                Map<String, Object> map = JSONObject.parseObject(json.toJSONString(), new TypeReference<Map<String, Object>>(){});
                Integer userId = (Integer)map.get("id");
                List<Role> roles = userService.getRoles(userId.toString());
                map.put("roles",roles);
                newList.add(map);
            }catch(Exception e){
                log.error("this json handle fail:[{}]! message:{}",json,e.getMessage());
                continue;
            }
            mapList.put("content",newList);
        }
        return mapList;
    }

    /**
     * 从数据库中获取user，如http://localhost/systemtem/update/1,则返回用户ID为1的用户
     *
     * @param id
     * @return
     */
    @RequiresPermissions("/system/user:update")
    @RequestMapping(value = "update/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Object editFrom(@PathVariable("id") Long id) {
        User user = this.userService.findOne(id);
        return user;
    }

    /**
     * 远程校验登录名是否重复
     *
     * @param loginName
     * @return
     */
    @RequiresPermissions(value = "/system/user:add")
    @RequestMapping(value = "checkLoginName")
    @ResponseBody
    public String checkLoginName(@RequestParam String loginName) {
        User user = this.userService.findByLoginName(loginName);
        if (user == null) {
            return "";
        } else {
            return "该登录名已经被使用";
        }
    }

    @RequiresPermissions(value = "/system/user:add")
    @RequestMapping(value = "add", method = RequestMethod.POST)
    @ResponseBody
    public Object add(@Valid User user,
                      @RequestParam(value = "roleIds") List<Long> roleIds) {
        for (Long roleId : roleIds) {
            userService.saveUserRole(user.getId(),roleId);
        }
        user.setCreateTime(new Date());
        User creater = new User();
        creater.setId(this.getCurrentUserId());
        user.setCreater(creater);
        this.userService.save(user);
        return successResult;
    }

    @RequiresPermissions(value = "/system/user:update")
    @RequestMapping(value = "update", method = RequestMethod.POST)
    @ResponseBody
    public Object update(@Valid @ModelAttribute("user") User user,
                         @RequestParam(value = "roleIds") List<Long> roleIds) {
        user.setPlainPassword(null);// 强制将密码设置为空，避免有人越权把密码传回来进行修改
        // 清除从数据库load出来的角色列表
        userService.clearUserRole(user.getId());
        for (Long roleId : roleIds) {
            userService.saveUserRole(user.getId(),roleId);
        }
        this.userService.save(user);
        return successResult;
    }

    @RequiresPermissions("/system/user:del")
    @RequestMapping(value = "del")
    @ResponseBody
    public Object del(@RequestParam(value = "ids[]") Long[] ids) {
        this.userService.delete(ids);
        for (Long id:ids){
            userService.clearUserRole(id);
        }
        return successResult;
    }

    @RequiresRoles("Admin")
    @RequestMapping(value = "changePasswd", method = RequestMethod.POST)
    @ResponseBody
    public Object changePassword(@Valid @ModelAttribute("user") User user) {
        this.userService.save(user);
        return successResult;
    }


    /**
     * 所有RequestMapping方法调用前的Model准备方法, 实现Struts2
     * Preparable二次部分绑定的效果,先根据form的id从数据库查出User对象,再把Form提交的内容绑定到该对象上。
     * 因为仅update()方法的form中有id属性，因此仅在update时实际执行.
     */
    @ModelAttribute
    public void getUser(
            @RequestParam(value = "id", defaultValue = "-1") Long id,
            Model model) {
        if (id != -1) {
            model.addAttribute("user", this.userService.findOne(id));
        }
    }

    /**
     * 取出Shiro中的当前用户Id.
     */
    protected Long getCurrentUserId() {
        ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        return user.getId();
    }

    @Override
    protected DynamitSupportService<User> getService() {
        return this.userService;
    }


    @InitBinder
    public void initDate(WebDataBinder webDataBinder) {
        webDataBinder.addCustomFormatter(new DateFormatter("yyyy-MM-dd HH:mm:ss"));
        webDataBinder.addCustomFormatter(new DateFormatter("yyyy-MM-dd"));
    }

}
