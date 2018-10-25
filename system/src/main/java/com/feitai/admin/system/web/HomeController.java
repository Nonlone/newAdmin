/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package com.feitai.admin.system.web;

import com.feitai.admin.core.annotation.LogAnnotation;
import com.feitai.admin.core.vo.AjaxResult;
import com.feitai.admin.core.vo.ShiroUser;
import com.feitai.admin.system.model.User;
import com.feitai.admin.system.service.UserService;
import com.feitai.admin.system.vo.Menu;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Collection;

/**
 * LoginController负责打开登录页面(GET请求)和登录出错页面(POST请求)，
 * <p>
 * 真正登录的POST请求由Filter完成,
 *
 * @author calvin
 */
@Controller
@RequestMapping(value = "/home")
@Slf4j
public class HomeController {

    @Autowired
    private UserService userService;

    @RequiresUser
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index() {
        return "/home";
    }

    @RequiresUser
    @RequestMapping(value = "loadMenu", method = RequestMethod.GET)
    @ResponseBody
    @LogAnnotation(value = true, writeRespBody = false)
    // 写日志但是不打印请求的params,但不打印ResponseBody的内容
    public Collection<Menu> loadMenu(ServletRequest request) {
        HttpServletRequest r = (HttpServletRequest) request;
        String ctx = r.getContextPath();
        Collection<Menu> menu = this.userService.loadMenu(
                this.getCurrentUserId(), ctx);
        return menu;
    }

    @RequiresUser
    @RequestMapping(value = "changePasswd", method = RequestMethod.POST)
    @ResponseBody
    @LogAnnotation(value = true, writeParams = false)// 写日志但是不打印请求的params,但不打印Params的内容
    public Object changePasswd(
            @RequestParam(value = "oldPassword") String oldPassword,
            @RequestParam(value = "plainPassword") String plainPassword) {
        Long userId = this.getCurrentUserId();
        AjaxResult successResult = new AjaxResult(false, null);
        if (this.userService.checkPassword(userId, oldPassword)) {
            User user = this.userService.findOne(userId);
            user.setPlainPassword(plainPassword);
            this.userService.save(user);
            successResult.setSuccess(true);
        } else {
            successResult.setMsg("原密码错误！");
        }
        return successResult;
    }

    /**
     * 取出Shiro中的当前用户Id.
     */
    private Long getCurrentUserId() {
        ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        return user.getId();
    }

}
