package com.feitai.admin.mop.appversion.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.feitai.admin.backend.config.entity.AppManage;
import com.feitai.admin.core.service.DynamitSupportService;
import com.feitai.admin.core.web.BaseListableController;
import com.feitai.admin.mop.advert.vo.SelectItem;
import com.feitai.admin.mop.appversion.dao.entity.VerAppVersion;
import com.feitai.admin.mop.appversion.enums.OSTypeEnum;
import com.feitai.admin.mop.appversion.enums.UpdateTypeEnum;
import com.feitai.admin.mop.appversion.request.AppChannelQueryRequest;
import com.feitai.admin.mop.appversion.request.AppVersionQueryRequest;
import com.feitai.admin.mop.appversion.service.AppVersionService;
import com.feitai.admin.mop.appversion.vo.VerAppVersionVo;
import com.feitai.admin.mop.base.AdaptDateEditor;
import com.feitai.admin.mop.base.ListUtils;
import com.feitai.jieya.server.dao.channel.model.Channel;
import com.github.pagehelper.PageInfo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/mop/appVersion")
public class AppVersionController extends BaseListableController<VerAppVersion> {

	@InitBinder
	public void initDate(WebDataBinder webDataBinder) {
		webDataBinder.registerCustomEditor(Date.class, new AdaptDateEditor());
	}

	@Autowired
	private AppVersionService appVersionService;

	@RequestMapping()
	@RequiresPermissions("/mop/appVersion:list")
	public ModelAndView index() {
		ModelAndView modelAndView = new ModelAndView("/mop/appVersion/index");
		modelAndView.addObject("appInfoSelectItems", JSON.toJSONString(getAppInfoSelectItems()));
		modelAndView.addObject("osTypeSelectItems", JSON.toJSONString(getOsTypeSelectItems()));
		modelAndView.addObject("versionSelectItems", JSON.toJSONString(getVersionsSelectItems()));
		return modelAndView;
	}

	@RequiresPermissions("/mop/appVersion:list")
	@RequestMapping("list")
	@ResponseBody
	public Object list(@ModelAttribute AppVersionQueryRequest queryRequest) {

		List<VerAppVersion> list = appVersionService.pageList(queryRequest.getPageIndex(), queryRequest.getLimit(),
				queryRequest.getAppCode(), queryRequest.getVersion(), queryRequest.getOsType(), null, null);

		PageInfo<VerAppVersionVo> pageInfo = new PageInfo<>(VerAppVersionVo.buildList(list, getAppInfoMap()));

		return buildPage(pageInfo, queryRequest.getPageIndex(), queryRequest.getLimit());
	}

	@RequiresPermissions("/mop/appVersion:add")
	@RequestMapping("/detail/index")
	public ModelAndView detail(@RequestParam(required = false) Long id) {
		ModelAndView modelAndView = new ModelAndView("/mop/appVersion/detail");
		modelAndView.addObject("appInfoSelectItems", JSON.toJSONString(getAppInfoSelectItems()));
		modelAndView.addObject("osTypeSelectItems", JSON.toJSONString(getOsTypeSelectItems()));
		modelAndView.addObject("updateTypeSelectItems", JSON.toJSONString(getUpdateTypeSelectItems()));
		modelAndView.addObject("versionSelectItems", JSON.toJSONString(getVersionsSelectItems()));
		modelAndView.addObject("channelSortSelectItems", JSON.toJSONString(getChannelSortSelectItems()));
		if (id != null) {
			modelAndView.addObject("id", id);
		}
		return modelAndView;
	}

	@RequiresPermissions("/mop/appVersion:add")
	@RequestMapping("/detail")
	@ResponseBody
	public Object getDetailInfo(@RequestParam(required = true) Long id) {
		if (id != null) {
			return VerAppVersionVo.build(appVersionService.getVerAppVersion(id), null);
		}
		return null;
	}

