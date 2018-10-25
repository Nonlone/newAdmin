/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package com.feitai.admin.system.web;

import com.feitai.admin.core.annotation.LogAnnotation;
import com.feitai.admin.core.vo.AjaxResult;
import com.feitai.admin.system.model.User;
import com.feitai.admin.system.service.UserService;
import com.google.code.kaptcha.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * LoginController负责打开登录页面(GET请求)和登录出错页面(POST请求)，
 * <p>
 * 真正登录的POST请求由Filter完成,
 *
 * @author calvin
 */
@Controller
@Slf4j
public class LoginController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = {"login", ""}, method = RequestMethod.GET)
    public String login(Model model) {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated() || subject.isRemembered()) {
            return "redirect:/home";
        } else {
            return "/login";
        }
    }


    @RequestMapping(value = {"logout", ""}, method = RequestMethod.GET)
    public String logout(Model model) {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return "redirect:/login";
    }

    @RequestMapping(value = "loginForm", method = RequestMethod.POST)
    @ResponseBody
    @LogAnnotation(value = true, writeParams = false)
    // 写日志但是不打印请求的params,避免把密码也打印出来
    public Object loginForm(@RequestParam String username,
                            @RequestParam String password, @RequestParam String code,
                            @RequestParam(value = "rememberMe", defaultValue = "false") boolean rememberMe,
                            HttpSession session) {
        User user = userService.findByLoginName(username);
        String msg = null;
        boolean result = false;

        String sessionCode = (String) session
                .getAttribute(Constants.KAPTCHA_SESSION_KEY);

        // 判断图形验证码
        boolean codeFlag = false;
        if (StringUtils.isNotBlank(sessionCode)
                && StringUtils.isNotBlank(code)) {
            sessionCode = sessionCode.toLowerCase();
            code = code.toLowerCase();
            if (sessionCode.equals(code)) {
                codeFlag = true;
            }
        }

        if (!codeFlag) {
            msg = "验证码超时或错误";
        } else {
            AuthenticationToken token = new UsernamePasswordToken(username,
                    password, rememberMe);
            Subject currentUser = SecurityUtils.getSubject();

            try {
                currentUser.login(token);
                result = true;
                msg = "登录成功";
            } catch (UnknownAccountException uae) {
                msg = "用户不存在！";
                log.info("username wasn't in the system.");
            } catch (IncorrectCredentialsException ice) {
                msg = "用户名或密码错误！";
                log.info("password didn't match.");
            } catch (LockedAccountException lae) {
                msg = "用户为锁定状态！";
                log.info("account for that username is locked - can't login.");
            } catch (AuthenticationException ae) {
                msg = "登录失败！";
                log.info("unexpected condition." + ae.getMessage());
            }
        }
        // 只要调用了登录方法就清空SESSION中的验证码，防止暴力破解
        session.removeAttribute(Constants.KAPTCHA_SESSION_KEY);
        AjaxResult successResult = new AjaxResult(result, msg);
        return successResult;
    }

    @RequestMapping(value = "login/success")
    @ResponseBody
    public Object success() {
        AjaxResult successResult = new AjaxResult(true, "");
        return successResult;
    }

    @RequestMapping(value = "login/unlogin")
    @ResponseBody
    public Object unlogin() {
        // TODO
        AjaxResult successResult = new AjaxResult(true, "");
        return successResult;
    }

}
