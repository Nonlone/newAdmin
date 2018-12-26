package com.feitai.admin.backend.supply.web;

import com.alibaba.fastjson.JSON;
import com.feitai.admin.backend.fund.service.FundService;
import com.feitai.admin.backend.loan.entity.LoanOrderMore;
import com.feitai.admin.backend.properties.MapProperties;
import com.feitai.admin.backend.supply.entity.SupplyRequirementMore;
import com.feitai.admin.backend.supply.service.SupplyLogService;
import com.feitai.admin.backend.supply.service.SupplyRequirementService;
import com.feitai.admin.core.service.*;
import com.feitai.admin.core.web.BaseListableController;
import com.feitai.admin.core.web.PageBulider;
import com.feitai.jieya.server.dao.data.model.IdCardData;
import com.feitai.jieya.server.dao.fund.model.Fund;
import com.feitai.jieya.server.dao.loan.model.LoanOrder;
import com.feitai.jieya.server.dao.loan.model.LoanSupplyLog;
import com.feitai.jieya.server.dao.user.model.User;
import com.feitai.utils.datetime.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletRequest;
import java.util.*;

/**
 * detail:补件需求web层
 * author:longhaoteng
 * date:2018/11/19
 */
@Controller
@RequestMapping(value = "/backend/supply/requirement")
@Slf4j
public class SupplyRequirementController extends BaseListableController<SupplyRequirementMore> {

    @Autowired
    private SupplyRequirementService supplyRequirementService;

    @Autowired
    private SupplyLogService supplyLogService;

    @Autowired
    private FundService fundService;

    @Autowired
    private MapProperties mapProperties;

    private final static String DATA_FORMAT = "yyyy-MM-dd HH:mm:ss";


    @RequestMapping(value = "index")
    public String index(Model model) {
        return "backend/supply/requirement/index";
    }


    @RequiresPermissions("/backend/supply/requirement:list")
    @RequestMapping(value = "list")
    @ResponseBody
    public Object listPage(ServletRequest request) {
        //根据request获取page
        int pageNo = PageBulider.getPageNo(request);
        int pageSize = PageBulider.getPageSize(request);
        List<SupplyRequirementMore> result2PageList = new ArrayList<>();
        Map<Long,SupplyRequirementMore> map = new HashMap<>();
        List<SupplyRequirementMore> resultList = getService().findAll(getCommonSqls(request,getSelectMultiTable().buildSqlString())+ " ORDER BY " + SelectMultiTable.MAIN_ALAIS + ".created_time DESC");
        Set<Long> set = new HashSet<>();
        for (SupplyRequirementMore supplyRequirementMore:resultList){
            if(set.add(supplyRequirementMore.getLoanOrderId())){
                map.put(supplyRequirementMore.getLoanOrderId(),supplyRequirementMore);
            }else {
                if(map.get(supplyRequirementMore.getLoanOrderId()).getSupplyCount()<supplyRequirementMore.getSupplyCount()){
                    map.put(supplyRequirementMore.getLoanOrderId(),supplyRequirementMore);
                }
            }
        }
        int size = set.size();
        for (SupplyRequirementMore supplyRequirementMore:map.values()){
            LoanOrder loanOrder = supplyRequirementMore.getLoanOrder();
            if(loanOrder==null){
                size = size-1;
                log.error(String.format("These supplyRequirement[id:{%s}] is dirty data! ",supplyRequirementMore.getId()));
                continue;
            }
            Long payFundId = loanOrder.getPayFundId();
            if(payFundId!=null){
                Fund fund = fundService.findOne(payFundId);
                if(fund!=null){
                    supplyRequirementMore.setFundName(fund.getFundName());
                }
            }
            //判断是否已提交补件
            List<LoanSupplyLog> byLoanOrder = supplyLogService.findByLoanOrder(loanOrder.getId());
            if(byLoanOrder.size()>=(int)supplyRequirementMore.getSupplyCount()){
                size = size-1;
            }else{
                result2PageList.add(supplyRequirementMore);
            }
        }
        Page<SupplyRequirementMore> supplyRequirementMorePage = buildPage(result2PageList, pageNo, pageSize);
        return supplyRequirementMorePage;
    }


    /***
     * 获取详细页面
     * @param id
     * @return
     */
    @RequestMapping(value = "detail/{id}", method = RequestMethod.GET)
    public ModelAndView detail(@PathVariable("id") String id) {
        ModelAndView modelAndView = new ModelAndView("/backend/supply/requirement/detail");
        SupplyRequirementMore supplyRequirement = getService().findOne(id);
        modelAndView.addObject("supplyRequirement",supplyRequirement);
        modelAndView.addObject("createdTime",DateUtils.format(supplyRequirement.getCreatedTime(),DATA_FORMAT));
        LoanOrderMore loanOrder = supplyRequirement.getLoanOrder();
        if(loanOrder!=null){
            Long payFundId = loanOrder.getPayFundId();
            if(payFundId!=null){
                Fund fund = fundService.findOne(payFundId);
                if(fund!=null){
                    modelAndView.addObject("fundName",fund.getFundName());
                }
            }
        }

        List<Map> supplyRequirementInfo = JSON.parseObject(supplyRequirement.getRequirementInfo(), List.class);
        List<Map> info = new ArrayList<>();
        for (Map map:supplyRequirementInfo){
            map.put("material",mapProperties.getSupplyMarterialNm(map.get("code").toString()));
            map.put("type",mapProperties.getSupplyMarterialType(map.get("code").toString()));
            info.add(map);
        }
        modelAndView.addObject("supplyRequirementInfo",info);
        return modelAndView;
    }


    @ModelAttribute
    public void getloanOrder(@RequestParam(value = "id", defaultValue = "-1") String id, Model model) {
        if (!id.equals("-1")) {
            model.addAttribute("supplyRequirement", this.supplyRequirementService.findOneBySql(getOneSql(id)));
        }
    }

    private String getOneSql(String id) {
        return null;
    }


    @Override
    protected DynamitSupportService<SupplyRequirementMore> getService() {
        return this.supplyRequirementService;
    }


    private SelectMultiTable getSelectMultiTable() {
        return SelectMultiTable.builder(SupplyRequirementMore.class)
                .leftJoin(LoanOrderMore.class, "loan_order", new OnCondition[]{
                        new OnCondition(SelectMultiTable.ConnectType.AND, "loanOrderId", Operator.EQ, "id"),
                }).leftJoin(IdCardData.class, "idcard", new OnCondition[]{
                        new OnCondition(SelectMultiTable.ConnectType.AND, "userId", Operator.EQ, "userId"),
                }).leftJoin(User.class, "user", new OnCondition[]{
                        new OnCondition(SelectMultiTable.ConnectType.AND, "userId", Operator.EQ, "id"),
                });
    }


}
