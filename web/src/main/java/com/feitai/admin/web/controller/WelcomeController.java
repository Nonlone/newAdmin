package com.feitai.admin.web.controller;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/welcome")
@Slf4j
public class WelcomeController {


    @RequestMapping("/index")
    public ModelAndView index(HttpServletRequest request) {
        log.debug(JSON.toJSONString(request.getParameterMap()));
        ModelAndView modelAndView = new ModelAndView("/welcome/index");
        modelAndView.addObject("value", RandomStringUtils.randomAlphanumeric(20));
        return modelAndView;
    }


}
