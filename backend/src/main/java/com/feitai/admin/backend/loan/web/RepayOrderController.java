/**
 * @author
 * @version 1.0
 * @desc 还款支付订单表
 * @since 2018-07-25 21:19:09
 */

package com.feitai.admin.backend.loan.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.feitai.admin.backend.customer.service.IdCardService;
import com.feitai.admin.backend.customer.service.UserService;
import com.feitai.admin.backend.fund.service.FundService;
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
import com.feitai.admin.core.vo.ListItem;
import com.feitai.admin.core.web.BaseListableController;
import com.feitai.admin.core.web.PageBulider;
import com.feitai.jieya.server.dao.data.model.IdCardData;
import com.feitai.jieya.server.dao.fund.model.Fund;
import com.feitai.jieya.server.dao.loan.model.LoanOrder;
import com.feitai.jieya.server.dao.loan.model.RepayOrder;
import com.feitai.jieya.server.dao.loan.model.RepayPlan;
import com.feitai.jieya.server.dao.product.model.Product;
import com.feitai.jieya.server.dao.product.model.ProductTermFeeFeature;
import com.feitai.jieya.server.dao.user.model.User;
import com.feitai.jieya.server.utils.IdCardUtils;
import com.feitai.utils.Desensitization;
import com.feitai.utils.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletRequest;
import java.math.BigDecimal;
import java.util.*;


@Controller
@RequestMapping(value = "/backend/loan/repayOrder")
@Slf4j
public class RepayOrderController extends BaseListableController<RepayOrderMore> {

    @Autowired
    private RepayOrderService repayOrderService;

    @Autowired
    private IdCardService idcardService;

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
    
    @Autowired
    private FundService fundService;

    @RequestMapping(value = "")
    public String index(Model model) {
        model.addAttribute("isOut",false);
        return "/backend/repayOrder/index";
    }
    
    /**
     * 首期还款列表页面
     * @return
     */
    @RequestMapping(value = "/firstRepayOrder")
    public ModelAndView firstRepayOrder() {
    	ModelAndView modelAndView=new ModelAndView("/backend/repayOrder/firstRepayOrder");
    	getProductList(modelAndView);
        return modelAndView;
    }

	private void getProductList(ModelAndView modelAndView) {
		List<Product> products = productService.findAll();
		List<ListItem> list = new ArrayList<ListItem>();
		list.add(new ListItem("全部"," "));
		for(Product product:products){
			list.add(new ListItem(product.getName(), product.getId().toString()));
		}
		modelAndView.addObject("productList",JSONObject.toJSONString(list));
	}
	  /**
     * 逾期还款列表页面
     * @return
     */
    @RequestMapping(value = "/pastRepayOrder")
    public ModelAndView pastRepayOrder() {
    	ModelAndView modelAndView=new ModelAndView("/backend/repayOrder/pastRepayOrder");
    	getProductList(modelAndView);
        return modelAndView;
    }

    @RequestMapping(value = "listOut")
    @ResponseBody
    public Map<String, Object> listOut(ServletRequest request){
        return listSupport(request);
    }

	@RequiresPermissions("/backend/loan/repayOrder:list")
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
        if(repayOrder.getLoanOrder()!=null){
            if(repayOrder.getLoanOrder().getProductId()!=null){
                Product product = productService.findOne(repayOrder.getLoanOrder().getProductId());
                model.addAttribute("prodcutName", product.getName());
            }
            List<ProductTermFeeFeature> byProductIdAndTerm = productTermFeeFeatureService.findByProductIdAndTerm(repayOrder.getLoanOrder().getProductId(), repayOrder.getLoanOrder().getLoanTerm().shortValue());
            model.addAttribute("productIdAndTerm", byProductIdAndTerm.get(0));
        }

        //脱敏处理
        String payAccount = repayOrder.getPayAccount();
        if(payAccount!=null){
            String phPayAccount = Desensitization.bankCardNo(payAccount);
            model.addAttribute("phPayAccount",phPayAccount);
        }
        model.addAttribute("repayOrder", repayOrder);
        model.addAttribute("userIn", userIn);
        model.addAttribute("idCard", idcard);

