/**
 * @author
 * @version 1.0
 * @desc 借款订单表
 * @since 2018-07-25 12:16:07
 */

package com.feitai.admin.backend.loan.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.feitai.admin.backend.customer.service.BankSupportService;
import com.feitai.admin.backend.customer.service.IdCardService;
import com.feitai.admin.backend.customer.service.UserService;
import com.feitai.admin.backend.fund.service.FundService;
import com.feitai.admin.backend.loan.entity.LoanOrderMore;
import com.feitai.admin.backend.loan.entity.RepayOrderMore;
import com.feitai.admin.backend.loan.service.LoanOrderService;
import com.feitai.admin.backend.loan.service.RepayPlanComponentService;
import com.feitai.admin.backend.loan.service.RepayPlanService;
import com.feitai.admin.backend.loan.vo.OrderPlande;
import com.feitai.admin.backend.opencard.service.CardService;
import com.feitai.admin.backend.product.service.ProductService;
import com.feitai.admin.backend.product.service.ProductTermFeeFeatureService;
import com.feitai.admin.backend.properties.AppProperties;
import com.feitai.admin.backend.properties.MapProperties;
import com.feitai.admin.core.annotation.LogAnnotation;
import com.feitai.admin.core.service.*;
import com.feitai.admin.core.vo.ListItem;
import com.feitai.admin.core.web.BaseListableController;
import com.feitai.admin.core.web.PageBulider;
import com.feitai.jieya.server.dao.bank.model.BankSupport;
import com.feitai.jieya.server.dao.card.model.Card;
import com.feitai.jieya.server.dao.data.model.IdCardData;
import com.feitai.jieya.server.dao.fund.model.Fund;
import com.feitai.jieya.server.dao.loan.model.RepayPlan;
import com.feitai.jieya.server.dao.product.model.Product;
import com.feitai.jieya.server.dao.product.model.ProductTermFeeFeature;
import com.feitai.jieya.server.dao.user.model.User;
import com.feitai.utils.Desensitization;
import com.feitai.utils.ObjectUtils;
import com.feitai.utils.datetime.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletRequest;
import java.math.BigDecimal;
import java.util.*;


@Controller
@RequestMapping(value = "/backend/loanOrder")
@Slf4j
public class LoanOrderController extends BaseListableController<LoanOrderMore> {

    @Autowired
    private LoanOrderService loanOrderService;

    @Autowired
    private ProductTermFeeFeatureService productTermFeeFeatureService;

    @Autowired
    private IdCardService idcardService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CardService cardService;

    @Autowired
    private BankSupportService bankSupportService;

    @Autowired
    private RepayPlanService repayPlanService;

    @Autowired
    private RepayPlanComponentService repayPlanComponentService;

    @Autowired
    private MapProperties mapProperties;

    @Autowired
    private AppProperties appProperties;

    @Autowired
    private FundService fundService;


