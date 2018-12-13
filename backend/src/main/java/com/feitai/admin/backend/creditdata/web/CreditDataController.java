package com.feitai.admin.backend.creditdata.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.feitai.admin.backend.creditdata.model.CreditData;
import com.feitai.admin.backend.creditdata.service.CreditDataService;
import com.feitai.admin.backend.creditdata.service.MoxieDataService;
import com.feitai.admin.core.contants.ResultCode;
import com.feitai.admin.core.vo.ResponseBean;
import com.feitai.jieya.server.dao.callback.model.moxie.MoxieData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Objects;

/**
 * detail:渲染模板获取数据
 * author:longhaoteng
 * date:2018/8/28
 */
@Controller
@RequestMapping(value = "/backend/creditdata/")
public class CreditDataController {

    @Autowired
    private CreditDataService creditDataService;

    @Autowired
    private MoxieDataService moxieDataService;


    @PostMapping("/moxie")
    @ResponseBody
    public Object moxie(@RequestParam("userId") Long userId){
        MoxieData moxieData = moxieDataService.findByUserId(userId);
        if(!Objects.isNull(moxieData)){
            return new ResponseBean<>(ResultCode.SUCCESS,moxieData);
        }
        return new ResponseBean<Void>(ResultCode.FAIL);
    }


    @PostMapping("/sauron")
    @ResponseBody
    public Object sauron(@RequestParam("userId") Long userId){
        CreditData creditData = creditDataService.findByUserIdAndSource(userId, "DAIHOUBANG");
        if(!Objects.isNull(creditData)){
            JSONObject json = (JSONObject) JSON.toJSON(creditData);
            json.put("report",JSON.parse(creditData.getCreditData()));
            return new ResponseBean<>(ResultCode.SUCCESS,json);
        }
        return new ResponseBean<Void>(ResultCode.FAIL);
    }


    @PostMapping("/suanhua")
    @ResponseBody Object suanhua(@RequestParam("userId") Long userId){
        CreditData creditData = creditDataService.findByUserIdAndSource(userId, "SUANHUA");
        if(!Objects.isNull(creditData)){
            JSONObject json = (JSONObject) JSON.toJSON(creditData);
            json.put("report",JSON.parse(creditData.getCreditData()));
            return new ResponseBean<>(ResultCode.SUCCESS,json);
        }
        return new ResponseBean<Void>(ResultCode.FAIL);
    }

}
