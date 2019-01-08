/**
 * @author
 * @version 1.0
 * @desc 还款支付订单表
 * @since 2018-07-25 21:19:09
 */

package com.feitai.admin.backend.loan.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.csvreader.CsvWriter;
import com.feitai.admin.backend.customer.service.AreaService;
import com.feitai.admin.backend.customer.service.IdCardService;
import com.feitai.admin.backend.customer.service.UserService;
import com.feitai.admin.backend.fund.service.FundService;
import com.feitai.admin.backend.loan.entity.LoanOrderMore;
import com.feitai.admin.backend.loan.entity.RepayOrderMore;
import com.feitai.admin.backend.loan.service.LoanOrderService;
import com.feitai.admin.backend.loan.service.RepayOrderService;
import com.feitai.admin.backend.loan.service.RepayPlanComponentService;
import com.feitai.admin.backend.loan.service.RepayPlanService;
import com.feitai.admin.backend.loan.vo.OrderPlande;
import com.feitai.admin.backend.loan.vo.RepayPlanVo;
import com.feitai.admin.backend.opencard.entity.CardMore;
import com.feitai.admin.backend.opencard.service.CardService;
import com.feitai.admin.backend.product.service.ProductService;
import com.feitai.admin.backend.product.service.ProductTermFeeFeatureService;
import com.feitai.admin.backend.properties.MapProperties;
import com.feitai.admin.core.annotation.LogAnnotation;
import com.feitai.admin.core.service.*;
import com.feitai.admin.core.vo.ListItem;
import com.feitai.admin.core.web.BaseListableController;
import com.feitai.admin.core.web.PageBulider;
import com.feitai.jieya.server.dao.bank.model.UserBankCard;
import com.feitai.jieya.server.dao.data.model.IdCardData;
import com.feitai.jieya.server.dao.data.model.LocationData;
import com.feitai.jieya.server.dao.fund.model.Fund;
import com.feitai.jieya.server.dao.loan.model.LoanOrder;
import com.feitai.jieya.server.dao.loan.model.RepayOrder;
import com.feitai.jieya.server.dao.loan.model.RepayPlan;
import com.feitai.jieya.server.dao.product.model.Product;
import com.feitai.jieya.server.dao.product.model.ProductTermFeeFeature;
import com.feitai.jieya.server.dao.user.model.User;
import com.feitai.utils.Desensitization;
import com.feitai.utils.ObjectUtils;
import com.feitai.utils.datetime.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;


@Controller
@RequestMapping(value = "/backend/loan/repayOrder")
@Slf4j
public class RepayOrderController extends BaseListableController<RepayOrderMore> {

    @Autowired
    private RepayOrderService repayOrderService;

    @Autowired
    private LoanOrderService loanOrderService;

    @Autowired
    private IdCardService idcardService;

    @Autowired
    private CardService cardService;

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

    @Autowired
    private AreaService areaService;

    @Autowired
    private ProductService productService;


    @Autowired
    private MapProperties mapProperties;

    private final static String DATE_FORMAT = "yyyy-MM-dd";

    private final static String LOAN_PURPOSE = "loanPurpose";

    private final static String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";


    @RequestMapping(value = "/repayOrderStatus")
    @ResponseBody
    @LogAnnotation(value = true, writeRespBody = false)// 写日志但是不打印请求的params,但不打印ResponseBody的内容
    public Object repayOrderStatus(){
        Map<String, String> repayOrderStatusMap = mapProperties.getRepayOrderStatusMap();
        List<ListItem> list = new ArrayList<ListItem>();
        list.add(new ListItem("全部", " "));
        for (String key : repayOrderStatusMap.keySet()) {
            list.add(new ListItem(repayOrderStatusMap.get(key), key));
        }
        return list;
    }

