/**
 * @author 
 * @version 1.0
 * @since  2018-08-02 11:40:20
 * @desc AppConfigType
 */

package com.feitai.admin.backend.config.web;

import com.feitai.admin.backend.config.entity.AppConfigType;
import com.feitai.admin.backend.config.service.AppConfigTypeService;
import com.feitai.admin.core.annotation.LogAnnotation;
import com.feitai.admin.core.service.*;
import com.feitai.admin.core.vo.ListItem;
import com.feitai.admin.core.web.BaseListableController;
import com.feitai.admin.core.web.PageBulider;
import com.feitai.jieya.server.dao.appconfig.model.AppConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;


@Controller
@RequestMapping(value = "/backend/appConfigType")
@Slf4j
public class AppConfigTypeController extends BaseListableController<AppConfigType> {

	@Autowired
	private AppConfigTypeService appConfigTypeService;

	private static final String TYPE_CODE = "typeCode";
	
	@RequestMapping(value = "index")
	public String index() {
		return "/backend/appConfigType/index";
	}
	
	@RequiresPermissions("/backend/appConfigType:list")
	@RequestMapping(value = "list")
	@ResponseBody
	public Object listPage(ServletRequest request) {
		int pageNo = PageBulider.getPageNo(request);
        int pageSize = PageBulider.getPageSize(request);
        String sql = getSql(request, getSelectMultiTable());  
		Page<AppConfigType> listPage =list(sql, pageNo, pageSize, getCountSqls(request), SelectMultiTable.COUNT_ALIAS);//listBySql(request,getSql(request));
		return listPage;
	}
	
	@RequiresPermissions("/backend/appConfigType:list")
	@RequestMapping(value = "listAll")
	@ResponseBody
	@LogAnnotation(value = true, writeRespBody = false)// 写日志但是不打印请求的params,但不打印ResponseBody的内容
	public Object listAll(){
		String sql=getSelectMultiTable().buildSqlString()+" GROUP BY type_code";
		List<ListItem> list = this.appConfigTypeService.findAllItems(sql);
		return list;
	}
	
	@RequiresPermissions("/backend/appConfigType:update")
	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	@ResponseBody
	public Object editFrom(@PathVariable("typeCode") String typeCode) {
		String singleSql = getSingleSql(typeCode);
		AppConfigType appConfigType = this.appConfigTypeService.findOneBySql(singleSql);
		return appConfigType;
	}

	@RequiresPermissions("/backend/appConfigType:add")
	@RequestMapping(value = "add", method = RequestMethod.POST)
	@ResponseBody
	public Object add(HttpServletRequest request,HttpServletResponse response){
		String typeCode = (String) request.getParameter(TYPE_CODE);
		String name = (String) request.getParameter("name");
		String remark = (String) request.getParameter("remark");
		AppConfigType appConfigType = new AppConfigType();
		appConfigType.setTypeCode(typeCode);
		appConfigType.setName(name);
		appConfigType.setRemark(remark);
		appConfigType.setCreatedTime(new Date());
		appConfigType.setUpdateTime(new Date());
		this.appConfigTypeService.save(appConfigType);
		return BaseListableController.successResult;
	}

	@RequiresPermissions("/backend/appConfigType:update")
	@RequestMapping(value = "update", method = RequestMethod.POST)
	@ResponseBody
	public Object update(HttpServletRequest request,HttpServletResponse response){
		String typeCode = (String) request.getParameter(TYPE_CODE);
		String name = (String) request.getParameter("name");
		String remark = (String) request.getParameter("remark");
		AppConfigType appConfigType = appConfigTypeService.findOne(typeCode);
		appConfigType.setTypeCode(typeCode);
		appConfigType.setName(name);
		appConfigType.setRemark(remark);
		appConfigType.setUpdateTime(new Date());
		this.appConfigTypeService.save(appConfigType);
		return BaseListableController.successResult;
	}

	@RequiresPermissions("/backend/appConfigType:del")
	@RequestMapping(value = "del")
	@ResponseBody
	public Object del(@RequestParam(value = "typeCodes[]") String[] ids){
		this.appConfigTypeService.delete(ids);
		return BaseListableController.successResult;
	}


	/**
	 * 所有RequestMapping方法调用前的Model准备方法, 实现Struts2 Preparable二次部分绑定的效果,先根据form的id从数据库查出User对象,再把Form提交的内容绑定到该对象上。
	 * 因为仅update()方法的form中有id属性，因此仅在update时实际执行.
	 */
	@ModelAttribute
	public void getappConfigType(@RequestParam(value = "id", defaultValue = "-1") String id, Model model) {
		if (!id.equals("-1")) {
			model.addAttribute("appConfigType", this.appConfigTypeService.findOneBySql(getSingleSql(id)));
		}
	}

	@Override
	protected DynamitSupportService<AppConfigType> getService() {
		return this.appConfigTypeService;
	}


	@Override
	protected String getSql(ServletRequest request, SelectMultiTable selectMultiTable) {
	    	StringBuffer sbSql = new StringBuffer();
	        sbSql.append(selectMultiTable.buildSqlString());
	        List<SearchParams> searchParamsList = bulidSearchParamsList(request);
	        sbSql.append(getService().buildSqlWhereCondition(searchParamsList, SelectMultiTable.MAIN_ALAIS));
	        sbSql.append(" GROUP BY type_code");
	        return sbSql.toString();
	}

	protected String getSingleSql(String typeCode){
		String sql = SelectMultiTable.builder(AppConfigType.class)
				.leftJoin(AppConfig.class,"app_config",new OnCondition[]{
						new OnCondition(SelectMultiTable.ConnectType.AND, TYPE_CODE, Operator.EQ, TYPE_CODE),
				}).buildSqlString()+"where maintable.type_Code = '"+typeCode+"' GROUP BY type_code";
		return sql;
	}
	  private SelectMultiTable getSelectMultiTable() {
	        return SelectMultiTable.builder(AppConfigType.class)
					.leftJoin(AppConfig.class,"app_config",new OnCondition[]{
							new OnCondition(SelectMultiTable.ConnectType.AND, TYPE_CODE, Operator.EQ, TYPE_CODE),
					});
	    }
	  
	   

	    private String getCountSqls(ServletRequest request) {
	        StringBuffer sbSql = new StringBuffer();
	        String searchSql = getService().buildSqlWhereCondition(bulidSearchParamsList(request), SelectMultiTable.MAIN_ALAIS);
	        if(searchSql.equals(getService().WHERE_COMMON)){
	            sbSql.append(SelectMultiTable.builder(AppConfigType.class).buildCountSqlString());
	        }else{
	            sbSql.append(getSelectMultiTable().buildCountSqlString());
	        }
	        sbSql.append(searchSql);
	        return sbSql.toString();
	    }
}
