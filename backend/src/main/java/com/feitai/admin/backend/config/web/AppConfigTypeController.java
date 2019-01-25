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
import com.feitai.admin.core.web.BaseCrudController;
import com.feitai.admin.core.web.BaseListableController;
import com.feitai.admin.core.web.PageBulider;
import com.feitai.jieya.server.dao.appconfig.model.AppConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.servlet.ServletRequest;
import java.util.List;


@Controller
@RequestMapping(value = "/backend/appConfigType")
@Slf4j
public class AppConfigTypeController extends BaseCrudController<AppConfigType> {

	@Autowired
	private AppConfigTypeService appConfigTypeService;

	private static final String TYPE_CODE = "typeCode";
	
	@RequestMapping(value = "index")
	@RequiresPermissions("/backend/appConfigType:list")
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
	@RequestMapping(value = "update/{typeCode}", method = RequestMethod.GET)
	@ResponseBody
	public Object editFrom(@PathVariable("typeCode") String typeCode) {
		String singleSql = getSingleSql(typeCode);
		AppConfigType appConfigType = this.appConfigTypeService.findOneBySql(singleSql);
		return appConfigType;
	}

	@RequiresPermissions("/backend/appConfigType:del")
	@RequestMapping(value = "delModel")
	@ResponseBody
	public Object delModel(@RequestParam(value = "typeCodes[]") String[] ids){
		this.appConfigTypeService.delete(ids);
		return BaseListableController.successResult;
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
