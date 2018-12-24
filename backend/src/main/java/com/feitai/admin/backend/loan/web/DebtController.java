package com.feitai.admin.backend.loan.web;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.csvreader.CsvWriter;
import com.feitai.admin.backend.fund.service.FundService;
import com.feitai.admin.backend.loan.entity.Debt;
import com.feitai.admin.backend.loan.service.DebtService;
import com.feitai.admin.backend.loan.service.RepayPlanComponentService;
import com.feitai.admin.backend.loan.service.RepayPlanService;
import com.feitai.admin.backend.loan.vo.OrderPlande;
import com.feitai.admin.backend.product.service.ProductService;
import com.feitai.admin.core.service.DynamitSupportService;
import com.feitai.admin.core.service.OnCondition;
import com.feitai.admin.core.service.Operator;
import com.feitai.admin.core.service.Page;
import com.feitai.admin.core.service.SelectMultiTable;
import com.feitai.admin.core.vo.ListItem;
import com.feitai.admin.core.web.BaseListableController;
import com.feitai.admin.core.web.PageBulider;
import com.feitai.jieya.server.dao.data.model.IdCardData;
import com.feitai.jieya.server.dao.fund.model.Fund;
import com.feitai.jieya.server.dao.loan.model.LoanOrder;
import com.feitai.jieya.server.dao.loan.model.RepayPlan;
import com.feitai.jieya.server.dao.product.model.Product;
import com.feitai.jieya.server.dao.user.model.User;
import com.feitai.utils.datetime.DateUtils;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(value = "/backend/loan/debt")
@Slf4j
public class DebtController extends BaseListableController<Debt>{

	@Autowired
	private DebtService debtDervice;
	

    @Autowired
    private RepayPlanComponentService repayPlanComponentService;

    
    @Autowired
    private FundService fundService;



    @Autowired
    private ProductService productService;
    
    private final static String DATE_FORMAT = "yyyy-MM-dd";
    
    private final static String DUE_DATE_FORMAT_PARAM="maintable.due_date_format";
    
    private final static String DUE_DATE_FORMAT="DATE_FORMAT(maintable.due_date,'%m-%d')";
    
    

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
			list.add(new ListItem(product.getRemark(), product.getId().toString()));
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
    
    
    @RequiresPermissions("/backend/loan/debt:list")
    @RequestMapping(value = "list")
    @ResponseBody
    public Map<String,Object> listPage(ServletRequest request) {
        Map<String, Object> map = listSupport(request);
        return map;
    }
    
    public Map<String,Object> listSupport(ServletRequest request){
        //根据request获取page
        int pageNo = PageBulider.getPageNo(request);
        int pageSize = PageBulider.getPageSize(request);
        StringBuffer sbSql = new StringBuffer();
        sbSql.append(getSelectMultiTable().buildSqlString());
        sbSql.append(getService().buildSqlWhereCondition(bulidSearchParamsList(request), SelectMultiTable.MAIN_ALAIS));
        String sql=sbSql.toString() + " ORDER BY " + SelectMultiTable.MAIN_ALAIS + ".due_date,"+SelectMultiTable.MAIN_ALAIS+".id";
        String countSql=getCountSqls(request);
        countSql=countSql.replace(DUE_DATE_FORMAT_PARAM, DUE_DATE_FORMAT);
        sql=sql.replace(DUE_DATE_FORMAT_PARAM, DUE_DATE_FORMAT);
        Page<Debt> debtPage = list(sql, pageNo, pageSize,countSql , SelectMultiTable.COUNT_ALIAS);
        List<Debt> content = debtPage.getContent();
        List<JSONObject> resultList = new ArrayList<>();
        for (Debt debt :content) {
            try{
                JSONObject jsonObject = handleDebtOrder(debt);
                resultList.add(jsonObject);
            }catch (Exception e){
                log.error("This debt has error",e);
                continue;
            }
        }
        return switchContent(debtPage,resultList);

    }
    