    @RequestMapping(value = "index")
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("/backend/repayOrder/index");
        modelAndView.addObject("isOut",false);
        return modelAndView;
    }

    @RequestMapping(value = "repayPlan/{id}")
    @RequiresPermissions("/backend/loan/repayOrder:list")
    @ResponseBody
    public ModelAndView repayPlan(@PathVariable("id") Long id) {
        List<RepayPlanVo> repayPlanVoByLoanOrder = repayPlanComponentService.findRepayPlanVoByLoanOrder(id);
        ModelAndView modelAndView = new ModelAndView("/backend/repayOrder/repayPlan");
        modelAndView.addObject("repayPlan",JSON.toJSONString(repayPlanVoByLoanOrder));
        return modelAndView;
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

	@RequiresPermissions("/backend/loan/repayOrder:list")
    @RequestMapping(value = "list")
    @ResponseBody
    public Map<String,Object> listPage(ServletRequest request) {
        Map<String, Object> map = listSupport(request);
        return map;
    }

    @RequestMapping(value = "detail/{id}", method = RequestMethod.GET)
    @RequiresPermissions("/backend/loan/repayOrder:list")
    public ModelAndView auth(@PathVariable("id") String id) {
        ModelAndView modelAndView = new ModelAndView("/backend/repayOrder/detail");
        RepayOrderMore repayOrder = repayOrderService.findOneBySql(getOneSql(id));
        List<RepayOrderMore> repayOrderMores = repayOrderService.findByRepayPlanId(repayOrder.getRepayPlanId());
        Double amount = new Double(0);
        for(RepayOrderMore repayOrderMore:repayOrderMores){
            amount +=  repayOrderMore.getAmount().doubleValue();;
        }
        modelAndView.addObject("amount",amount);

        Long userId = repayOrder.getUserId();
        User user = userService.findOne(userId);
        IdCardData idcard = idcardService.findByUserId(userId);
        LoanOrderMore loanOrder = repayOrder.getLoanOrder();


        modelAndView.addObject("loanOrder",loanOrder);
        modelAndView.addObject("repayOrder", repayOrder);
        modelAndView.addObject("user", user);
        modelAndView.addObject("idcard", idcard);
        String hyIdcard = Desensitization.idCard(idcard.getIdCard());
        modelAndView.addObject("hyIdcard", hyIdcard);

        //基本信息
        String statu = mapProperties.getRepayOrderStatus(repayOrder.getStatus().toString());
        modelAndView.addObject("status", statu);
        String repayOrderPayType = mapProperties.getRepayOrderPayType(repayOrder.getPayType().toString());
        modelAndView.addObject("payType",repayOrderPayType);

        //资金方
        Fund fund = fundService.findOne(loanOrder.getPayFundId());
        if(fund!=null){
            modelAndView.addObject("fundName",fund.getFundName());
        }

        RepayPlan repayPlan = repayPlanService.findOne(repayOrder.getRepayPlanId());

        //当期/总期termPre
        modelAndView.addObject("termPre", repayPlan.getTerm().toString() + "/" + loanOrder.getLoanTerm());

        //存储信贷系统的出账编号
        modelAndView.addObject("putOutNo", repayPlan.getPutoutno());

        //卡
        CardMore card = cardService.findByUserId(userId);

        //还款计划详情
        List<RepayPlan> byLoanOrderId = repayPlanService.findByLoanOrderId(repayOrder.getLoanOrderId());
        List<OrderPlande> orderPlandes = repayPlanComponentService.findOrderPlandesByRepayPlans(byLoanOrderId);
        modelAndView.addObject("repayPlan",orderPlandes);

        //还款银行卡
        List<UserBankCard> byUserIdAndRepay = repayOrderService.findByUserIdAndRepay(userId);
        StringBuffer bankCardDetail = getBankCardDetail(byUserIdAndRepay);
        String repayCardString = "";
        if(bankCardDetail.length()>0){
            repayCardString = bankCardDetail.substring(0, bankCardDetail.length() - 1);
        }
        modelAndView.addObject("repayCard", repayCardString);

        //收款银行卡
        List<UserBankCard> byUserIdAndPay = repayOrderService.findByUserIdAndPay(userId);
        StringBuffer payCardBuffer = getBankCardDetail(byUserIdAndPay);
        String payCardString = "";
        if(payCardBuffer.length()>0){
            payCardString = payCardBuffer.substring(0, payCardBuffer.length() - 1);
        }
        modelAndView.addObject("payCard", payCardString);

        //地区信息
        //获取对应地址
        List<JSONObject> areaList = new ArrayList<>();
        LocationData authArea = areaService.findByCardIdInAuth(card.getId());
        if (authArea != null) {
            JSONObject jsonObject = (JSONObject) JSON.toJSON(authArea);
            jsonObject.put("segmentName", mapProperties.getSegment(authArea.getSegment()));
            areaList.add(jsonObject);
        }
        LocationData openCardArea = areaService.findByCardIdInOpenCard(card.getId());
        if (openCardArea != null) {
            JSONObject jsonObject = (JSONObject) JSON.toJSON(openCardArea);
            jsonObject.put("segmentName", mapProperties.getSegment(openCardArea.getSegment()));
            areaList.add(jsonObject);
        }
        modelAndView.addObject("areaList",areaList);


        //日期类
        if(loanOrder.getPayLoanTime()!=null){
            modelAndView.addObject("payLoanTime",DateUtils.format(loanOrder.getPayLoanTime(), TIME_FORMAT));
        }
        if(repayPlan.getDueDate()!=null){
            modelAndView.addObject("dueDate",DateUtils.format(repayPlan.getDueDate(),DATE_FORMAT));
        }
        modelAndView.addObject("overdueDays",repayPlan.getOverdueDays());

        //利率
        List<ProductTermFeeFeature> byProductIdAndTerm = productTermFeeFeatureService.findByProductIdAndTerm(loanOrder.getProductId(), loanOrder.getLoanTerm().shortValue());
        if(byProductIdAndTerm.size()!=0){
            modelAndView.addObject("productIdAndTerm",byProductIdAndTerm.get(0));
        }

        return modelAndView;
    }

    private StringBuffer getBankCardDetail(List<UserBankCard> byUserIdAndPay) {
        StringBuffer cardBuffer = new StringBuffer();
        for (UserBankCard userBankCard:byUserIdAndPay){
            String hyCard = Desensitization.bankCardNo(userBankCard.getBankCardNo());
            String bankNameByCode = repayOrderService.findBankNameByCode(userBankCard.getBankCode());
            String bankCardType = mapProperties.getBankCardType(userBankCard.getBankCardType());
            cardBuffer.append(hyCard+"（"+bankNameByCode+bankCardType+"）</br>");
        }
        return cardBuffer;
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
        StringBuffer sbSql = new StringBuffer();
        sbSql.append(getSelectMultiTable().buildSqlString());
        sbSql.append(getService().buildSqlWhereCondition(bulidSearchParamsList(request), SelectMultiTable.MAIN_ALAIS));
        sbSql.append(" GROUP BY " + SelectMultiTable.MAIN_ALAIS + ".repay_plan_id");
        Page<RepayOrderMore> repayOrderMorePage = list(sbSql.toString() + " ORDER BY " + SelectMultiTable.MAIN_ALAIS + ".created_time DESC", pageNo, pageSize, getCountSqls(request), SelectMultiTable.RCOUNT_ALIAS);
        List<RepayOrderMore> content = repayOrderMorePage.getContent();
        List<JSONObject> resultList = new ArrayList<>();
        for (RepayOrderMore repayOrderMore :
                content) {
            try{
                JSONObject jsonObject = handleRepayOrder(repayOrderMore);
                resultList.add(jsonObject);
            }catch (Exception e){
                log.error(String.format("This repayOrder can't be find repayOrderId is [{%s}]",repayOrderMore.getId()),e);
                continue;
            }
        }
        return switchContent(repayOrderMorePage,resultList);

    }

    private JSONObject handleRepayOrder(RepayOrderMore repayOrderMore){
        Map<String, Object> productTermFeeFeatureMap = new HashMap<>();
        JSONObject json = (JSONObject) JSON.toJSON(repayOrderMore);

        //product
        Map loanOrder = (Map) json.get("loanOrder");
        Long productId = (Long) loanOrder.get("productId");
        Integer loanTerm = (Integer) loanOrder.get("loanTerm");

        //productTermFeeFeature
        List<ProductTermFeeFeature> search = new ArrayList<ProductTermFeeFeature>();
        search = productTermFeeFeatureService.findByProductIdAndTerm(productId, loanTerm.shortValue());

        if (search.size() > 0) {
            ProductTermFeeFeature productTermFeeFeature = search.get(0);
            try {
                productTermFeeFeatureMap = ObjectUtils.objectToMap(productTermFeeFeature);
            } catch (Exception e) {
                log.error("productTermFeeFeatureMap can't objectToMap!",e);
            }
        }
        json.put("productTermFeeFeature", productTermFeeFeatureMap);

        //repayPlan
        JSONObject repayPlan = (JSONObject)json.get("repayPlan");

        //实还款日repayDate
        json.put("dueDate",DateUtils.format(repayPlan.getDate("dueDate"),DATE_FORMAT));
        //当期/总期termPre
        json.put("termPre", repayPlan.get("term").toString() + "/" + loanTerm);
        //还款银行卡
        Long userId = json.getLong("userId");
        List<UserBankCard> byUserIdAndRepay = repayOrderService.findByUserIdAndRepay(userId);
        StringBuffer stringBuffer = new StringBuffer();
        for (UserBankCard userBankCard:byUserIdAndRepay){
            String hyCard = Desensitization.bankCardNo(userBankCard.getBankCardNo());
            stringBuffer.append(hyCard+",");
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
        String substring = "";
        if(stringBuffer.length()>0){
            substring = stringBuffer.substring(0, stringBuffer.length() - 1);
        }
        json.put("payCard", substring);
        List<RepayOrderMore> repayOrderMores = repayOrderService.findByRepayPlanId((Long)json.get("repayPlanId"));
        Double amount = new Double(0);
        for(RepayOrderMore repayOrder:repayOrderMores){
            amount +=  repayOrder.getAmount().doubleValue();;
        }
        DecimalFormat df = new DecimalFormat("0.00");
        json.put("amount",df.format(amount));
        json.put("status",mapProperties.getRepayOrderStatus(json.getString("status")));
        return json;
    }
        

    private String getCountSqls(ServletRequest request) {
        StringBuffer sbSql = new StringBuffer();
        String searchSql = getService().buildSqlWhereCondition(bulidSearchParamsList(request), SelectMultiTable.MAIN_ALAIS);
        if(searchSql.equals(getService().WHERE_COMMON)){
            sbSql.append(SelectMultiTable.builder(RepayOrder.class).buildCountSqlString());
        }else{
            sbSql.append(getSelectMultiTable().buildCountSqlStringByDistinct("id"));
        }
        sbSql.append(searchSql);
        sbSql.append(" Group by " + SelectMultiTable.MAIN_ALAIS + ".repay_plan_id )tcount");
        sbSql.insert(0,"select count(tcount." + SelectMultiTable.COUNT_ALIAS + ") AS " + SelectMultiTable.RCOUNT_ALIAS + " from (" );
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
                }).leftJoin(IdCardData.class,"idcard",new OnCondition[]{
                        new OnCondition(SelectMultiTable.ConnectType.AND,"userId",Operator.EQ,"userId")
                }).leftJoin(LoanOrder.class,"loan_order",new OnCondition[]{
                        new OnCondition(SelectMultiTable.ConnectType.AND,"loanOrderId",Operator.EQ,"id")
                }).leftJoin(User.class,"user",new OnCondition[]{
                        new OnCondition(SelectMultiTable.ConnectType.AND,"userId",Operator.EQ,"id")
                });
    }

    protected String getOneSql(Object id){
        String sql = SelectMultiTable.builder(RepayOrder.class)
                .leftJoin(RepayPlan.class,"repayPlan",new OnCondition[]{
                        new OnCondition(SelectMultiTable.ConnectType.AND, "repayPlanId", Operator.EQ, "id"),
                }).leftJoin(IdCardData.class,"idcard",new OnCondition[]{
                        new OnCondition(SelectMultiTable.ConnectType.AND,"userId",Operator.EQ,"userId")
                }).leftJoin(LoanOrder.class,"loanOrder",new OnCondition[]{
                        new OnCondition(SelectMultiTable.ConnectType.AND,"loanOrderId",Operator.EQ,"id")
                }).leftJoin(User.class,"user",new OnCondition[]{
                        new OnCondition(SelectMultiTable.ConnectType.AND,"userId",Operator.EQ,"id")
                }).buildSqlString()+" where "+SelectMultiTable.MAIN_ALAIS+".id = '" + id +"' ";
        return sql;
    }
    /**
     * 下载首期还款数据
     * @param request
     * @param response
     */
    @RequestMapping(value = "downLoadFirstRepayOrder")
    public void downLoadFirstRepayOrder(HttpServletRequest request,HttpServletResponse response){
    	try{
    		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
    		List<String[]> dataList=getDataList(listSupport(request),(obj,rowList)->{
    			rowList.add(obj.getString("userId"));
        		rowList.add(obj.getJSONObject("idCard").getString("name"));
        		rowList.add(obj.getJSONObject("user").get("phone").toString());
        		rowList.add(obj.getJSONObject("loanOrder").get("loanAmount").toString());
        		Date dueDate=obj.getJSONObject("repayPlan").getDate("dueDate");
        		rowList.add(sdf.format(dueDate));  		
        		rowList.add(obj.getDouble("amount").toString());
        		rowList.add(obj.getJSONObject("orderPlande").get("approveFeeAmount").toString());
        		rowList.add(obj.getJSONObject("orderPlande").get("guaranteeFeeAmount").toString());
        		rowList.add(obj.getJSONObject("orderPlande").get("pincipalAmount").toString());
        		rowList.add(obj.getString("fundName"));
        		rowList.add(obj.getString("productName"));
    		});
    	  dataList.add(0, new String[]{"用户ID","客户姓名","注册手机号","贷款金额","首个还款日","首期总费用","评审费","担保费","本息","资金方","产品名称"});
    	  downLoad(request,response, dataList,"首期还款列表.cvs");
    	}catch(Exception e){
    		log.error("",e);
    	}
    }
    /**
     * 下载逾期还款数据
     * @param request
     * @param response
     */
    @RequestMapping(value = "downLoadPastRepayOrder")
    public void downLoadPastRepayOrder(HttpServletRequest request,HttpServletResponse response){
    	try{
    		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
    		List<String[]> dataList=getDataList(listSupport(request),(obj,rowList)->{
    			rowList.add(obj.getString("userId"));
        		rowList.add(obj.getJSONObject("idCard").getString("name"));
        		rowList.add(obj.getJSONObject("user").get("phone").toString());
        		rowList.add(obj.getJSONObject("loanOrder").get("loanAmount").toString());
        		Date dueDate=obj.getJSONObject("repayPlan").getDate("dueDate");
        		rowList.add(sdf.format(dueDate));  	
        		rowList.add(obj.getJSONObject("repayPlan").get("overdueDays").toString());
        		rowList.add(obj.getString("termPre"));
        		rowList.add(obj.getString("fundName"));
        		rowList.add(obj.getString("productName"));
    		});
    	  dataList.add(0, new String[]{"用户ID","客户姓名","注册手机号","贷款金额","还款日","逾期天数","当期/总期","应还金额","资金方","产品名称"});
    	  downLoad(request,response, dataList,"逾期还款列表.cvs");
    	}catch(Exception e){
    		log.error("",e);
    	}
    }
	private void downLoad(HttpServletRequest request,HttpServletResponse response, List<String[]> dataList,String fileName) throws IOException {
		try{
			String userAgent = request.getHeader("User-Agent");      
		    // 针对IE或者以IE为内核的浏览器：  
		    if (userAgent.contains("MSIE") || userAgent.contains("Trident")) {  
		    	fileName = java.net.URLEncoder.encode(fileName, "UTF-8");  
		    } else {  
		        // 非IE浏览器的处理：  
		    	fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");  
		    }
           response.setCharacterEncoding("utf-8");
		  response.setHeader("content-type", "application/octet-stream");
		  response.setContentType("application/octet-stream");
		 response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", fileName));  
		 write(response.getOutputStream(),dataList, fileName);
		}catch(Exception e){
			log.error("",e);
		}finally {
			response.getOutputStream().flush();
			response.getOutputStream().close();
		}
		  
	}
    
    private List<String[]> getDataList(Map<String,Object> map,DownLoadProcesser dlp){
    	List<String[]> dataList=new ArrayList<>();
    	List<JSONObject> repayDataList= (List<JSONObject>) map.get("content");
    	List<String> rowList=null;
    	for(JSONObject obj:repayDataList){
    		rowList=new ArrayList<>();
    		dlp.process(obj,rowList);
    		dataList.add(rowList.toArray(new String[rowList.size()]));
    	}
    	return dataList;
    }
    private interface DownLoadProcesser{
    	 void process(JSONObject obj,List<String> rowList);
    }
    private void write(OutputStream os,List<String[]> dataList,String fileName){
    	CsvWriter writer=new CsvWriter(os, ',', Charset.forName("UTF-8"));
    	dataList.forEach(data->{
    		try {
				writer.writeRecord(data);
			} catch (Exception e) {
				log.error("",e);
			}
    	});
    	writer.close();
    }
}
