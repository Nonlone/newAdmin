package com.feitai.admin.mop.appversion.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.feitai.admin.backend.config.entity.AppManage;
import com.feitai.admin.backend.config.service.AppManageService;
import com.feitai.admin.backend.config.service.ChannelService;
import com.feitai.admin.mop.appversion.dao.entity.VerAppVersion;
import com.feitai.admin.mop.appversion.dao.mapper.VerAppVersionMapper;
import com.feitai.admin.mop.appversion.enums.AppVersionStatusEnum;
import com.feitai.admin.mop.base.BusinessException;
import com.feitai.admin.mop.base.RpcResult;
import com.feitai.jieya.server.dao.channel.model.Channel;
import com.feitai.utils.SnowFlakeIdGenerator;
import com.feitai.utils.http.OkHttpClientUtils;
import com.github.pagehelper.PageHelper;

import lombok.extern.slf4j.Slf4j;
import tk.mybatis.mapper.entity.Example;

@Slf4j
@Service
public class AppVersionService {

	@Autowired
	private VerAppVersionMapper appVersionMapper;

	@Autowired
	private ChannelService channelService;

	@Autowired
	private AppManageService appManageService;

	@Value("${mop.app.version.evictCache.url}")
	private String appVersionEvictUrl;

	public void add(VerAppVersion appVersion, String operator) {
		Date now = new Date();

		appVersion.setId(SnowFlakeIdGenerator.getDefaultNextId());
		appVersion.setStatus(AppVersionStatusEnum.ENABLE.getValue());
		appVersion.setCreatedTime(now);
		appVersion.setUpdateTime(now);
		appVersionMapper.insertSelective(appVersion);
	}

	public void update(VerAppVersion appVersion, String operator) {
		appVersion.setCreatedTime(null);
		appVersion.setUpdateTime(new Date());
		appVersionMapper.updateByPrimaryKeySelective(appVersion);
	}

	public List<VerAppVersion> pageList(int pageNo, int pageSize, String appCode, String version, String osType,
			String orderField, String order) {
		PageHelper.startPage(pageNo, pageSize);
		Example example = new Example(VerAppVersion.class);
		Example.Criteria criteria = example.createCriteria();
		if (StringUtils.isNotBlank(appCode)) {
			criteria.andEqualTo("appCode", appCode);
		}
		if (StringUtils.isNotBlank(version)) {
			criteria.andEqualTo("appVersion", version);
		}
		if (StringUtils.isNotBlank(osType)) {
			criteria.andEqualTo("osType", osType);
		}
		if (orderField != null) {
			if ("DESC".equals(order)) {
				example.orderBy(orderField).desc();
			} else {
				example.orderBy(orderField).asc();
			}
		} else {
			example.orderBy("id").desc();
		}

		return appVersionMapper.selectByExample(example);
	}

	public VerAppVersion getVerAppVersion(long id) {
		Example example = new Example(VerAppVersion.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("id", id);
		return appVersionMapper.selectOneByExample(example);
	}

	public List<AppManage> queryAppInfoList() {
		return appManageService.findAll();
	}

	public List<Channel> queryChannelList(String searchChannelSort, String searchChannelId) {
		List<Channel> list = new ArrayList<>();

		// List<ChannelInfoVo> queryList = queryChannelList();

		List<Channel> queryList = channelService.findAll();

		for (Channel channel : queryList) {

			if (StringUtils.isNotBlank(searchChannelSort) && !searchChannelSort.equals(channel.getChannelSort())) {
				continue;
			}

			if (StringUtils.isNotBlank(searchChannelId) && !(searchChannelId.equals(channel.getChannelId())
					|| searchChannelId.equals(channel.getChannelId()))) {
				continue;
			}

			list.add(channel);
		}

		return list;
	}

	public List<String> queryAllVersions() {
		return appVersionMapper.queryAllVersions();
	}

	public boolean evictCache(String appCode) {
		RpcResult result = null;
		try {
			result = doEvictCache(appCode);
		} catch (IOException e) {
			throw new BusinessException("缓存更新失败", e);
		}
		if (null != result && result.isSuccess()) {
			return true;
		} else {
			throw new BusinessException("缓存更新失败" + (null != result ? result.getMessage() : ""));
		}
	}

	private RpcResult doEvictCache(String appCode) throws IOException {
		JSONObject param = new JSONObject();
		param.put("appCode", appCode);
		JSONObject data = new JSONObject();
		data.put("data", param);
		String resultStr = OkHttpClientUtils.postReturnBody(appVersionEvictUrl, data);
		return JSON.parseObject(resultStr, RpcResult.class);
	}
}
