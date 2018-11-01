/**
 * @author
 * @version 1.0
 * @desc 还款支付订单表
 * @since 2018-07-25 21:19:09
 */

package com.feitai.admin.backend.loan.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.feitai.admin.backend.customer.service.IdcardService;
import com.feitai.admin.backend.customer.service.UserService;
import com.feitai.admin.backend.loan.entity.RepayOrderMore;
import com.feitai.admin.backend.loan.service.RepayOrderService;
import com.feitai.admin.backend.loan.service.RepayPlanComponentService;
import com.feitai.admin.backend.loan.service.RepayPlanService;
import com.feitai.admin.backend.loan.vo.OrderPlande;
import com.feitai.admin.backend.opencard.entity.CardMore;
import com.feitai.admin.backend.opencard.service.CardService;
import com.feitai.admin.backend.product.service.ProductService;
import com.feitai.admin.backend.product.service.ProductTermFeeFeatureService;
import com.feitai.admin.core.service.*;
import com.feitai.admin.core.web.BaseListableController;
import com.feitai.jieya.server.dao.data.model.IdCardData;
import com.feitai.jieya.server.dao.loan.model.LoanOrder;
import com.feitai.jieya.server.dao.loan.model.RepayOrder;
import com.feitai.jieya.server.dao.loan.model.RepayPlan;
import com.feitai.jieya.server.dao.product.model.Product;
import com.feitai.jieya.server.dao.product.model.ProductTermFeeFeature;
import com.feitai.jieya.server.dao.user.model.User;
import com.feitai.utils.Desensitization;
import com.feitai.utils.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import java.math.BigDecimal;
import java.util.*;


@Controller
@RequestMapping(value = "/admin/loan/repayOrder")
@Slf4j
public class RepayOrderController extends BaseListableController<RepayOrderMore> {

    @Autowired
    private RepayOrderService repayOrderService;

    @Autowired
    private IdcardService idcardService;

    @Autowired
    private CardService cardService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @Autowired
    private RepayPlanService repayPlanService;

    @Autowired
    private RepayPlanComponentService repayPlanComponentService;

    @Autowired
    private ProductTermFeeFeatureService productTermFeeFeatureService;

    @RequestMapping(value = "")
    public String index(Model model) {
        model.addAttribute("isOut",false);
        return "/admin/loan/repayOrder/index";
    }


    @RequestMapping(value = "listOut")
    @ResponseBody
    public Map<String, Object> listOut(ServletRequest request){
        return listSupport(request);
    }

	@RequiresPermissions("/admin/loan/repayOrder:list")
    @RequestMapping(value = "list")
    @ResponseBody
    public Map<String,Object> listPage(ServletRequest request) {
        return listSupport(request);
    }

    @RequestMapping(value = "auth/{id}", method = RequestMethod.GET)
    //@RequiresPermissions("/admin/loan/loanOrder:auth")
    public String auth(@PathVariable("id") String id, Model model) {
        RepayOrderMore repayOrder = repayOrderService.findOneBySql(getOneSql(id));
        Long userId = repayOrder.getUserId();
        User userIn = userService.findOne(userId);
        IdCardData idcard = idcardService.findByUserId(userId);
        Product product = productService.findOne(repayOrder.getLoanOrder().getProductId());
        List<ProductTermFeeFeature> byProductIdAndTerm = productTermFeeFeatureService.findByProductIdAndTerm(repayOrder.getLoanOrder().getProductId(), repayOrder.getLoanOrder().getLoanTerm().shortValue());
        //脱敏处理
        String payAccount = repayOrder.getPayAccount();
        String phPayAccount = Desensitization.bankCardNo(payAccount);
        model.addAttribute("phPayAccount",phPayAccount);

        model.addAttribute("repayOrder", repayOrder);
        model.addAttribute("userIn", userIn);
        model.addAttribute("idcard", idcard);
        model.addAttribute("prodcutName", product.getName());
        model.addAttribute("productIdAndTerm", byProductIdAndTerm.get(0));
        int year = new Date().getYear() - idcard.getBirthday().getYear();
        model.addAttribute("year", year);
        Byte status = repayOrder.getStatus();
        String statu = "暂无";

        //授信
        CardMore card = cardService.findByUserId(userId);
        BigDecimal creditSum = card.getCreditSum();
        model.addAttribute("shouxin", creditSum);
        model.addAttribute("status", statu);


        //成分详情
        List<RepayPlan> byLoanOrderId = repayPlanService.findByLoanOrderId(repayOrder.getLoanOrderId());
        List<OrderPlande> orderPlandes = repayPlanComponentService.findOrderPlandesByRepayPlans(byLoanOrderId);
        model.addAttribute("repayPlan",orderPlandes);
        return "/admin/loan/repayOrder/detail";
    }

