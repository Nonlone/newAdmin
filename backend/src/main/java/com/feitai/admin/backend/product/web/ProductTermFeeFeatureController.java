/**
 * @author 
 * @version 1.0
 * @since  2018-07-25 09:44:15
 * @desc 产品期限费用特性
 */

package com.feitai.admin.backend.product.web;

import com.feitai.admin.backend.product.service.ProductTermFeeFeatureService;
import com.feitai.admin.core.service.DynamitSupportService;
import com.feitai.admin.core.web.BaseCrudController;
import com.feitai.admin.core.web.BaseListableController;
import com.feitai.jieya.server.dao.product.model.ProductTermFeeFeature;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import javax.validation.Valid;
import java.util.Date;


@Controller
@RequestMapping(value = "/backend/productTermFeeFeature")
public class ProductTermFeeFeatureController extends BaseCrudController<ProductTermFeeFeature> {
	@Autowired
	private ProductTermFeeFeatureService productTermFeeFeatureService;
	
	@RequestMapping(value = "")
	public String index() {
		return "/backend/productTermFeeFeature/index";
	}
/*	
	@RequiresPermissions("/backend/productTermFeeFeature:list")
	@RequestMapping(value = "list")
	@ResponseBody
	public Object listPage(ServletRequest request) {
		return super.list(request);
	}
	
	@RequiresPermissions("/backend/productTermFeeFeature:update")
	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	@ResponseBody
	public Object editFrom(@PathVariable("id") Long id) {
		ProductTermFeeFeature productTermFeeFeature = this.productTermFeeFeatureService.findOne(id);
		return productTermFeeFeature;
	}
	
	@RequiresPermissions("/backend/productTermFeeFeature:add")
	@RequestMapping(value = "add", method = RequestMethod.POST)
	@ResponseBody
	public Object add(@Valid ProductTermFeeFeature productTermFeeFeature){
		productTermFeeFeature.setCreatedTime(new Date());
		productTermFeeFeature.setUpdateTime(new Date());
		this.productTermFeeFeatureService.save(productTermFeeFeature);
		return successResult;
	}
	
	@RequiresPermissions("/backend/productTermFeeFeature:update")
	@RequestMapping(value = "update", method = RequestMethod.POST)
	@ResponseBody
	public Object update(@Valid @ModelAttribute("productTermFeeFeature") ProductTermFeeFeature productTermFeeFeature){
		productTermFeeFeature.setUpdateTime(new Date());
		this.productTermFeeFeatureService.save(productTermFeeFeature);
		return successResult;
	}
	
	@RequiresPermissions("/backend/productTermFeeFeature:del")
	@RequestMapping(value = "del")
	@ResponseBody
	public Object del(@RequestParam(value = "ids[]") Long[] ids){
		this.productTermFeeFeatureService.delete(ids);
		return successResult;
	}
	

	*//**
	 * 所有RequestMapping方法调用前的Model准备方法, 实现Struts2 Preparable二次部分绑定的效果,先根据form的id从数据库查出User对象,再把Form提交的内容绑定到该对象上。
	 * 因为仅update()方法的form中有id属性，因此仅在update时实际执行.
	 *//*
	@ModelAttribute
	public void getproductTermFeeFeature(@RequestParam(value = "id", defaultValue = "-1") Long id, Model model) {
		if (id != -1) {
			model.addAttribute("productTermFeeFeature", this.productTermFeeFeatureService.findOne(id));
		}
	}*/

	@Override
	protected DynamitSupportService<ProductTermFeeFeature> getService() {
		return this.productTermFeeFeatureService;
	}

}
