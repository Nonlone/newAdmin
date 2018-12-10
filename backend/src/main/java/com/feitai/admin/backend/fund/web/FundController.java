/**
 * @author
 * @version 1.0
 * @desc Funs
 * @since 2018-08-06 11:46:54
 */

package com.feitai.admin.backend.fund.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.feitai.admin.backend.fund.service.FundAmountDetailService;
import com.feitai.admin.backend.fund.service.FundService;
import com.feitai.admin.backend.fund.vo.FundChargeRequest;
import com.feitai.admin.backend.fund.vo.FundDetailRequest;
import com.feitai.admin.backend.fund.vo.FundRequest;
import com.feitai.admin.backend.properties.AppProperties;
import com.feitai.admin.core.annotation.LogAnnotation;
import com.feitai.admin.core.service.DynamitSupportService;
import com.feitai.admin.core.service.Page;
import com.feitai.admin.core.vo.ListItem;
import com.feitai.admin.core.vo.ShiroUser;
import com.feitai.admin.core.web.BaseListableController;
import com.feitai.admin.system.model.User;
import com.feitai.admin.system.service.UserService;
import com.feitai.jieya.server.dao.fund.model.Fund;
import com.feitai.jieya.server.dao.fund.model.FundAmountDetail;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletRequest;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Controller
@RequestMapping(value = "/backend/fund")
@Slf4j
public class FundController extends BaseListableController<Fund> {

    @Autowired
    private AppProperties appProperties;

    @Autowired
    private FundService fundService;

    @Autowired
    private UserService userService;

    @Autowired
    private FundAmountDetailService fundAmountDetailService;

    private RestTemplate restTemplate = new RestTemplate();


    @RequestMapping(value = "getFundList")
    @ResponseBody
    @LogAnnotation(value = true, writeRespBody = false)// 写日志但是不打印请求的params,但不打印ResponseBody的内容
    public Object getFundList(){
        List<Fund> funds = fundService.findAll();
        List<ListItem> list = new ArrayList<ListItem>();
        list.add(new ListItem("全部"," "));
        for(Fund fund:funds){
            list.add(new ListItem(fund.getFundName(), fund.getId().toString()));
        }
        return list;
    }


    @RequestMapping(value = "detail", method = RequestMethod.GET)
    public ModelAndView detail(@Valid FundDetailRequest fundDetailRequest) {
        ModelAndView modelAndView = new ModelAndView("/backend/fund/detail");
        if (StringUtils.isNotBlank(fundDetailRequest.getType())) {
            Page<FundAmountDetail> fundAmountDetails = buildPage(fundAmountDetailService.queryFundChargeByFundId(fundDetailRequest.getPage(), fundDetailRequest.getSize(), fundDetailRequest.getFundId(), new Byte(fundDetailRequest.getType())), fundDetailRequest.getPage(), fundDetailRequest.getSize());
            modelAndView.addObject("fundAmountDetails", fundAmountDetails);
        } else {
            Page<FundAmountDetail> fundAmountDetails = buildPage(fundAmountDetailService.queryFundChargeByFundId(fundDetailRequest.getPage(), fundDetailRequest.getSize(), fundDetailRequest.getFundId()), fundDetailRequest.getPage(), fundDetailRequest.getSize());
            modelAndView.addObject("fundAmountDetails", fundAmountDetails);
        }
        modelAndView.addObject("fundId", fundDetailRequest.getFundId());
        modelAndView.addObject("type", fundDetailRequest.getType());
        return modelAndView;
    }


    @RequiresPermissions(value = {"/backend/fund:update", "/backend/fund:add"}, logical = Logical.OR)
    @RequestMapping(value = "items")
    @ResponseBody
    @LogAnnotation(value = true, writeRespBody = false)// 写日志但是不打印请求的params,但不打印ResponseBody的内容
    public List<ListItem> items() {
        List<Fund> fundList = fundService.findAll();
        List<ListItem> list = new ArrayList<ListItem>();
        for (Fund fund : fundList) {
            // 只展示启动资方
            if (fund.getEnable() == 1) {
                list.add(new ListItem(fund.getFundName(), fund.getId().toString()));
            }
        }
        return list;
    }


