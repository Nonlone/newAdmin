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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping(value = "/backend/ratePlanTerm")
public class RatePlanTermController extends BaseCrudController<RatePlanTermMore> {
	@Autowired
	private RatePlanTermService ratePlanTermService;
	
	@RequestMapping(value = "")
	public String index() {
		return "/backend/ratePlanTerm/index";
	}
	

	@Override
	protected DynamitSupportService<RatePlanTermMore> getService() {
		return this.ratePlanTermService;
	}

}
