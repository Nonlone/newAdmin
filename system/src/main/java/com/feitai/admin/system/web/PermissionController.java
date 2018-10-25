package com.feitai.admin.system.web;

import com.feitai.admin.core.annotation.LogAnnotation;
import com.feitai.admin.core.service.DynamitSupportService;
import com.feitai.admin.core.service.Page;
import com.feitai.admin.core.vo.ListItem;
import com.feitai.admin.core.web.BaseListableController;
import com.feitai.admin.system.model.Permission;
import com.feitai.admin.system.service.PermissionService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/system/permission")
@Slf4j
public class PermissionController  extends BaseListableController<Permission> {

	@Autowired
	private PermissionService permissionService;

	@RequiresUser
	@RequestMapping(value = "")
	public String index() {
		return "/system/permission/index";
	}
	
	@RequiresPermissions("/system/permission:list")
	@RequestMapping(value = "list")
	@ResponseBody
	@LogAnnotation(value = true, writeRespBody = false)// 写日志但是不打印请求的params,但不打印ResponseBody的内容
	public Object listPage(ServletRequest request) {
		Page<Permission> listPage = super.list(request);
		return listPage;
	}
	
	@RequiresPermissions(value = {"/system/resource:update","/system/role:update"}, logical= Logical.OR)
	@RequestMapping(value = "permissionCheckList")
	@ResponseBody
	@LogAnnotation(value = true, writeRespBody = false)// 写日志但是不打印请求的params,但不打印ResponseBody的内容
	public Object permissionCheckList(){
		List<Permission> permissions = this.permissionService.findAll();
		List<ListItem> list = new ArrayList<ListItem>();
		for(Permission permission : permissions){
			list.add(new ListItem(permission.getName(), permission.getId().toString()));
		}
		return list;
	}
	
	// 特别设定多个ReuireRoles之间为Or关系，而不是默认的And.
	@RequiresPermissions("/system/permission:update")
	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	@ResponseBody
	public Object editFrom(@PathVariable("id") Long id) {
		Permission permission = this.permissionService.findOne(id);
		return permission;
	}
	
	@RequiresPermissions("/system/permission:add")
	@RequestMapping(value = "add", method = RequestMethod.POST)
	@ResponseBody
	public Object add(@Valid Permission permission){
		this.permissionService.save(permission);
		return successResult;
	}
	
	@RequiresPermissions("/system/permission:update")
	@RequestMapping(value = "update", method = RequestMethod.POST)
	@ResponseBody
	public Object update(@Valid @ModelAttribute("permission") Permission permission){
		this.permissionService.save(permission);
		return successResult;
	}
	
	@RequiresPermissions("/system/permission:del")
	@RequestMapping(value = "del")
	@ResponseBody
	public Object del(@RequestParam(value = "ids[]") Long[] ids){
		this.permissionService.delete(ids);
		return successResult;
	}
	

	/**
	 * 所有RequestMapping方法调用前的Model准备方法, 实现Struts2 Preparable二次部分绑定的效果,先根据form的id从数据库查出User对象,再把Form提交的内容绑定到该对象上。
	 * 因为仅update()方法的form中有id属性，因此仅在update时实际执行.
	 */
	@ModelAttribute
	public void getPermission(@RequestParam(value = "id", defaultValue = "-1") Long id, Model model) {
		if (id != -1) {
			model.addAttribute("permission", this.permissionService.findOne(id));
		}
	}

	@Override
	protected DynamitSupportService<Permission> getService() {
		return this.permissionService;
	}

	@Override
	protected String getSql() {
		return null;
	}


	@InitBinder
	public void initDate(WebDataBinder webDataBinder){
		webDataBinder.addCustomFormatter(new DateFormatter("yyyy-MM-dd HH:mm:ss"));
		webDataBinder.addCustomFormatter(new DateFormatter("yyyy-MM-dd"));
	}

}
