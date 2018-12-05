/**
 * @author 
 * @version 1.0
 * @since  2018-08-15 10:20:44
 * @desc Channel
 */

package com.feitai.admin.backend.config.web;

import com.alibaba.fastjson.JSONObject;
import com.feitai.admin.backend.config.entity.ChannelPrimary;
import com.feitai.admin.backend.config.service.ChannelPrimaryService;
import com.feitai.admin.backend.config.service.ChannelService;
import com.feitai.admin.backend.properties.MapProperties;
import com.feitai.admin.core.annotation.LogAnnotation;
import com.feitai.admin.core.service.DynamitSupportService;
import com.feitai.admin.core.service.Page;
import com.feitai.admin.core.vo.ListItem;
import com.feitai.admin.core.web.BaseListableController;
import com.feitai.jieya.server.dao.channel.model.Channel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletRequest;
import javax.validation.Valid;
import java.util.*;


@Controller
@RequestMapping(value = "/backend/channel")
@Slf4j
public class ChannelController extends BaseListableController<Channel> {

	@Autowired
	private ChannelService channelService;

	@Autowired
	private ChannelPrimaryService channelPrimaryService;
	
	@Autowired
	private MapProperties mapProperties;
	
	@RequestMapping(value = "index")
	public ModelAndView index() {
		ModelAndView mav=new ModelAndView("/backend/channel/index");
    	List<ListItem> itemList = new ArrayList<>();
    	itemList.add(new ListItem("全部", ""));
    	List<String> channelSortList=mapProperties.getChannelSortList();
    	channelSortList.forEach(channelSort->{
    		 itemList.add(new ListItem(channelSort, channelSort));
    	}); 
    	mav.addObject("channelSortList",JSONObject.toJSONString(itemList));
		return mav;
	}

	@RequestMapping(value = "/checkChannelName", method = RequestMethod.GET)
	@ResponseBody
	public Object checkChannelName(@RequestParam String subPackage, @RequestParam String mainPackage) {
		if(StringUtils.isBlank(mainPackage)){
			return "请先选择一级渠道";
		}
		Channel channel = this.channelService.findBySubPackageAndMainPackage(subPackage,mainPackage);
		if (channel == null) {
			return null;
		} else {
			return "该名称已经被使用";
		}
	}

	@RequestMapping(value = "/checkChannelId", method = RequestMethod.GET)
	@ResponseBody
	public Object checkChannelId(@RequestParam String channelId,@RequestParam String primaryCode) {
		if(StringUtils.isBlank(primaryCode)){
			return "请先选择一级渠道";
		}

		if (channelService.checkChannel(primaryCode+channelId)) {
			return null;
		} else {
			return "该渠道标识已经被使用";
		}
	}

	@RequestMapping(value = "/getPrimaryChannel/{channelName}", method = RequestMethod.GET)
	@ResponseBody
	public Object getPrimaryChannel(@PathVariable("channelName") String channelName) {
		ChannelPrimary channelPrimary = channelPrimaryService.findByChannelName(channelName);
		Map<String,Object> map = new HashMap<>();
		map.put("code",channelPrimary.getChannelCode());
		map.put("sort",channelPrimary.getChannelSort());
		return map;
	}

	@RequestMapping(value = "/primaryList")
	@ResponseBody
	@LogAnnotation(value = true, writeRespBody = false)// 写日志但是不打印请求的params,但不打印ResponseBody的内容
	public Object primaryList(){
		List<ChannelPrimary> channelPrimarys = channelPrimaryService.findAll();
		List<ListItem> list = new ArrayList<ListItem>();
		for(ChannelPrimary channelPrimary:channelPrimarys){
			list.add(new ListItem(channelPrimary.getPrimaryChannelName(), channelPrimary.getPrimaryChannelName()));
		}
		return list;
	}

	@RequiresPermissions("/backend/channel:list")
	@RequestMapping(value = "list")
	@ResponseBody
	public Object listPage(ServletRequest request) {
		Page<Channel> listPage = super.list(request);
		return listPage;
	}
	
	@RequiresPermissions("/backend/channel:update")
	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	@ResponseBody
	public Object editFrom(@PathVariable("id") Long id) {
		Channel channel = this.channelService.findOne(id);
		return channel;
	}
	
	@RequiresPermissions("/backend/channel:add")
	@RequestMapping(value = "add", method = RequestMethod.POST)
	@ResponseBody
	public Object add(@Valid Channel channel){
		ChannelPrimary channelPrimary = channelPrimaryService.findByChannelName(channel.getMainPackgage());
		channel.setMainPackageCode(channelPrimary.getChannelCode().toString());
		channel.setChannelId(channelPrimary.getChannelCode()+"_"+channel.getChannelId());
		channel.setCreatedTime(new Date());
		channel.setChannelSort(channelPrimary.getChannelSort());
		channel.setUpdateTime(new Date());
		this.channelService.save(channel);
		return BaseListableController.successResult;
	}
	
	@RequiresPermissions("/backend/channel:update")
	@RequestMapping(value = "update", method = RequestMethod.POST)
	@ResponseBody
	public Object update(@Valid @ModelAttribute("channel") Channel channel){
		ChannelPrimary channelPrimary = channelPrimaryService.findByChannelName(channel.getMainPackgage());
		channel.setMainPackageCode(channelPrimary.getChannelCode());
		channel.setUpdateTime(new Date());
		this.channelService.save(channel);
		return BaseListableController.successResult;
	}
	
	@RequiresPermissions("/backend/channel:del")
	@RequestMapping(value = "del")
	@ResponseBody
	public Object del(@RequestParam(value = "ids[]") Long[] ids){
		this.channelService.delete(ids);
		return BaseListableController.successResult;
	}
	

	/**
	 * 所有RequestMapping方法调用前的Model准备方法, 实现Struts2 Preparable二次部分绑定的效果,先根据form的id从数据库查出User对象,再把Form提交的内容绑定到该对象上。
	 * 因为仅update()方法的form中有id属性，因此仅在update时实际执行.
	 */
	@ModelAttribute
	public void getchannel(@RequestParam(value = "id", defaultValue = "-1") Long id, Model model) {
		if (id != -1) {
			model.addAttribute("channel", this.channelService.findOne(id));
		}
	}

	@Override
	protected DynamitSupportService<Channel> getService() {
		return this.channelService;
	}

}