    @RequiresUser
    @GetMapping(value = "/index")
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("/backend/loanOrder/index");
        String rejectCash = appProperties.getRejectCash();
        modelAndView.addObject("rejectCash", rejectCash);
        Map<String, String> loanStatusMap = mapProperties.getLoanStatusMap();
        modelAndView.addObject("loanStatusMap",JSON.toJSONString(loanStatusMap));
        return modelAndView;
    }


    @RequestMapping(value = "getLoanStatusList")
    @ResponseBody
    @LogAnnotation(value = true, writeRespBody = false)
    public Object getLoanStatusList(){
        Map<String, String> loanStatusMap = mapProperties.getLoanStatusMap();
        List<ListItem> list = new ArrayList<ListItem>();
        list.add(new ListItem("全部"," "));
        for(String key:loanStatusMap.keySet()){
            list.add(new ListItem(loanStatusMap.get(key), key));
        }
        return list;
    }


    @RequiresPermissions("/backend/loanOrder:list")
    @PostMapping("/list")
    @ResponseBody
    public Map<String, Object> listPage(ServletRequest request) {
        Map<String, Object> map = listSup(request);
        return map;
    }


    /***
     * 取消放款
     * @param id
     * @return
     */
    @PostMapping("/rejectCash/{id}")
    @RequiresPermissions("/backend/loanOrder:auth")
    @ResponseBody
    public Object rejectCash(@PathVariable("id") String id) {
        //String url = "http://10.168.2.207:9090/cash/reject";
        //String json = "{\"serialNo\":\""+id+"\"}";
        //String result = HttpUtil.postByTypeIsJson(url, json);

        return BaseListableController.successResult;
    }

    /***
     * 获取详细页面
     * @param id
     * @return
     */
    @RequiresUser
    @RequestMapping(value = "detail/{id}", method = RequestMethod.GET)
    public ModelAndView detail(@PathVariable("id") String id) {
        ModelAndView modelAndView = new ModelAndView("/backend/loanOrder/detail");
        LoanOrderMore loanOrder = loanOrderService.findOneBySql(getOneSql(id));
        Long userId = loanOrder.getUserId();
        User userIn = userService.findOne(userId);
        IdCardData idcard = idcardService.findByUserId(userId);
        Product product = productService.findOne(loanOrder.getProductId());

        //资金方
        Fund fund = fundService.findOne(loanOrder.getPayFundId());
        if(fund!=null){
            modelAndView.addObject("fundName",fund.getFundName());
        }

        //获取产品详细信息，需要产品id和期数
        List<ProductTermFeeFeature> byProductIdAndTerm = productTermFeeFeatureService.findByProductIdAndTerm(loanOrder.getProductId(), loanOrder.getLoanTerm().shortValue());

        //脱敏处理
        String hyPhone = Desensitization.phone(userIn.getPhone());
        modelAndView.addObject("hyPhone", hyPhone);
        String hyIdcard = Desensitization.idCard(idcard.getIdCard());
        modelAndView.addObject("hyIdcard", hyIdcard);
        if (loanOrder.getBankCardNo() != null) {
            String bankCardNo = Desensitization.bankCardNo(loanOrder.getBankCardNo());
            modelAndView.addObject("bankCardNo", bankCardNo);
        }

        modelAndView.addObject("loanOrder",loanOrder);
        modelAndView.addObject("user",userIn);
        modelAndView.addObject("idCard",idcard);
        modelAndView.addObject("product",product);
        if(byProductIdAndTerm.size()!=0){
            modelAndView.addObject("productIdAndTerm",byProductIdAndTerm.get(0));
        }
        int year = new Date().getYear() - idcard.getBirthday().getYear();
        modelAndView.addObject("year",year);
        Integer status = loanOrder.getStatus();
        String statu = mapProperties.getloanStatus(status.toString());

        //授信
        Card card = cardService.findSingleById(loanOrder.getCardId());
        if(card!=null){
            BigDecimal creditSum = card.getCreditSum();
            modelAndView.addObject("shouxin",creditSum);
        }
        modelAndView.addObject("status",statu);
        if(loanOrder.getBankCode()!=null){
            BankSupport bankSuppor = bankSupportService.findByBankCode(loanOrder.getBankCode());
            modelAndView.addObject("bank", bankSuppor.getBankName());
        }

        List<RepayPlan> byLoanOrderId = repayPlanService.findByLoanOrderId(Long.parseLong(id));
        //还款计划
        List<OrderPlande> orderPlandes = repayPlanComponentService.findOrderPlandesByRepayPlans(byLoanOrderId);
        if(orderPlandes.size()==0){
            modelAndView.addObject("havePlan",false);
        }else{
            modelAndView.addObject("havePlan",true);
        }
        modelAndView.addObject("repayPlan", orderPlandes);
        if(loanOrder.getPayLoanTime()!=null){
            modelAndView.addObject("payLoanTime",DateUtils.format(loanOrder.getPayLoanTime(),"yyyy-MM-dd HH:mm:ss"));
        }
        if(loanOrder.getApplyTime()!=null){
            modelAndView.addObject("applyTime",DateUtils.format(loanOrder.getApplyTime(),"yyyy-MM-dd HH:mm:ss"));
        }

        return modelAndView;
    }

    /**
     * 所有RequestMapping方法调用前的Model准备方法, 实现Struts2 Preparable二次部分绑定的效果,先根据form的id从数据库查出User对象,再把Form提交的内容绑定到该对象上。
     * 因为仅update()方法的form中有id属性，因此仅在update时实际执行.
     */
    @ModelAttribute
    public void getloanOrder(@RequestParam(value = "id", defaultValue = "-1") String id, Model model) {
        if (!id.equals("-1")) {
            model.addAttribute("loanOrder", this.loanOrderService.findOneBySql(getOneSql(id)));
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public Map<String, Object> listSup(ServletRequest request) {
        //根据request获取page
        int pageNo = PageBulider.getPageNo(request);
        int pageSize = PageBulider.getPageSize(request);
        Page<LoanOrderMore> loanOrderMorePage = list(getCommonSqls(request,getSelectMultiTable().buildSqlString()), pageNo, pageSize, getCountSqls(request), SelectMultiTable.COUNT_ALIAS);
        List<LoanOrderMore> content = loanOrderMorePage.getContent();
        List<JSONObject> resultList = new ArrayList<>();

        //遍历page中内容，修改或添加非数据库的自定义字段
        for (LoanOrderMore loanOrderMore :
                content) {
            JSONObject json = (JSONObject) JSON.toJSON(loanOrderMore);
            try{
                JSONObject jsonObject = handleSingleData(json);
                resultList.add(jsonObject);
            }catch (Exception e){
                log.error("this json handle fail:[{}]! message:{}",json,e.getMessage());
                continue;
            }
        }
        return switchContent(loanOrderMorePage,resultList);
    }

    /***
     * 处理单条数据
     * @return
     */
    private JSONObject handleSingleData(JSONObject json){
        List<ProductTermFeeFeature> search = new ArrayList<ProductTermFeeFeature>();
        //productTermFeeFeature
        Long productId = (Long) json.get("productId");
        Integer loanTerm = (Integer)json.get("loanTerm");
        Long cardId = (Long)json.get("cardId");

        search = productTermFeeFeatureService.findByProductIdAndTerm(productId.longValue(),loanTerm.shortValue());

        if(search.size()>0){
            ProductTermFeeFeature productTermFeeFeature = search.get(0);
            try {
                Map<String, Object> stringObjectMap = ObjectUtils.objectToMap(productTermFeeFeature);
                json.put("productTermFeeFeature",stringObjectMap);
            }catch (Exception e){
                log.error("productTermFeeFeature can't achieve",e);
            }
        }

        Long payFundId = (Long) json.get("payFundId");
        if(payFundId!=null){
            Fund fund = fundService.findOne(payFundId);
            if(fund!=null)
                json.put("fundName",fund.getFundName());
        }else{
            json.put("fundName","");
        }

        //授信
        Card card = cardService.findSingleById(cardId);
        if(card!=null){
            BigDecimal creditSum = card.getCreditSum();
            json.put("card.creditSum",creditSum);
            json.put("cardStatus",card.getStatus());
            json.put("card",card);
        }

        return json;
    }


    private String getCountSqls(ServletRequest request) {
        StringBuffer sbSql = new StringBuffer();
        sbSql.append(SelectMultiTable.builder(LoanOrderMore.class).buildCountSqlString());
        sbSql.append(getService().buildSqlWhereCondition(bulidSearchParamsList(request), SelectMultiTable.MAIN_ALAIS));
        return sbSql.toString();
    }

    private SelectMultiTable getSelectMultiTable() {
        return SelectMultiTable.builder(LoanOrderMore.class)
                .leftJoin(RepayOrderMore.class, "repay_order", new OnCondition[]{
                        new OnCondition(SelectMultiTable.ConnectType.AND, "id", Operator.EQ, "loanOrderId"),
                }).leftJoin(IdCardData.class, "idCard", new OnCondition[]{
                        new OnCondition(SelectMultiTable.ConnectType.AND, "userId", Operator.EQ, "userId")
                }).leftJoin(Product.class, "product", new OnCondition[]{
                        new OnCondition(SelectMultiTable.ConnectType.AND, "productId", Operator.EQ, "id")
                }).leftJoin(User.class, "user_in", new OnCondition[]{
                        new OnCondition(SelectMultiTable.ConnectType.AND, "userId", Operator.EQ, "id")
                }).leftJoin(Card.class, "opencard", new OnCondition[]{
                        new OnCondition(SelectMultiTable.ConnectType.AND, "userId", Operator.EQ, "userId")
                });
    }

    @Override
    protected DynamitSupportService<LoanOrderMore> getService() {
        return this.loanOrderService;
    }


    protected String getOneSql(String id) {
        String sql = SelectMultiTable.builder(LoanOrderMore.class)
                .leftJoin(RepayOrderMore.class, "repay_order", new OnCondition[]{
                        new OnCondition(SelectMultiTable.ConnectType.AND, "id", Operator.EQ, "loanOrderId"),
                }).leftJoin(IdCardData.class, "idCard", new OnCondition[]{
                        new OnCondition(SelectMultiTable.ConnectType.AND, "userId", Operator.EQ, "userId")
                }).leftJoin(Product.class, "product", new OnCondition[]{
                        new OnCondition(SelectMultiTable.ConnectType.AND, "productId", Operator.EQ, "id")
                }).leftJoin(User.class, "user_in", new OnCondition[]{
                        new OnCondition(SelectMultiTable.ConnectType.AND, "userId", Operator.EQ, "id")
                }).leftJoin(Card.class, "opencard", new OnCondition[]{
                        new OnCondition(SelectMultiTable.ConnectType.AND, "userId", Operator.EQ, "userId")
                }).buildSqlString() + "where maintable.id = " + id + " Group by id";
        return sql;
    }

}
