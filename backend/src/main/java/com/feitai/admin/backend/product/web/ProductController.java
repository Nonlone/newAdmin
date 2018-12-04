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
import com.feitai.admin.core.web.BaseCrudController;
import com.feitai.jieya.server.dao.product.model.Product;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;


@Controller
@RequestMapping(value = "/backend/product")
public class ProductController extends BaseCrudController<Product> {
	@Autowired
	private ProductService productService;

	@RequestMapping(value = "productNameList")
	@ResponseBody
	@LogAnnotation(value = true, writeRespBody = false)// 写日志但是不打印请求的params,但不打印ResponseBody的内容
	public Object ProductList(){
		List<Product> products = productService.findAll();
		List<ListItem> list = new ArrayList<ListItem>();
		list.add(new ListItem("全部"," "));
		for(Product product:products){
			list.add(new ListItem(product.getRemark(), product.getId().toString()));
		}
		return list;
	}

	@RequiresPermissions(value = {"/backend/product:update", "/backend/product:add"}, logical = Logical.OR)
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
		return "/backend/product/index";
	}
	

	@Override
	protected DynamitSupportService<Product> getService() {
		return this.productService;
	}

}
