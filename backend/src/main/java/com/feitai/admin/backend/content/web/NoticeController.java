/**
 * @author 
 * @version 1.0
 * @since  2018-07-11 14:57:46
 * @desc Notice
 */

package com.feitai.admin.backend.content.web;


import com.feitai.admin.backend.content.service.NoticeService;
import com.feitai.admin.core.service.DynamitSupportService;
import com.feitai.admin.core.service.Page;
import com.feitai.admin.core.web.BaseListableController;
import com.feitai.jieya.server.dao.cms.model.Notice;
import com.feitai.utils.SnowFlakeIdGenerator;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import javax.validation.Valid;
import java.util.Date;


@Controller
@RequestMapping(value = "/backend/notice")
@Slf4j
public class NoticeController extends BaseListableController<Notice> {
	@Autowired
	private NoticeService noticeService;
	
	@RequestMapping(value = "index")
	public String index() {
		return "/backend/notice/index";
	}
	
	@RequiresPermissions("/backend/notice:list")
	@RequestMapping(value = "list")
	@ResponseBody
	public Object listPage(ServletRequest request) {
		Page<Notice> listPage = super.list(request);
		return listPage;
	}
	
	@RequiresPermissions("/backend/notice:update")
	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	@ResponseBody
	public Object editFrom(@PathVariable("id") Long id) {
		Notice notice = this.noticeService.findOne(id);
		return notice;
	}
	
	@RequiresPermissions("/backend/notice:add")
	@RequestMapping(value = "add", method = RequestMethod.POST)
	@ResponseBody
	public Object add(@Valid Notice notice){
		notice.setId(SnowFlakeIdGenerator.getDefaultNextId());
		notice.setCreatedTime(new Date());
		notice.setUpdateTime(new Date());
		this.noticeService.save(notice);
		return successResult;
	}
	
	@RequiresPermissions("/backend/notice:update")
	@RequestMapping(value = "update", method = RequestMethod.POST)
	@ResponseBody
	public Object update(@Valid @ModelAttribute Notice notice, Model model){
		notice.setUpdateTime(new Date());
		this.noticeService.updateByPrimaryKey(notice);
		return successResult;
	}
	
	@RequiresPermissions("/backend/notice:del")
	@RequestMapping(value = "del")
	@ResponseBody
	public Object del(@RequestParam(value = "ids[]") Long[] ids){
		this.noticeService.delete(ids);
		return successResult;
	}
	

	/**
	 * 所有RequestMapping方法调用前的Model准备方法, 实现Struts2 Preparable二次部分绑定的效果,先根据form的id从数据库查出User对象,再把Form提交的内容绑定到该对象上。
	 * 因为仅update()方法的form中有id属性，因此仅在update时实际执行.
	 */
	@ModelAttribute
	public void getnotice(@RequestParam(value = "id", defaultValue = "-1") Long id, Model model) {
		if (id != -1) {
			model.addAttribute("notice", this.noticeService.findOne(id));
		}
	}

	@Override
	protected DynamitSupportService<Notice> getService() {
		return this.noticeService;
	}

}
