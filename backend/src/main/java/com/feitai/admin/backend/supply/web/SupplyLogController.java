package com.feitai.admin.backend.supply.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.feitai.admin.backend.fund.service.FundService;
import com.feitai.admin.backend.loan.entity.LoanOrderMore;
import com.feitai.admin.backend.properties.AppProperties;
import com.feitai.admin.backend.properties.MapProperties;
import com.feitai.admin.backend.supply.entity.SupplyLogMore;
import com.feitai.admin.backend.supply.service.SupplyLogService;
import com.feitai.admin.backend.supply.service.SupplyRequirementService;
import com.feitai.admin.backend.supply.vo.LoanSupplyInfo;
import com.feitai.admin.backend.supply.vo.PicturesInfo;
import com.feitai.admin.backend.supply.vo.SupplyLog2OrderCenterRequest;
import com.feitai.admin.backend.vo.BackendResponse;
import com.feitai.admin.core.service.*;
import com.feitai.admin.core.web.BaseListableController;
import com.feitai.admin.core.web.PageBulider;
import com.feitai.admin.system.model.SupplyCountInfo;
import com.feitai.admin.system.model.SupplyDashuLog;
import com.feitai.admin.system.service.SupplyCountInfoService;
import com.feitai.jieya.server.dao.data.model.IdCardData;
import com.feitai.jieya.server.dao.fund.model.Fund;
import com.feitai.jieya.server.dao.loan.model.LoanSupplyLog;
import com.feitai.jieya.server.dao.user.model.User;
import com.feitai.utils.StringUtils;
import com.feitai.utils.datetime.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * detail:补件记录web层
 * author:longhaoteng
 * date:2018/11/19
 */
@Controller
@Slf4j
@RequestMapping(value="/backend/supply/log")
public class SupplyLogController extends BaseListableController<SupplyLogMore> {

    @Autowired
    private SupplyLogService supplyLogService;

    @Autowired
    private FundService fundService;

    @Autowired
    private SupplyCountInfoService supplyCountInfoService;

    @Autowired
    private MapProperties mapProperties;

    @Autowired
    private AppProperties appProperties;

    @Autowired
    private SupplyRequirementService supplyRequirementService;

    private final static String DATA_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private final static String PHOTE_TYPE = "JPG";

    private RestTemplate restTemplate = new RestTemplate();

    @RequiresPermissions("/backend/supply/log:list")
    @RequestMapping(value = "index")
    public String index(Model model) {
        return "backend/supply/log/index";
    }