	@RequiresPermissions("/mop/appVersion:list")
	@RequestMapping("/channel/list")
	@ResponseBody
	public Object channelList(@ModelAttribute AppChannelQueryRequest request) {
		return appVersionService.queryChannelList(request.getChannelSort(), request.getChannelCode());
	}

	@RequiresPermissions("/mop/appVersion:add")
	@RequestMapping("add")
	@ResponseBody
	public Object add(@ModelAttribute VerAppVersionVo info) {
		appVersionService.add(info.toVerAppVersion(), getOperator());
		appVersionService.evictCache(info.getAppCode());
		return BaseListableController.successResult;
	}

	@RequiresPermissions("/mop/appVersion:update")
	@RequestMapping("update")
	@ResponseBody
	public Object update(@ModelAttribute VerAppVersionVo info) {
		appVersionService.update(info.toVerAppVersion(), getOperator());
		appVersionService.evictCache(info.getAppCode());
		return BaseListableController.successResult;
	}

	@RequiresPermissions("/mop/appVersion:update")
	@RequestMapping("saveOrUpdate")
	public Object saveOrUpdate(@ModelAttribute VerAppVersionVo info) {

		VerAppVersion version = info.toVerAppVersion();

		if (StringUtils.isBlank(info.getId())) {
			appVersionService.add(version, getOperator());
		}
		appVersionService.update(version, getOperator());
		appVersionService.evictCache(info.getAppCode());
		return detail(version.getId());
	}

	private Map<String, String> getAppInfoMap() {
		Map<String, String> map = new HashMap<>();

		List<AppManage> infos = appVersionService.queryAppInfoList();

		if (ListUtils.isNotEmpty(infos)) {
			infos.forEach(info -> {
				map.put(info.getCode(), info.getName());
			});
		}

		return map;
	}

	private List<SelectItem> getAppInfoSelectItems() {
		List<SelectItem> list = new ArrayList<>();

		List<AppManage> infos = appVersionService.queryAppInfoList();

		if (ListUtils.isNotEmpty(infos)) {
			infos.forEach(info -> {
				list.add(new SelectItem(info.getName(), info.getCode()));
			});
		}

		return list;
	}

	private List<SelectItem> getVersionsSelectItems() {
		List<SelectItem> list = new ArrayList<>();

		List<String> versions = appVersionService.queryAllVersions();

		if (ListUtils.isNotEmpty(versions)) {
			versions.forEach(version -> {
				list.add(new SelectItem(version, version));
			});
		}

		return list;
	}

	private List<SelectItem> getOsTypeSelectItems() {
		OSTypeEnum[] osTypes = OSTypeEnum.values();
		List<SelectItem> list = new ArrayList<>();

		for (OSTypeEnum osType : osTypes) {
			list.add(new SelectItem(osType.getDesc(), osType.getValue()));
		}
		return list;
	}

	private List<SelectItem> getUpdateTypeSelectItems() {
		UpdateTypeEnum[] updateTypes = UpdateTypeEnum.values();
		List<SelectItem> list = new ArrayList<>();

		for (UpdateTypeEnum updateType : updateTypes) {
			list.add(new SelectItem(updateType.getDesc(), String.valueOf(updateType.getValue())));
		}
		return list;
	}

	private List<SelectItem> getChannelSortSelectItems() {
		List<Channel> channels = appVersionService.queryChannelList(null, null);
		List<SelectItem> list = new ArrayList<>();

		Set<String> infoCodeSet = new HashSet<>();

		for (Channel channel : channels) {

			if (infoCodeSet.contains(channel.getChannelSort())) {
				continue;
			}

			infoCodeSet.add(channel.getChannelSort());
			list.add(new SelectItem(channel.getChannelSort(), channel.getChannelSort()));
		}
		return list;
	}

	@Override
	protected DynamitSupportService<VerAppVersion> getService() {
		return null;
	}

	/**
	 * 获得操作人
	 * 
	 * @return
	 */
	public String getOperator() {
		return SecurityUtils.getSubject().getPrincipal().toString();
	}
}