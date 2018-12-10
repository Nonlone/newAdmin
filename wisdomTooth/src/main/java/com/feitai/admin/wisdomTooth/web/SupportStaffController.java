/**
 * @author 
 * @version 1.0
 * @since  2018-08-10 15:17:49
 * @desc SupportStaff
 */

package com.feitai.admin.wisdomTooth.web;

import com.feitai.admin.core.service.DynamitSupportService;
import com.feitai.admin.core.service.Page;
import com.feitai.admin.core.web.BaseListableController;
import com.feitai.admin.wisdomTooth.model.SupportStaff;
import com.feitai.admin.wisdomTooth.service.SupportStaffService;
import com.feitai.utils.SnowFlakeIdGenerator;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import javax.validation.Valid;


@Controller
@RequestMapping(value = "/wisdomTooth/supportStaff")
@Slf4j
public class SupportStaffController extends BaseListableController<SupportStaff> {
	@Autowired
	private SupportStaffService supportStaffService;
	
	@RequestMapping(value = "")
	public String index() {
		return "/wisdomTooth/supportStaff/index";
	}
	
	@RequiresPermissions("/wisdomTooth/supportStaff:list")
	@RequestMapping(value = "list")
	@ResponseBody
	public Object listPage(ServletRequest request) {
		final Page<SupportStaff> listPage = super.list(request);
		return listPage;
	}
	
	@RequiresPermissions("/wisdomTooth/supportStaff:update")
	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	@ResponseBody
	public Object editFrom(@PathVariable("id") Long id) {
		SupportStaff supportStaff = this.supportStaffService.findOne(id);
		return supportStaff;
	}
	
	@RequiresPermissions("/wisdomTooth/supportStaff:add")
	@RequestMapping(value = "add", method = RequestMethod.POST)
	@ResponseBody
	public Object add(@Valid SupportStaff supportStaff){
		supportStaff.setId(SnowFlakeIdGenerator.getDefaultNextId());
		this.supportStaffService.save(supportStaff);
		return BaseListableController.successResult;
	}
	
	@RequiresPermissions("/wisdomTooth/supportStaff:update")
	@RequestMapping(value = "update", method = RequestMethod.POST)
	@ResponseBody
	public Object update(@Valid @ModelAttribute("supportStaff") SupportStaff supportStaff){
		this.supportStaffService.save(supportStaff);
		return BaseListableController.successResult;
	}
	
	@RequiresPermissions("/wisdomTooth/supportStaff:del")
	@RequestMapping(value = "del")
	@ResponseBody
	public Object del(@RequestParam(value = "ids[]") Long[] ids){
		this.supportStaffService.delete(ids);
		return BaseListableController.successResult;
	}
	

	/**
	 * 所有RequestMapping方法调用前的Model准备方法, 实现Struts2 Preparable二次部分绑定的效果,先根据form的id从数据库查出User对象,再把Form提交的内容绑定到该对象上。
	 * 因为仅update()方法的form中有id属性，因此仅在update时实际执行.
	 */
	@ModelAttribute
	public void getsupportStaff(@RequestParam(value = "id", defaultValue = "-1") Long id, Model model) {
		if (id != -1) {
			model.addAttribute("supportStaff", this.supportStaffService.findOne(id));
		}
	}

	@Override
	protected DynamitSupportService<SupportStaff> getService() {
		return this.supportStaffService;
	}
}
