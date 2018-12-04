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
import com.feitai.jieya.server.dao.product.model.ProductTermFeeFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;



@Controller
@RequestMapping(value = "/backend/productTermFeeFeature")
public class ProductTermFeeFeatureController extends BaseCrudController<ProductTermFeeFeature> {
	@Autowired
	private ProductTermFeeFeatureService productTermFeeFeatureService;
	
	@RequestMapping(value = "")
	public String index() {
		return "/backend/productTermFeeFeature/index";
	}


	@Override
	protected DynamitSupportService<ProductTermFeeFeature> getService() {
		return this.productTermFeeFeatureService;
	}

}