        int ageByIdCard = IdCardUtils.getAgeByIdCard(idcard.getIdCard());
        model.addAttribute("year", ageByIdCard);
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
        return "/backend/repayOrder/detail";
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
        //根据request获取page
        int pageNo = PageBulider.getPageNo(request);
        int pageSize = PageBulider.getPageSize(request);
        Page<RepayOrderMore> repayOrderMorePage = list(getCommonSqls(request,getSelectMultiTable().buildSqlString()), pageNo, pageSize, getCountSqls(request), SelectMultiTable.COUNT_ALIAS);
        List<RepayOrderMore> content = repayOrderMorePage.getContent();
        List<JSONObject> resultList = new ArrayList<>();
        for (RepayOrderMore repayOrderMore :
                content) {
            Map<String, Object> productTermFeeFeatureMap = new HashMap<>();
            JSONObject json = (JSONObject) JSON.toJSON(repayOrderMore);
            //IdCardDataExtend
            Long userId = (Long) json.get("userId");

            //product
            JSONObject loanOrder =  json.getJSONObject("loanOrder");
            Integer productId = loanOrder.getInteger("productId");
            Integer loanTerm = loanOrder.getInteger("loanTerm");


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
            json.put("productTermFeeFeature", productTermFeeFeatureMap);

            //repayPlan
            JSONObject repayPlan = (JSONObject)json.get("repayPlan");

            //实还款日repayDate
            json.put("repayDate", "未");
            //当期/总期termPre
            json.put("termPre", repayPlan.get("term").toString() + "/" + loanTerm);
            //脱敏处理
            //TODO-日后处理idcard/payAccount的脱敏处理
            JSONObject idcard = (JSONObject)json.get("idCard");
            String hyIdcard = Desensitization.idCard((String)idcard.get("idCard"));
            idcard.put("idCard",hyIdcard);
            json.remove("idCard");
            json.put("idCard", idcard);

            String hyCard = Desensitization.bankCardNo((String)json.get("payAccount"));
            json.put("payAccount", hyCard);
            resultList.add(json);
            List<RepayPlan> byLoanOrderId = repayPlanService.findByLoanOrderIdAndTerm(repayOrderMore.getLoanOrderId(),(short)1);
            List<OrderPlande> orderPlandes = repayPlanComponentService.findOrderPlandesByRepayPlans(byLoanOrderId);
            if(orderPlandes.size()>0){
            	json.put("orderPlande", JSONObject.toJSON(orderPlandes.get(0)));
            }
            if(repayOrderMore.getLoanOrder()!=null && repayOrderMore.getLoanOrder().getPayFundId()!=null){
              Fund fund=fundService.getFund(repayOrderMore.getLoanOrder().getPayFundId());
              json.put("fundName", Optional.ofNullable(fund).map(f ->f.getFundName()).orElse(""));
            }
            Product product=productService.findOne(productId);
            json.put("product", product);
        }
        return switchContent(repayOrderMorePage,resultList);

    }

    private String getCountSqls(ServletRequest request) {
        StringBuffer sbSql = new StringBuffer();
        sbSql.append(getSelectMultiTable().buildCountSqlString());
        sbSql.append(getService().buildSqlWhereCondition(bulidSearchParamsList(request), SelectMultiTable.MAIN_ALAIS));
        return sbSql.toString();
    }

    @Override
    protected DynamitSupportService<RepayOrderMore> getService() {
        return this.repayOrderService;
    }

    private SelectMultiTable getSelectMultiTable() {
        return  SelectMultiTable.builder(RepayOrder.class)
                .leftJoin(RepayPlan.class,"repay_plan",new OnCondition[]{
                        new OnCondition(SelectMultiTable.ConnectType.AND, "repayPlanId", Operator.EQ, "id"),
                }).leftJoin(IdCardData.class,"idCard",new OnCondition[]{
                        new OnCondition(SelectMultiTable.ConnectType.AND,"userId",Operator.EQ,"userId")
                }).leftJoin(LoanOrder.class,"loan_order",new OnCondition[]{
                        new OnCondition(SelectMultiTable.ConnectType.AND,"loanOrderId",Operator.EQ,"id")
                }).leftJoin(User.class,"user_in",new OnCondition[]{
                        new OnCondition(SelectMultiTable.ConnectType.AND,"userId",Operator.EQ,"id")
                });
    }

    protected String getOneSql(Object id){
        String sql = SelectMultiTable.builder(RepayOrder.class)
                .leftJoin(RepayPlan.class,"repay_plan",new OnCondition[]{
                        new OnCondition(SelectMultiTable.ConnectType.AND, "repayPlanId", Operator.EQ, "id"),
                }).leftJoin(IdCardData.class,"idCard",new OnCondition[]{
                        new OnCondition(SelectMultiTable.ConnectType.AND,"userId",Operator.EQ,"userId")
                }).leftJoin(LoanOrder.class,"loan_order",new OnCondition[]{
                        new OnCondition(SelectMultiTable.ConnectType.AND,"loanOrderId",Operator.EQ,"id")
                }).leftJoin(User.class,"user_in",new OnCondition[]{
                        new OnCondition(SelectMultiTable.ConnectType.AND,"userId",Operator.EQ,"id")
                }).buildSqlString()+" where id = '" + id +"' Group by id";
        return sql;
    }

}
