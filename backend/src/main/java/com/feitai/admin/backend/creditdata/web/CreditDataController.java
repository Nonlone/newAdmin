package com.feitai.admin.backend.creditdata.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
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
    
    /**
     * 返回烟草授信数据最新记录数据
     */
    private final static int NEW_ORDER_COUNT=10;


    @PostMapping("/moxie")
    @ResponseBody
    public Object moxie(@RequestParam("userId") Long userId,@RequestParam("cardId")Long cardId){
        MoxieData moxieData = moxieDataService.findByUserIdAndCardId(userId,cardId);
        if(!Objects.isNull(moxieData)){
            return new ResponseBean<>(ResultCode.SUCCESS,moxieData);
        }
        return new ResponseBean<Void>(ResultCode.FAIL);
    }


    @PostMapping("/sauron")
    @ResponseBody
    public Object sauron(@RequestParam("userId") Long userId,@RequestParam("cardId")Long cardId){
        CreditData creditData = creditDataService.findByCardIdAndUserIdAndSource(userId,cardId, "DAIHOUBANG");
        if(!Objects.isNull(creditData)){
            JSONObject json = (JSONObject) JSON.toJSON(creditData);
            json.put("report",JSON.parse(creditData.getCreditData()));
            return new ResponseBean<>(ResultCode.SUCCESS,json);
        }
        return new ResponseBean<Void>(ResultCode.FAIL);
    }


    @PostMapping("/suanhua")
    @ResponseBody Object suanhua(@RequestParam("userId") Long userId,@RequestParam("cardId")Long cardId){
        CreditData creditData = creditDataService.findByUserIdAndSource(userId, "SUANHUA");
        if(!Objects.isNull(creditData)){
            JSONObject json = (JSONObject) JSON.toJSON(creditData);
            json.put("report",JSON.parse(creditData.getCreditData()));
            return new ResponseBean<>(ResultCode.SUCCESS,json);
        }
        return new ResponseBean<Void>(ResultCode.FAIL);
    }
    /**
     * 获取自建烟草授信数据
     * @param userId
     * @return
     */
    @PostMapping("/tobacco")
    @ResponseBody
    public Object tobacco(@RequestParam("userId") Long userId){
    	JSONObject json=handleOrders(userId,"TOBACCO");
        return json!=null?new ResponseBean<>(ResultCode.SUCCESS,json):new ResponseBean<Void>(ResultCode.FAIL);
    }
    /**
     * 获取新云联烟草授信数据
     * @param userId
     * @return
     */
    @PostMapping("/xinyunlian")
    @ResponseBody
    public Object xinyunlian(@RequestParam("userId") Long userId){
    	JSONObject json=handleOrders(userId,"XYL_TOBACCO");
         return json!=null?new ResponseBean<>(ResultCode.SUCCESS,json):new ResponseBean<Void>(ResultCode.FAIL);
    }


	private JSONObject handleOrders(Long userId,String code) {
		CreditData creditData = creditDataService.findByUserIdAndCode(userId, code);
		JSONObject json =null;
         if(!Objects.isNull(creditData)){
             json = (JSONObject) JSON.toJSON(creditData);
             JSONObject creditDataReport=(JSONObject) JSON.parse(creditData.getCreditData());
             if(creditDataReport.getJSONObject("data")!=null){
            	 creditDataReport=creditDataReport.getJSONObject("data");
             }
             JSONArray orderArray=creditDataReport.getJSONArray("orders");
             if(orderArray==null){
            	 return null;
             }
             JSONArray newOrderArray=new JSONArray();
             for(int i=0;i<NEW_ORDER_COUNT && i<orderArray.size();i++){
            	 JSONObject newOrder=(JSONObject) orderArray.get(i);
            	 newOrder.remove("orderDetail");
            	 newOrderArray.add(newOrder);
             }
             JSONObject orders=new JSONObject();
             orders.put("orders", newOrderArray);
             json.put("report", orders);          
         }
         return json;
	}

}
