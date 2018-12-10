/**
 * @author 
 * @version 1.0
 * @since  2018-10-23 14:27:41
 * @desc ChannelPrimary
 */

package com.feitai.admin.backend.config.web;

import com.alibaba.fastjson.JSONObject;
import com.feitai.admin.backend.config.entity.ChannelPrimary;
import com.feitai.admin.backend.config.service.ChannelPrimaryService;
import com.feitai.admin.backend.config.service.ChannelService;
import com.feitai.admin.backend.properties.MapProperties;
import com.feitai.admin.core.service.DynamitSupportService;
import com.feitai.admin.core.service.OnCondition;
import com.feitai.admin.core.service.Operator;
import com.feitai.admin.core.service.SelectMultiTable;
import com.feitai.admin.core.vo.AjaxResult;
import com.feitai.admin.core.vo.ListItem;
import com.feitai.admin.core.web.BaseListableController;
import com.feitai.jieya.server.dao.channel.model.Channel;
import com.feitai.utils.SnowFlakeIdGenerator;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Controller
@RequestMapping(value = "/backend/channelPrimary")
@Slf4j
public class ChannelPrimaryController extends BaseListableController<ChannelPrimary> {

	@Autowired
	private ChannelPrimaryService channelPrimaryService;

	@Autowired
	private ChannelService channelService;
	
	@Autowired
	private MapProperties mapProperties;
	
	@RequestMapping(value = "index")
	public ModelAndView index() {
		ModelAndView mav=new ModelAndView("/backend/channelPrimary/index");
    	List<ListItem> itemList = new ArrayList<>();
    	itemList.add(new ListItem("全部", ""));
    	List<String> channelSortList=mapProperties.getChannelSortList();
    	channelSortList.forEach(channelSort->{
    		 itemList.add(new ListItem(channelSort, channelSort));
    	}); 
    	mav.addObject("channelSortList",JSONObject.toJSONString(itemList));
		return mav;
	}
	
	@RequiresPermissions("/backend/channelPrimary:list")
	@RequestMapping(value = "list")
	@ResponseBody
	public Object listPage(ServletRequest request) {
		return super.list(request);
	}


	@RequiresPermissions("/backend/channelPrimary:update")
	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	@ResponseBody
	public Object editFrom(@PathVariable("id") Long id) {
		ChannelPrimary channelPrimary = this.channelPrimaryService.findOne(id);
		return channelPrimary;
	}

	@RequiresPermissions("/backend/channelPrimary:update")
	@RequestMapping(value = "checkChannelName")
	@ResponseBody
	public Object checkChannelName(@RequestParam String primaryChannelName) {
		ChannelPrimary channelPrimary = this.channelPrimaryService.findByChannelName(primaryChannelName);
		if (channelPrimary == null) {
			return null;
		} else {
			return "该名称已经被使用";
		}
	}

	@RequiresPermissions("/backend/channelPrimary:update")
	@RequestMapping(value = "checkChannelCode")
	@ResponseBody
	public Object checkChannelCode(@RequestParam String channelCode) {
		ChannelPrimary channelPrimary = this.channelPrimaryService.findByChannelCode(channelCode);
		if (channelPrimary == null) {
			return null;
		} else {
			return "该标识已经被使用";
		}
	}

	@RequiresPermissions("/backend/channelPrimary:add")
	@RequestMapping(value = "add", method = RequestMethod.POST)
	@ResponseBody
	public Object add(@Valid ChannelPrimary channelPrimary){
		channelPrimary.setId(SnowFlakeIdGenerator.getDefaultNextId());
		channelPrimary.setUpdateTime(new Date());
		this.channelPrimaryService.save(channelPrimary);
		return this.successResult;


	}

	@RequiresPermissions("/backend/channelPrimary:update")
	@RequestMapping(value = "update", method = RequestMethod.POST)
	@ResponseBody
	public Object update(@Valid @ModelAttribute("channelPrimary") ChannelPrimary channelPrimary){
		channelPrimary.setUpdateTime(new Date());
		this.channelPrimaryService.save(channelPrimary);
		return this.successResult;
	}
	
	@RequiresPermissions("/backend/channelPrimary:del")
	@RequestMapping(value = "del")
	@ResponseBody
	public Object del(@RequestParam(value = "ids[]") Long[] ids){
		boolean delAllow = true;
		for(Long id:ids){
			if(channelService.countBySqls(getCountSql(id),SelectMultiTable.COUNT_ALIAS)!=0){
				return new AjaxResult(false, "id为：[" + id + "] 的一级渠道尚有对应二级渠道未删除");
			}
		}
		this.channelPrimaryService.delete(ids);
		return this.successResult;
	}
	private  String getCountSql(Long id) {
		StringBuffer countSql=new StringBuffer();
		String sql=SelectMultiTable.builder(Channel.class).leftJoin(ChannelPrimary.class, "channelPrimary", new OnCondition[]{
                        new OnCondition(SelectMultiTable.ConnectType.AND, "mainPackageCode", Operator.EQ, "channelCode")
                }).buildCountSqlString();
		countSql.append(sql)
		        .append(" where channelPrimary.id=")
		        .append(id);
		return countSql.toString();
	}

	/**
	 * 所有RequestMapping方法调用前的Model准备方法, 实现Struts2 Preparable二次部分绑定的效果,先根据form的id从数据库查出User对象,再把Form提交的内容绑定到该对象上。
	 * 因为仅update()方法的form中有id属性，因此仅在update时实际执行.
	 */
	@ModelAttribute
	public void getchannelPrimary(@RequestParam(value = "id", defaultValue = "-1") Long id, Model model) {
		if (id != -1) {
			model.addAttribute("channelPrimary", this.channelPrimaryService.findOne(id));
		}
	}

	@Override
	protected DynamitSupportService<ChannelPrimary> getService() {
		return this.channelPrimaryService;
	}

}
