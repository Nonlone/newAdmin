package com.feitai.admin.mop.appversion.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.ArrayUtils;
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

	@Value("${mop.app.version.history}")
	private String appHistoryVersion;

	private volatile List<String> appHistoryVersionList;

	public void add(VerAppVersion appVersion, String operator) {
		VerAppVersion readVersion = query(appVersion.getAppCode(), appVersion.getOsType(), appVersion.getAppVersion());

		if (null != readVersion) {
			throw new BusinessException(String.format("外部版本号【%s】已存在,请更换外部版本号", appVersion.getAppVersion()));
		}

		Date now = new Date();

		appVersion.setId(SnowFlakeIdGenerator.getDefaultNextId());
		appVersion.setAppVersionValue(versionLongValue(appVersion.getAppVersion()));
		appVersion.setStatus(AppVersionStatusEnum.ENABLE.getValue());
		appVersion.setCreatedTime(now);
		appVersion.setUpdateTime(now);
		appVersionMapper.insertSelective(appVersion);
	}

	public void update(VerAppVersion appVersion, String operator) {

		VerAppVersion readVersion = query(appVersion.getAppCode(), appVersion.getOsType(), appVersion.getAppVersion());

		if (null != readVersion && readVersion.getId().longValue() != appVersion.getId()) {
			throw new BusinessException(String.format("外部版本号【%s】已存在,请更换外部版本号", appVersion.getAppVersion()));
		}

		appVersion.setAppVersionValue(versionLongValue(appVersion.getAppVersion()));
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

	protected VerAppVersion query(String appCode, String osType, String version) {
		Example example = new Example(VerAppVersion.class);

		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("appCode", appCode);
		criteria.andEqualTo("osType", osType);
		criteria.andEqualTo("appVersion", version);

		return appVersionMapper.selectOneByExample(example);
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

		List<Channel> queryList = channelService.findAll();

		for (Channel channel : queryList) {

			if (StringUtils.isNotBlank(searchChannelSort) && !searchChannelSort.equals(channel.getChannelSort())) {
				continue;
			}

			if (StringUtils.isNotBlank(searchChannelId)
					&& -1 == channel.getChannelId().toLowerCase().indexOf(searchChannelId.toLowerCase())) {
				continue;
			}

			list.add(channel);
		}

		return list;
	}

	public List<String> queryAllVersions() {
		List<String> versions = new ArrayList<>();

		List<String> queryVersions = appVersionMapper.queryAllVersions();

		versions.addAll(queryVersions);
		versions.addAll(getHistoryVersions());

		versions.sort(new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				return o2.compareToIgnoreCase(o1);
			}
		});

		return versions;
	}

	private synchronized List<String> getHistoryVersions() {

		if (null != appHistoryVersionList) {
			return appHistoryVersionList;
		}

		appHistoryVersionList = new ArrayList<>();

		String[] tmpVersions = appHistoryVersion.split(",");

		if (ArrayUtils.isEmpty(tmpVersions)) {
			return appHistoryVersionList;
		}

		Set<String> tmpVersionSet = new HashSet<>();

		for (String tmpVersion : tmpVersions) {
			if (tmpVersionSet.contains(tmpVersion)) {
				continue;
			}
			appHistoryVersionList.add(tmpVersion);
			tmpVersionSet.add(tmpVersion);
		}

		return appHistoryVersionList;
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

	private long versionLongValue(String appVersion) {
		if (StringUtils.isBlank(appVersion)) {
			return 0L;
		}

		// 版本结构1.4.1三段式
		String[] vals = appVersion.split("\\.");

		String version = "";

		for (int i = vals.length; i > 0; i--) {
			String tmp = String.format("%04d", Integer.parseInt(vals[i - 1]));
			version = tmp + version;
		}

		return Long.parseLong(version);
	}
}
