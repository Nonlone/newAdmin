package com.feitai.admin.backend.supply.web;

import com.alibaba.fastjson.JSON;
import com.feitai.admin.backend.fund.service.FundService;
import com.feitai.admin.backend.loan.entity.LoanOrderMore;
import com.feitai.admin.backend.properties.MapProperties;
import com.feitai.admin.backend.supply.entity.SupplyLogMore;
import com.feitai.admin.backend.supply.service.SupplyLogService;
import com.feitai.admin.core.service.*;
import com.feitai.admin.core.web.BaseListableController;
import com.feitai.admin.core.web.PageBulider;
import com.feitai.jieya.server.dao.data.model.IdCardData;
import com.feitai.jieya.server.dao.fund.model.Fund;
import com.feitai.jieya.server.dao.loan.model.LoanSupplyLog;
import com.feitai.jieya.server.dao.user.model.User;
import com.feitai.utils.datetime.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletRequest;
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
    private MapProperties mapProperties;

    private final static String DATA_FORMAT = "yyyy-MM-dd HH:mm:ss";

    @RequestMapping(value = "")
    public String index(Model model) {
        return "backend/supply/log/index";
    }


    @RequiresPermissions("/backend/supply/log:list")
    @RequestMapping(value = "list")
    @ResponseBody
    public Object listPage(ServletRequest request) {
        //根据request获取page
        int pageNo = PageBulider.getPageNo(request);
        int pageSize = PageBulider.getPageSize(request);
        List<SupplyLogMore> result2PageList = new ArrayList<>();
        Map<Long,SupplyLogMore> map = new HashMap<>();
        List<SupplyLogMore> resultList = getService().findAllBySqls(getCommonSqls(request,getSelectMultiTable().buildSqlString()), pageNo, pageSize);
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

        //历史补件记录
        List<LoanSupplyLog> supplyLogHistorys = supplyLogService.findByLoanOrder(supplyLog.getLoanOrderId());
        List<Map<String,Object>> historyList = new ArrayList<>();
        for (LoanSupplyLog loanSupplyLog:supplyLogHistorys){
            Map<String,Object> historyInfo = new HashMap<>();
            historyInfo.put("createdTime",DateUtils.format(loanSupplyLog.getCreatedTime(),DATA_FORMAT));
            List<Map> info = JSON.parseObject(loanSupplyLog.getSupplyInfo(), List.class);
            List<Map> handleInfo = new ArrayList<>();
            for (Map supplyLogInfo:info){
                String supplyMarterialNm = mapProperties.getSupplyMarterialNm(supplyLogInfo.get("supplyCode").toString());
                supplyLogInfo.put("supplyName",supplyMarterialNm);
                handleInfo.add(supplyLogInfo);
            }
            historyInfo.put("info", info);
            historyList.add(historyInfo);
        }
        modelAndView.addObject("history",historyList);

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
