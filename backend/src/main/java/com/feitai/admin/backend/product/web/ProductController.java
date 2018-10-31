/**
 * @author 
 * @version 1.0
 * @since  2018-07-25 09:42:04
 * @desc 产品表
 */

package com.feitai.admin.backend.product.web;

import com.feitai.admin.backend.product.service.ProductService;
import com.feitai.admin.core.annotation.LogAnnotation;
import com.feitai.admin.core.service.DynamitSupportService;
import com.feitai.admin.core.vo.ListItem;
import com.feitai.admin.core.web.BaseListableController;
import com.feitai.jieya.server.dao.product.model.Product;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Controller
@RequestMapping(value = "/admin/product/product")
public class ProductController extends BaseListableController<Product> {
	@Autowired
	private ProductService productService;

	@RequestMapping(value = "productNameList")
	@ResponseBody
	@LogAnnotation(value = true, writeRespBody = false)// 写日志但是不打印请求的params,但不打印ResponseBody的内容
	public Object ProductList(){
		List<Product> products = productService.findAll();
		List<ListItem> list = new ArrayList<ListItem>();
		for(Product product:products){
			list.add(new ListItem(product.getName(), product.getName()));
		}
		return list;
	}

	@RequiresPermissions(value = {"/admin/product/product:update", "/admin/product/product:add"}, logical = Logical.OR)
	@RequestMapping(value = "items")
	@ResponseBody
	@LogAnnotation(value = true, writeRespBody = false)// 写日志但是不打印请求的params,但不打印ResponseBody的内容
	public List<ListItem> items() {
		List<Product> productList = productService.findAll();
		List<ListItem> list = new ArrayList<ListItem>();
		for (Product product : productList) {
			//只展示启动产品
			if(product.getEnabled()==1) {
				list.add(new ListItem(product.getName(), product.getId().toString()));
			}
		}
		return list;
	}
	
	@RequestMapping(value = "")
	public String index() {
		return "/admin/product/product/index";
	}
	
	@RequiresPermissions("/admin/product/product:list")
	@RequestMapping(value = "list")
	@ResponseBody
	public Object listPage(ServletRequest request) {
		return super.list(request);
	}
	
	@RequiresPermissions("/admin/product/product:update")
	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	@ResponseBody
	public Object editFrom(@PathVariable("id") Long id) {
		Product product = this.productService.findOne(id);
		return product;
	}
	
	@RequiresPermissions("/admin/product/product:add")
	@RequestMapping(value = "add", method = RequestMethod.POST)
	@ResponseBody
	public Object add(@Valid Product product){
		product.setCreatedTime(new Date());
		product.setUpdateTime(new Date());
		//给初创product给与默认值
		this.productService.save(product);
		return successResult;
	}
	
	@RequiresPermissions("/admin/product/product:update")
	@RequestMapping(value = "update", method = RequestMethod.POST)
	@ResponseBody
	public Object update(@Valid @ModelAttribute("product") Product product){
		product.setUpdateTime(new Date());
		this.productService.save(product);
		return successResult;
	}
	
	@RequiresPermissions("/admin/product/product:del")
	@RequestMapping(value = "del")
	@ResponseBody
	public Object del(@RequestParam(value = "ids[]") Long[] ids){
		this.productService.delete(ids);
		return successResult;
	}
	

	/**
	 * 所有RequestMapping方法调用前的Model准备方法, 实现Struts2 Preparable二次部分绑定的效果,先根据form的id从数据库查出User对象,再把Form提交的内容绑定到该对象上。
	 * 因为仅update()方法的form中有id属性，因此仅在update时实际执行.
	 */
	@ModelAttribute
	public void getproduct(@RequestParam(value = "id", defaultValue = "-1") Long id, Model model) {
		if (id != -1) {
			model.addAttribute("product", this.productService.findOne(id));
		}
	}

	@Override
	protected DynamitSupportService<Product> getService() {
		return this.productService;
	}

	@InitBinder
	public void initDate(WebDataBinder webDataBinder){
		webDataBinder.addCustomFormatter(new DateFormatter("yyyy-MM-dd HH:mm:ss"));
		webDataBinder.addCustomFormatter(new DateFormatter("yyyy-MM-dd"));
	}
}