    @RequestMapping(value = "index")
    public String index() {
        return "/backend/fund/index";
    }

    @RequiresPermissions("/backend/fund:list")
    @RequestMapping(value = "list")
    @ResponseBody
    public Object listPage(ServletRequest request) {
        return super.list(request);
    }

    @RequiresPermissions("/backend/fund:update")
    @RequestMapping(value = "update/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Object editFrom(@PathVariable("id") Long id) {
        Fund fund = fundService.findOne(id);
        return fund;
    }

    @RequiresPermissions("/backend/fund:add")
    @RequestMapping(value = "add", method = RequestMethod.POST)
    @ResponseBody
    public Object add(@Valid FundRequest fundRequest) {
        Fund fund = new Fund();
        BeanUtils.copyProperties(fundRequest, fund);
        fund.setBalance(BigDecimal.ZERO);
        fund.setTotalAmount(BigDecimal.ZERO);
        fund.setFrozenAmount(BigDecimal.ZERO);
        fund.setUsedAmount(BigDecimal.ZERO);
        fund.setCreatedTime(new Date());
        fund.setUpdateTime(new Date());
        fundService.save(fund);
        return BaseListableController.successResult;
    }

    @RequiresPermissions("/backend/fund:update")
    @RequestMapping(value = "update", method = RequestMethod.POST)
    @ResponseBody
    public Object update(@Valid FundRequest fundRequest) {
        if (fundRequest.getId() != null) {
            Fund fund = fundService.findOne(fundRequest.getId());
            if (fund != null) {
                BeanUtils.copyProperties(fundRequest, fund);
                fund.setUpdateTime(new Date());
                fundService.save(fund);
                return BaseListableController.successResult;
            }
        }
        return BaseListableController.failResult;
    }

    @RequiresPermissions("/backend/fund:update")
    @RequestMapping(value = "disable")
    @ResponseBody
    public Object disable(@RequestParam(value = "id") Long id) {
        Fund fund = fundService.findOne(id);
        if (fund != null) {
            fund.setEnable(new Byte("0"));
            fund.setUpdateTime(new Date());
            fundService.save(fund);
            return BaseListableController.successResult;
        }
        return BaseListableController.failResult;
    }

    @RequiresPermissions("/backend/fund:update")
    @RequestMapping(value = "enable")
    @ResponseBody
    public Object enable(@RequestParam(value = "id") Long id) {
        Fund fund = fundService.findOne(id);
        if (fund != null) {
            fund.setEnable(new Byte("1"));
            fund.setUpdateTime(new Date());
            fundService.save(fund);
            return BaseListableController.successResult;
        }
        return BaseListableController.failResult;
    }

    @RequestMapping(value = "addCharge", method = RequestMethod.POST)
    @ResponseBody
    public Object addCharge(@Valid FundChargeRequest fundChargeRequest) {
        ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        User user = userService.findOne(shiroUser.getId());
        fundChargeRequest.setOperator(user.getName());

        String jsonStr = JSON.toJSONString(fundChargeRequest);
        JSONObject data = JSONObject.parseObject(jsonStr);
        JSONObject object = new JSONObject();
        object.put("data", data);
        log.info("[addCharge's FundChargeRequest]FundChargeRequest:{}", object.toJSONString());
        //RestTemplate中文乱码解决方案
        restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        ResponseEntity<String> jsonString = restTemplate.postForEntity(appProperties.getFundCharge(), object.toJSONString(), String.class);
        if (StringUtils.isNotEmpty(jsonString.getBody())) {
            JSONObject result = JSONObject.parseObject(jsonString.getBody());
            if ("0".equals(result.getString("code"))) {
                log.info("[api-server response result success message] result:{}", result.toJSONString());
                return BaseListableController.successResult;
            } else {
                log.info("[api-server response result fail message] result:{}", result.toJSONString());
//               AjaxResult failMessage = new AjaxResult(false,result.getString("message"));
                return BaseListableController.failResult;
            }
        }
        log.info("[Can't connect api-server]");
        return BaseListableController.failResult;
    }

    @Override
    protected DynamitSupportService<Fund> getService() {
        return this.fundService;
    }

}
