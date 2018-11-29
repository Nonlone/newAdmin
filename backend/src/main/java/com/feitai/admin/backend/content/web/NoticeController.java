/**
 * @author 
 * @version 1.0
 * @since  2018-07-11 14:57:46
 * @desc Notice
 */

package com.feitai.admin.backend.content.web;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.feitai.admin.backend.content.service.NoticeService;
import com.feitai.admin.core.service.DynamitSupportService;
import com.feitai.admin.core.web.BaseCrudController;
import com.feitai.jieya.server.dao.cms.model.Notice;

import lombok.extern.slf4j.Slf4j;


@Controller
@RequestMapping(value = "/backend/notice")
@Slf4j
public class NoticeController extends BaseCrudController<Notice> {
	@Autowired
	private NoticeService noticeService;
	
	@RequestMapping(value = "")
	public String index() {		
		return "/backend/notice/index";
	}
	

	@Override
	protected DynamitSupportService<Notice> getService() {
		return this.noticeService;
	}


}