    private JSONObject handleDebtOrder(Debt debt){
        JSONObject json = (JSONObject) JSON.toJSON(debt);
        Product product=productService.findOne(debt.getLoanOrder().getProductId());
        json.put("product", product);
        Integer loanTerm = debt.getLoanOrder().getLoanTerm();

        OrderPlande orderPlande=repayPlanComponentService.getOrderPlande(debt.getLoanOrderId(), debt.getId());      
        json.put("orderPlande", orderPlande);
        
        //实还款日repayDate
        json.put("dueDate",DateUtils.format(debt.getDueDate(),DATE_FORMAT));
        //当期/总期termPre
        json.put("termPre", debt.getTerm().toString() + "/" + loanTerm+" ");
        //还款银行卡
        Fund fund=fundService.getFund(debt.getLoanOrder().getPayFundId());
        json.put("fundName", Optional.ofNullable(fund).map(f ->f.getFundName()).orElse(""));
        return json;
    }
    
    
    public List<JSONObject>  downSupport(ServletRequest request){
        //根据request获取page
        StringBuffer sbSql = new StringBuffer();
        sbSql.append(getSelectMultiTable().buildSqlString())
        .append(getService().buildSqlWhereCondition(bulidSearchParamsList(request), SelectMultiTable.MAIN_ALAIS))
        .append(" ORDER BY ")
        .append(SelectMultiTable.MAIN_ALAIS).append(".due_date ,").append(SelectMultiTable.MAIN_ALAIS).append(".id");
        String sql=sbSql.toString().replace(DUE_DATE_FORMAT_PARAM, DUE_DATE_FORMAT);
        List<Debt> debtList= getService().findAll(sql);
        List<JSONObject> resultList = new ArrayList<>();
        for (Debt debt :debtList) {
            try{
                JSONObject jsonObject = handleDebtOrder(debt);
                resultList.add(jsonObject);
            }catch (Exception e){
                log.error("This debt has error",e);
                continue;
            }
        }
        return resultList;

    }
    
    
    private SelectMultiTable getSelectMultiTable() {
        return  SelectMultiTable.builder(RepayPlan.class)
                .leftJoin(IdCardData.class,"idcard",new OnCondition[]{
                        new OnCondition(SelectMultiTable.ConnectType.AND,"userId",Operator.EQ,"userId")
                }).leftJoin(LoanOrder.class,"loan_order",new OnCondition[]{
                        new OnCondition(SelectMultiTable.ConnectType.AND,"loanOrderId",Operator.EQ,"id")
                }).leftJoin(User.class,"user",new OnCondition[]{
                        new OnCondition(SelectMultiTable.ConnectType.AND,"userId",Operator.EQ,"id")
                });
    }
    private String getCountSqls(ServletRequest request) {
        StringBuffer sbSql = new StringBuffer();
        String searchSql = getService().buildSqlWhereCondition(bulidSearchParamsList(request), SelectMultiTable.MAIN_ALAIS);
        if(searchSql.equals(getService().WHERE_COMMON)){
            sbSql.append(SelectMultiTable.builder(RepayPlan.class).buildCountSqlString());
        }else{
            sbSql.append(getSelectMultiTable().buildCountSqlString());
        }
        sbSql.append(searchSql);
        return sbSql.toString();
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
    		List<String[]> dataList=getDataList(downSupport(request),(obj,rowList)->{
    			try{
    			rowList.add(obj.getString("userId")+"\t");
        		rowList.add(obj.getJSONObject("idcard").getString("name"));
        		rowList.add(obj.getJSONObject("user").get("phone").toString()+"\t");
        		rowList.add(obj.getJSONObject("loanOrder").get("loanAmount").toString());
        		Date dueDate=obj.getDate("dueDate");
        		rowList.add(sdf.format(dueDate)+"\t"); 
        		rowList.add(obj.get("orderTerm").toString());
        		rowList.add(obj.getDouble("amount").toString());
        		rowList.add(obj.getJSONObject("orderPlande").get("approveFeeAmount").toString());
        		rowList.add(obj.getJSONObject("orderPlande").get("guaranteeFeeAmount").toString());
        		Double pincipalAmount=(Double) obj.getJSONObject("orderPlande").get("pincipalAmount");
        		Double interestAmount= (Double) obj.getJSONObject("orderPlande").get("interestAmount");
        		Double amount=pincipalAmount;
        		if(interestAmount!=null){
        			amount+=interestAmount;
        		}
        		rowList.add(amount.toString());
        		rowList.add(obj.getString("fundName"));
        		rowList.add(obj.getJSONObject("product").getString("name"));
    			}catch(Exception e){
    				log.error("downLoadFirstRepayOrder has errer",e);
    			}
    		});
    	  dataList.add(0, new String[]{"客户ID","客户姓名","注册手机号","贷款金额","首个还款日","总期数","首期总费用","评审费","担保费","本息","资金方","产品名称"});
    	  downLoad(request,response, dataList,"首期还款列表.csv");
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
    		List<String[]> dataList=getDataList(downSupport(request),(obj,rowList)->{
    			try{
    			rowList.add(obj.getString("userId")+"\t");
        		rowList.add(obj.getJSONObject("idcard").getString("name"));
        		rowList.add(obj.getJSONObject("user").get("phone").toString()+"\t");
        		rowList.add(obj.getJSONObject("loanOrder").get("loanAmount").toString());
        		Date dueDate=obj.getDate("dueDate");
        		rowList.add(sdf.format(dueDate)+"\t");  	
        		rowList.add(obj.get("overdueDays").toString());
        		rowList.add(obj.getString("termPre")+"\t");
        		rowList.add(obj.get("amount").toString());
        		rowList.add(obj.get("balanceAmount").toString());
        		rowList.add(obj.getString("fundName"));
        		rowList.add(obj.getJSONObject("product").getString("name"));
    			}catch(Exception e){
    				log.error("downLoadPastRepayOrder has errer",e);
    			}
    		});
    	  dataList.add(0, new String[]{"客户ID","客户姓名","注册手机号","贷款金额","还款日","逾期天数","当期/总期","应还金额","逾期金额","资金方","产品名称"});
    	  downLoad(request,response, dataList,"逾期还款列表.csv");
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
          // response.setCharacterEncoding("utf-8");
		  //response.setHeader("content-type", "application/octet-stream");
		  response.setContentType("application/csv");
		 response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", fileName));  
		 byte[] uft8bom={(byte)0xef,(byte)0xbb,(byte)0xbf};
		 response.getOutputStream().write(uft8bom);
		 write(response.getOutputStream(),dataList, fileName);
		}catch(Exception e){
			log.error("",e);
		}finally {
			response.getOutputStream().flush();
			response.getOutputStream().close();
		}
		  
	}
    
    private List<String[]> getDataList(List<JSONObject> resultList,DownLoadProcesser dlp){
    	List<String[]> dataList=new ArrayList<>();
    	List<String> rowList=null;
    	for(JSONObject obj:resultList){
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
				writer.writeRecord(data,true);
			} catch (Exception e) {
				log.error("",e);
			}
    	});
    	writer.close();
    }
    
	@Override
	protected DynamitSupportService<Debt> getService() {
		
		return debtDervice;
	}
   
}
