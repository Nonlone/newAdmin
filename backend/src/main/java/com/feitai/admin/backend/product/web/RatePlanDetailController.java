/**
 * @author 
 * @version 1.0
 * @since  2018-08-10 13:12:44
 * @desc 科目明细
 */

package com.feitai.admin.backend.product.web;


import com.feitai.admin.backend.product.service.RatePlanDetailService;
import com.feitai.admin.core.service.DynamitSupportService;
import com.feitai.admin.core.web.BaseListableController;
import com.feitai.jieya.server.dao.rateplan.model.RatePlanDetail;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import javax.validation.Valid;


@Controller
@RequestMapping(value = "/backend/ratePlanDetail")
public class RatePlanDetailController extends BaseListableController<RatePlanDetail> {

	@Autowired
	private RatePlanDetailService ratePlanDetailService;

	@RequiresPermissions("/backend/ratePlanDetail:list")
	@RequestMapping(value = "index")
	public String index() {
		return "/backend/ratePlanDetail/index";
	}
	
	@RequiresPermissions("/backend/ratePlanDetail:list")
	@RequestMapping(value = "list")
	@ResponseBody
	public Object listPage(ServletRequest request) {
		return super.list(request);
	}
	
	@RequiresPermissions("/backend/ratePlanDetail:update")
	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	@ResponseBody
	public Object editFrom(@PathVariable("id") Long id) {
		RatePlanDetail ratePlanDetail = this.ratePlanDetailService.findOne(id);
		return ratePlanDetail;
	}
	
	@RequiresPermissions("/backend/ratePlanDetail:add")
	@RequestMapping(value = "add", method = RequestMethod.POST)
	@ResponseBody
	public Object add(@Valid RatePlanDetail ratePlanDetail){
		this.ratePlanDetailService.save(ratePlanDetail);
		return successResult;
	}
	
	@RequiresPermissions("/backend/ratePlanDetail:update")
	@RequestMapping(value = "update", method = RequestMethod.POST)
	@ResponseBody
	public Object update(@Valid @ModelAttribute("ratePlanDetail") RatePlanDetail ratePlanDetail){
		this.ratePlanDetailService.save(ratePlanDetail);
		return successResult;
	}
	
	@RequiresPermissions("/backend/ratePlanDetail:del")
	@RequestMapping(value = "del")
	@ResponseBody
	public Object del(@RequestParam(value = "ids[]") Long[] ids){
		this.ratePlanDetailService.delete(ids);
		return successResult;
	}
	

	/**
	 * 所有RequestMapping方法调用前的Model准备方法, 实现Struts2 Preparable二次部分绑定的效果,先根据form的id从数据库查出User对象,再把Form提交的内容绑定到该对象上。
	 * 因为仅update()方法的form中有id属性，因此仅在update时实际执行.
	 */
	@ModelAttribute
	public void getratePlanDetail(@RequestParam(value = "id", defaultValue = "-1") Long id, Model model) {
		if (id != -1) {
			model.addAttribute("ratePlanDetail", this.ratePlanDetailService.findOne(id));
		}
	}

	@Override
	protected DynamitSupportService<RatePlanDetail> getService() {
		return this.ratePlanDetailService;
	}

}
