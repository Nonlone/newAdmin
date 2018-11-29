package com.feitai.admin.backend.creditdata.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.feitai.admin.backend.creditdata.service.AttachUserBusiInforService;
import com.feitai.admin.core.service.DynamitSupportService;
import com.feitai.admin.core.web.BaseListableController;
import com.feitai.jieya.server.dao.attach.model.AttachUserBusiInfor;

@Controller
@RequestMapping("/backend/xinwang")
public class AttachUserBusiInforController extends BaseListableController<AttachUserBusiInfor>{

	@Autowired
	private AttachUserBusiInforService attachUserBusiInforService;
	
	@Override
	protected DynamitSupportService<AttachUserBusiInfor> getService() {
		
		return attachUserBusiInforService;
	}
	
	 @GetMapping(value = "/detail/{userId}")
	 public ModelAndView detail(@PathVariable("userId") Long userId){
		 ModelAndView modelAndView = new ModelAndView("/backend/credit/xinwang/detail");
		 AttachUserBusiInfor attachUserBusiInfor =attachUserBusiInforService.getAttachUserBusiInforByUserId(userId);
		 modelAndView.addObject("attachUserBusiInfor", attachUserBusiInfor);
		 return modelAndView;
	 }
     
}
