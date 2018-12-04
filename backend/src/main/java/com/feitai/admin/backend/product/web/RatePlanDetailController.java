/**
 * @author 
 * @version 1.0
 * @since  2018-08-10 13:12:44
 * @desc 科目明细
 */

package com.feitai.admin.backend.product.web;


import com.feitai.admin.backend.product.service.RatePlanDetailService;
import com.feitai.admin.core.service.DynamitSupportService;
import com.feitai.admin.core.web.BaseCrudController;
import com.feitai.jieya.server.dao.rateplan.model.RatePlanDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;



@Controller
@RequestMapping(value = "/backend/ratePlanDetail")
public class RatePlanDetailController extends BaseCrudController<RatePlanDetail>{

	@Autowired
	private RatePlanDetailService ratePlanDetailService;
	
	@RequestMapping(value = "")
	public String index() {
		return "/backend/ratePlanDetail/index";
	}
	

	@Override
	protected DynamitSupportService<RatePlanDetail> getService() {
		return this.ratePlanDetailService;
	}

}
