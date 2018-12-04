/**
 * @author 
 * @version 1.0
 * @since  2018-08-10 13:12:45
 * @desc 方案相关期数配置
 */

package com.feitai.admin.backend.product.web;


import com.feitai.admin.backend.product.entity.RatePlanTermMore;
import com.feitai.admin.backend.product.service.RatePlanTermService;
import com.feitai.admin.core.service.DynamitSupportService;
import com.feitai.admin.core.web.BaseCrudController;
import com.feitai.admin.core.web.BaseListableController;
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
@RequestMapping(value = "/backend/ratePlanTerm")
public class RatePlanTermController extends BaseCrudController<RatePlanTermMore> {
	@Autowired
	private RatePlanTermService ratePlanTermService;
	
	@RequestMapping(value = "")
	public String index() {
		return "/backend/ratePlanTerm/index";
	}
	
/*	@RequiresPermissions("/backend/ratePlanTerm:list")
	@RequestMapping(value = "list")
	@ResponseBody
	public Object listPage(ServletRequest request) {
		return super.list(request);
	}
	
	@RequiresPermissions("/backend/ratePlanTerm:update")
	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	@ResponseBody
	public Object editFrom(@PathVariable("id") Long id) {
		RatePlanTermMore ratePlanTerm = this.ratePlanTermService.findOne(id);
		return ratePlanTerm;
	}
	
	@RequiresPermissions("/backend/ratePlanTerm:add")
	@RequestMapping(value = "add", method = RequestMethod.POST)
	@ResponseBody
	public Object add(@Valid RatePlanTermMore ratePlanTerm){
		this.ratePlanTermService.save(ratePlanTerm);
		return successResult;
	}
	
	@RequiresPermissions("/backend/ratePlanTerm:update")
	@RequestMapping(value = "update", method = RequestMethod.POST)
	@ResponseBody
	public Object update(@Valid @ModelAttribute("ratePlanTerm") RatePlanTermMore ratePlanTerm){
		this.ratePlanTermService.save(ratePlanTerm);
		return successResult;
	}
	
	@RequiresPermissions("/backend/ratePlanTerm:del")
	@RequestMapping(value = "del")
	@ResponseBody
	public Object del(@RequestParam(value = "ids[]") Long[] ids){
		this.ratePlanTermService.delete(ids);
		return successResult;
	}
	

	*//**
	 * 所有RequestMapping方法调用前的Model准备方法, 实现Struts2 Preparable二次部分绑定的效果,先根据form的id从数据库查出User对象,再把Form提交的内容绑定到该对象上。
	 * 因为仅update()方法的form中有id属性，因此仅在update时实际执行.
	 *//*
	@ModelAttribute
	public void getratePlanTerm(@RequestParam(value = "id", defaultValue = "-1") Long id, Model model) {
		if (id != -1) {
			model.addAttribute("ratePlanTerm", this.ratePlanTermService.findOne(id));
		}
	}*/

	@Override
	protected DynamitSupportService<RatePlanTermMore> getService() {
		return this.ratePlanTermService;
	}

}
