/**
 * @author 
 * @version 1.0
 * @since  2018-07-12 10:44:26
 * @desc AppVersion
 */

package com.feitai.admin.backend.config.web;

import com.feitai.admin.backend.config.service.AppVersionService;
import com.feitai.admin.core.service.DynamitSupportService;
import com.feitai.admin.core.service.Page;
import com.feitai.admin.core.web.BaseListableController;
import com.feitai.jieya.server.dao.appconfig.model.AppVersion;
import com.feitai.utils.SnowFlakeIdGenerator;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import javax.validation.Valid;


@Controller
@RequestMapping(value = "/admin/config/appVersion")
@Slf4j
public class AppVersionController extends BaseListableController<AppVersion> {

	@Autowired
	private AppVersionService appVersionService;
	
	@RequestMapping(value = "")
	public String index() {
		return "/admin/config/appVersion/index";
	}
	
	@RequiresPermissions("/admin/config/appVersion:list")
	@RequestMapping(value = "list")
	@ResponseBody
	public Object listPage(ServletRequest request) {
		Page<AppVersion> listPage = super.list(request);
		return listPage;
	}
	
	@RequiresPermissions("/admin/config/appVersion:update")
	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	@ResponseBody
	public Object editFrom(@PathVariable("id") Long id) {
		AppVersion appVersion = this.appVersionService.findOne(id);
		return appVersion;
	}
	
	@RequiresPermissions("/admin/config/appVersion:add")
	@RequestMapping(value = "add", method = RequestMethod.POST)
	@ResponseBody
	public Object add(@Valid AppVersion appVersion){
		appVersion.setId(SnowFlakeIdGenerator.getDefaultNextId());
		this.appVersionService.save(appVersion);
		return BaseListableController.successResult;
	}
	
	@RequiresPermissions("/admin/config/appVersion:update")
	@RequestMapping(value = "update", method = RequestMethod.POST)
	@ResponseBody
	public Object update(@Valid @ModelAttribute("appVersion") AppVersion appVersion){
		this.appVersionService.save(appVersion);
		return BaseListableController.successResult;
	}
	
	@RequiresPermissions("/admin/config/appVersion:del")
	@RequestMapping(value = "del")
	@ResponseBody
	public Object del(@RequestParam(value = "ids[]") Long[] ids){
		this.appVersionService.delete(ids);
		return BaseListableController.successResult;
	}
	

	/**
	 * 所有RequestMapping方法调用前的Model准备方法, 实现Struts2 Preparable二次部分绑定的效果,先根据form的id从数据库查出User对象,再把Form提交的内容绑定到该对象上。
	 * 因为仅update()方法的form中有id属性，因此仅在update时实际执行.
	 */
	@ModelAttribute
	public void getappVersion(@RequestParam(value = "id", defaultValue = "-1") Long id, Model model) {
		if (id != -1) {
			model.addAttribute("appVersion", this.appVersionService.findOne(id));
		}
	}

	@Override
	protected DynamitSupportService<AppVersion> getService() {
		return this.appVersionService;
	}

	protected String getSingleSql(String typeCode){
		return null;
	}

	@InitBinder
	public void initDate(WebDataBinder webDataBinder){
		webDataBinder.addCustomFormatter(new DateFormatter("yyyy-MM-dd HH:mm:ss"));
		webDataBinder.addCustomFormatter(new DateFormatter("yyyy-MM-dd"));
	}
}