    /**
     * 所有RequestMapping方法调用前的Model准备方法, 实现Struts2 Preparable二次部分绑定的效果,先根据form的id从数据库查出User对象,再把Form提交的内容绑定到该对象上。
     * 因为仅update()方法的form中有id属性，因此仅在update时实际执行.
     */
    @ModelAttribute
    public void getrepayOrder(@RequestParam(value = "id", defaultValue = "-1") String id, Model model) {
        if (!id.equals("-1")) {
            model.addAttribute("repayOrder", this.repayOrderService.findOneBySql(getOneSql(id)));
        }
    }

    public Map<String,Object> listSupport(ServletRequest request){
        Page<RepayOrderMore> listPage = super.list(request);
        String list = JSON.toJSONString(listPage);;
        List newList = new ArrayList();
        Map mapList = JSON.parseObject(list, Map.class);
        Map<String, Object> productTermFeeFeatureMap = new HashMap<String, Object>();
        List<JSONObject> content = (List<JSONObject>) mapList.get("content");
        for (JSONObject json :
                content) {
            Map<String, Object> map = JSONObject.parseObject(json.toJSONString(), new TypeReference<Map<String, Object>>(){});
            //IdCardDataExtend
            Long userId = (Long) map.get("userId");

            //product
            HashMap loanOrder = (HashMap) map.get("loanOrder");
            Integer productId = (Integer) loanOrder.get("productId");
            Integer loanTerm = (Integer) loanOrder.get("loanTerm");


            //productTermFeeFeature
            List<ProductTermFeeFeature> search = new ArrayList<ProductTermFeeFeature>();
            search = productTermFeeFeatureService.findByProductIdAndTerm(productId.longValue(), loanTerm.shortValue());

            if (search.size() > 0) {
                ProductTermFeeFeature productTermFeeFeature = search.get(0);
                try {
                    productTermFeeFeatureMap = ObjectUtils.objectToMap(productTermFeeFeature);
                } catch (Exception e) {
                    log.error("",e);
                }
            }
            map.put("productTermFeeFeature", productTermFeeFeatureMap);

            //repayPlan
            JSONObject repayPlan = (JSONObject)map.get("repayPlan");

            //实还款日repayDate
            map.put("repayDate", "未");
            //当期/总期termPre
            map.put("termPre", repayPlan.get("term").toString() + "/" + loanTerm);
            //脱敏处理
            //TODO-日后处理idcard/payAccount的脱敏处理
            JSONObject idcard = (JSONObject)map.get("idcard");
            String hyIdcard = Desensitization.idCard((String)idcard.get("idCard"));
            idcard.put("idCard",hyIdcard);
            map.remove("idcard");
            map.put("idcard", idcard);

            String hyCard = Desensitization.bankCardNo((String)map.get("payAccount"));
            map.put("payAccount", hyCard);

            newList.add(map);
        }
        mapList.put("content", newList);
        return mapList;

    }

    @Override
    protected DynamitSupportService<RepayOrderMore> getService() {
        return this.repayOrderService;
    }

    protected String getSql() {
        String sql = SelectMultiTable.builder(RepayOrder.class)
                .leftJoin(RepayPlan.class,"repay_plan",new OnCondition[]{
                        new OnCondition(SelectMultiTable.ConnectType.AND, "repayPlanId", Operator.EQ, "id"),
                }).leftJoin(IdCardData.class,"idcard",new OnCondition[]{
                        new OnCondition(SelectMultiTable.ConnectType.AND,"userId",Operator.EQ,"userId")
                }).leftJoin(LoanOrder.class,"loan_order",new OnCondition[]{
                        new OnCondition(SelectMultiTable.ConnectType.AND,"loanOrderId",Operator.EQ,"id")
                }).leftJoin(User.class,"user_in",new OnCondition[]{
                        new OnCondition(SelectMultiTable.ConnectType.AND,"userId",Operator.EQ,"id")
                }).buildSqlString();
        return sql;
    }

    protected String getOneSql(Object id){
        String sql = SelectMultiTable.builder(RepayOrder.class)
                .leftJoin(RepayPlan.class,"repay_plan",new OnCondition[]{
                        new OnCondition(SelectMultiTable.ConnectType.AND, "repayPlanId", Operator.EQ, "id"),
                }).leftJoin(IdCardData.class,"idcard",new OnCondition[]{
                        new OnCondition(SelectMultiTable.ConnectType.AND,"userId",Operator.EQ,"userId")
                }).leftJoin(LoanOrder.class,"loan_order",new OnCondition[]{
                        new OnCondition(SelectMultiTable.ConnectType.AND,"loanOrderId",Operator.EQ,"id")
                }).leftJoin(User.class,"user_in",new OnCondition[]{
                        new OnCondition(SelectMultiTable.ConnectType.AND,"userId",Operator.EQ,"id")
                }).buildSqlString()+" where id = '" + id +"'";
        return sql;
    }

    @InitBinder
    public void initDate(WebDataBinder webDataBinder){
        webDataBinder.addCustomFormatter(new DateFormatter("yyyy-MM-dd HH:mm:ss"));
        webDataBinder.addCustomFormatter(new DateFormatter("yyyy-MM-dd"));
    }


}