    /***
     *  发送总部影像信息数据
     * @param supplyLogId
     * @return
     */
    @PostMapping(value = "/supplyLog2Dashu/{supplyLogId}")
    @RequiresPermissions("/backend/supply/log:supply")
    @ResponseBody
    public Object supplyLog2Dashu(@PathVariable("supplyLogId") String supplyLogId) {
        SupplyLogMore supplyLogMore = supplyLogService.findOne(supplyLogId);
        SupplyCountInfo supplyCountInfo = supplyCountInfoService.findOne(supplyLogMore.getLoanOrderId());
        //做提交次数限制
        if(supplyCountInfo.getCount()>=(appProperties.getSupply2dashu()-1)){
            return new BackendResponse(3,"提交次数超出总部限制("+appProperties.getSupply2dashu()+"),将预留一次作于提交！");
        }

        List<JSONObject> loanSupplyData = JSON.parseObject(supplyLogMore.getSupplyInfo(), List.class);
        SupplyLog2OrderCenterRequest supplyLog2OrderCenterRequest = new SupplyLog2OrderCenterRequest();
        List<PicturesInfo> picturesInfos = new ArrayList<>();
        supplyLog2OrderCenterRequest.setLoanId(supplyCountInfo.getId());
        for (JSONObject loanSupplyJson:loanSupplyData){
            LoanSupplyInfo loanSupplyInfo = JSON.parseObject(JSON.toJSONString(loanSupplyJson), LoanSupplyInfo.class);
             if(loanSupplyInfo.getSupplyType().equals("1")){
                if(loanSupplyInfo.getIfPlural().equals("1")){
                    for(String url:loanSupplyInfo.getSupplyInfos()){
                        PicturesInfo picturesInfo = new PicturesInfo();
                        picturesInfo.setContent(url);
                        picturesInfo.setEndFlag(PHOTE_TYPE);
                        String supplyTodashu = mapProperties.getSupplyTodashu(loanSupplyInfo.getSupplyCode());
                        if(StringUtils.isNotBlank(supplyTodashu)){
                            picturesInfo.setType(supplyTodashu);
                            picturesInfos.add(picturesInfo);
                        }
                    }
                }else{
                    PicturesInfo picturesInfo = new PicturesInfo();
                    picturesInfo.setContent(loanSupplyInfo.getSupplyInfo());
                    picturesInfo.setEndFlag(PHOTE_TYPE);
                    String supplyTodashu = mapProperties.getSupplyTodashu(loanSupplyInfo.getSupplyCode());
                    if(StringUtils.isNotBlank(supplyTodashu)){
                        picturesInfo.setType(supplyTodashu);
                        picturesInfos.add(picturesInfo);
                    }
                }
            }
        }
        if(picturesInfos.size()==0){
            return new BackendResponse(2,"此次补件内容没有影像信息！");
        }
        supplyLog2OrderCenterRequest.setPicList(picturesInfos);
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> request = new HttpEntity<>(JSON.toJSONString(supplyLog2OrderCenterRequest), headers);
            restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
            ResponseEntity<String> jsonString = restTemplate.postForEntity(appProperties.getSupply2OrderCenter(), request, String.class);
            JSONObject jsonObject = JSON.parseObject(jsonString.getBody());
            if(jsonObject.get("code").equals("1")){
                supplyCountInfo.setUpdateTime(new Date());
                supplyCountInfo.setCount(supplyCountInfo.getCount()+1);
                supplyCountInfoService.saveAndLog(supplyCountInfo,Long.parseLong(supplyLogId));
                return new BackendResponse(0,"成功");
            }else{
                log.error(String.format("order-center can't not send supplyLogPhotoInfo to dashu,supplyLog[id:{%s}]",supplyLogId));
                return new BackendResponse(1,"订单中心发送总部失败！");
            }
        }catch (Exception e){
            log.error(String.format("supplyLog[id:{%s}] send to order-center fail",supplyLogId),e);
            return new BackendResponse(-1,"连接订单中心失败！");
        }
    }


    @RequiresPermissions("/backend/supply/log:list")
    @RequestMapping(value = "list")
    @ResponseBody
    public Object listPage(ServletRequest request) {
        //根据request获取page
        int pageNo = PageBulider.getPageNo(request);
        int pageSize = PageBulider.getPageSize(request);
        List<SupplyLogMore> result2PageList = new ArrayList<>();
        Map<Long,SupplyLogMore> map = new LinkedHashMap<>();
        List<SupplyLogMore> resultList = getService().findAll(getCommonSqls(request,getSelectMultiTable().buildSqlString())+ " ORDER BY " + SelectMultiTable.MAIN_ALAIS + ".created_time DESC");
        Set<Long> set = new HashSet<>();
        for (SupplyLogMore supplyLogMore:resultList){
            if(set.add(supplyLogMore.getLoanOrderId())){
                map.put(supplyLogMore.getLoanOrderId(),supplyLogMore);
            }else {
                if(map.get(supplyLogMore.getLoanOrderId()).getSupplyCount()<supplyLogMore.getSupplyCount()){
                    map.put(supplyLogMore.getLoanOrderId(),supplyLogMore);
                }
            }
        }
        int size = set.size();
        int supply2dashu = appProperties.getSupply2dashu()-1;
        for (SupplyLogMore supplyLogMore:map.values()){
            LoanOrderMore loanOrder = supplyLogMore.getLoanOrder();
            if(loanOrder==null){
                size = size-1;
                log.error(String.format("These supplyLog[id:{%s}] is dirty data! ",supplyLogMore.getId()));
                continue;
            }
            Long payFundId = loanOrder.getPayFundId();
            if(payFundId!=null){
                Fund fund = fundService.findOne(payFundId);
                if(fund!=null){
                    supplyLogMore.setFundName(fund.getFundName());
                }
            }
            SupplyCountInfo supplyCountInfo = supplyCountInfoService.findOne(loanOrder.getId());
            if(supplyCountInfo!=null){
                supplyLogMore.setCan2dashu(supply2dashu-supplyCountInfo.getCount());
            }else {
                supplyLogMore.setCan2dashu(supply2dashu);
            }
            //历史补件记录
            List<LoanSupplyLog> supplyLogHistorys = supplyLogService.findByLoanOrder(supplyLogMore.getLoanOrderId());
            int supplyLogHistorysSize = supplyLogHistorys.size();
            for (LoanSupplyLog loanSupplyLog:supplyLogHistorys){
                SupplyDashuLog supplyDashuLog = supplyCountInfoService.checkSendDashuLog(loanSupplyLog.getId());
                if(supplyDashuLog !=null){
                    supplyLogHistorysSize = supplyLogHistorysSize-1;
                }
            }
            supplyLogMore.setRemain(supplyLogHistorysSize);
            result2PageList.add(supplyLogMore);
        }
        Page<SupplyLogMore> supplyLogMorePage = buildPage(result2PageList, size, pageNo, pageSize);
        return switchContent(supplyLogMorePage, supplyLogMorePage.getContent());
    }


    /***
     * 获取详细页面
     * @param id
     * @return
     */
    @RequestMapping(value = "detail/{id}", method = RequestMethod.GET)
    public ModelAndView detail(@PathVariable("id") String id) {
        ModelAndView modelAndView = new ModelAndView("/backend/supply/log/detail");

        //基本信息
        SupplyLogMore supplyLog = getService().findOne(id);
        if(ObjectUtils.isEmpty(supplyLog)){
            log.error(String.format("can't find supplyLog from loanOrder[id:{%s}]",id));
            return modelAndView;
        }
        modelAndView.addObject("supplyLog",supplyLog);
        modelAndView.addObject("createdTime",DateUtils.format(supplyLog.getCreatedTime(),DATA_FORMAT));
        LoanOrderMore loanOrder = supplyLog.getLoanOrder();
        if(loanOrder!=null){
            Long payFundId = loanOrder.getPayFundId();
            if(payFundId!=null){
                Fund fund = fundService.findOne(payFundId);
                if(fund!=null){
                    modelAndView.addObject("fundName",fund.getFundName());
                }
            }
        }
        //补件提交次数
        SupplyCountInfo supplyCountInfo = supplyCountInfoService.findOne(supplyLog.getLoanOrderId());
        if(supplyCountInfo==null){
            supplyCountInfo = new SupplyCountInfo();
            supplyCountInfo.setId(supplyLog.getLoanOrderId());
            supplyCountInfo.setCount(0);
            supplyCountInfo.setCreatedTime(new Date());
            supplyCountInfo.setUpdateTime(new Date());
            supplyCountInfoService.save(supplyCountInfo);
        }
        int supply2dashu = supplyCountInfo.getCount();
        int can2dashu = appProperties.getSupply2dashu()-1-supply2dashu;
        modelAndView.addObject("supply2dashu",supply2dashu);
        modelAndView.addObject("can2dashu",can2dashu);

        //历史补件记录
        List<LoanSupplyLog> supplyLogHistorys = supplyLogService.findByLoanOrder(supplyLog.getLoanOrderId());
        List<Map<String,Object>> historyList = new ArrayList<>();
        List<Map<String,Object>> sendHistoryList = new ArrayList<>();
        for (LoanSupplyLog loanSupplyLog:supplyLogHistorys){
            //添加原因
            List<JSONObject> list = JSON.parseObject(loanSupplyLog.getSupplyInfo(), List.class);
            List<JSONObject> listnew = new ArrayList<>();
            for (JSONObject json:list) {
                String supplyCode = (String)json.get("supplyCode");
                String reason = supplyRequirementService.findReasonByLoanOrderAndCount(loanOrder.getId(),loanSupplyLog.getSupplyCount(),supplyCode);
                json.put("reason",reason);
                listnew.add(json);
            }
            loanSupplyLog.setSupplyInfo(JSON.toJSONString(listnew));

            Map<String,Object> historyInfo = new HashMap<>();
            historyInfo.put("id",loanSupplyLog.getId());
            historyInfo.put("createdTime",DateUtils.format(loanSupplyLog.getCreatedTime(),DATA_FORMAT));
            List<Map> info = JSON.parseObject(loanSupplyLog.getSupplyInfo(), List.class);
            List<Map> handleInfo = new ArrayList<>();
            for (Map supplyLogInfo:info){
                String supplyMarterialNm = mapProperties.getSupplyMarterialNm(supplyLogInfo.get("supplyCode").toString());
                supplyLogInfo.put("supplyName",supplyMarterialNm);
                handleInfo.add(supplyLogInfo);
            }
            historyInfo.put("info", info);
            SupplyDashuLog supplyDashuLog = supplyCountInfoService.checkSendDashuLog(supplyLog.getId());
            if(supplyDashuLog !=null){
                historyInfo.put("sendTime",DateUtils.format(supplyDashuLog.getCreatedTime(),DATA_FORMAT));
                sendHistoryList.add(historyInfo);
            }else{
                historyList.add(historyInfo);
            }
        }
        modelAndView.addObject("history",historyList);
        modelAndView.addObject("sendHistoryList",sendHistoryList);
        return modelAndView;
    }


    @ModelAttribute
    public void getloanOrder(@RequestParam(value = "id", defaultValue = "-1") String id, Model model) {
        if (!id.equals("-1")) {
            model.addAttribute("supplyLog", this.supplyLogService.findOneBySql(getOneSql(id)));
        }
    }

    private String getOneSql(String id) {
        return null;
    }


    @Override
    protected DynamitSupportService<SupplyLogMore> getService() {
        return this.supplyLogService;
    }

    private SelectMultiTable getSelectMultiTable() {
        return SelectMultiTable.builder(SupplyLogMore.class)
                .leftJoin(LoanOrderMore.class, "loan_order", new OnCondition[]{
                        new OnCondition(SelectMultiTable.ConnectType.AND, "loanOrderId", Operator.EQ, "id"),
                }).leftJoin(IdCardData.class, "idcard", new OnCondition[]{
                        new OnCondition(SelectMultiTable.ConnectType.AND, "userId", Operator.EQ, "userId"),
                }).leftJoin(User.class, "user", new OnCondition[]{
                        new OnCondition(SelectMultiTable.ConnectType.AND, "userId", Operator.EQ, "id"),
                });
    }

}
