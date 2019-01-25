package com.feitai.admin.mop.appversion.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.feitai.admin.mop.appversion.dao.entity.VerAppVersion;
import com.feitai.admin.mop.appversion.dao.entity.VerAppVersion.VerAppUpdateRule;
import com.feitai.admin.mop.appversion.dao.entity.VerAppVersion.VerAppUpdateRule.UpdateRule;
import com.feitai.admin.mop.appversion.enums.OSTypeEnum;
import com.feitai.admin.mop.appversion.enums.UpdateTypeEnum;
import com.feitai.admin.mop.base.ListUtils;

import lombok.Data;

@Data
public class VerAppVersionVo implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	private String id;
	/**
	 * 应用编码
	 */
	private String appCode;
	/**
	 * 应用编码
	 */
	private String appName;
	/**
	 * APP外部版本号
	 */
	private String appVersion;
	/**
	 * 内部版本号
	 */
	private String insideVersion;
	/**
	 * 系统类型{@link OSTypeEnum}
	 */
	private String osType;
	/**
	 * 更新说明
	 */
	private String updateNote;
	/**
	 * 通用版本更新方式
	 */
	private Integer commonRuleUpdateType;
	/**
	 * 通用版本更新方式(描述)
	 */
	private String commonRuleUpdateTypeDesc;
	/**
	 * 是否存在指定版本更新
	 */
	private Boolean existAddition;
	/**
	 * 是否存在高级更新配置
	 */
	private Boolean existCustom;
	/**
	 * 创建时间
	 */
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date createdTime;
	/**
	 * 更新时间
	 */
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date updateTime;

	// ------------更新使用
	private String updateRule;

	public static List<VerAppVersionVo> buildList(List<VerAppVersion> appVersions, Map<String, String> appNameMap) {
		List<VerAppVersionVo> list = new ArrayList<>();

		if (ListUtils.isEmpty(appVersions)) {
			return list;
		}

		for (VerAppVersion appVersion : appVersions) {
			VerAppVersionVo vo = build(appVersion, appNameMap.get(appVersion.getAppCode()));
			list.add(vo);
		}

		return list;
	}

	public static VerAppVersionVo build(VerAppVersion appVersion, String appName) {
		if (null == appVersion) {
			return null;
		}

		VerAppVersionVo vo = new VerAppVersionVo();
		vo.setId(String.valueOf(appVersion.getId()));
		vo.setAppCode(appVersion.getAppCode());
		vo.setAppName(ObjectUtils.defaultIfNull(appName, appVersion.getAppCode()));
		vo.setAppVersion(appVersion.getAppVersion());
		vo.setInsideVersion(appVersion.getInsideVersion());
		vo.setOsType(appVersion.getOsType());
		vo.setUpdateNote(appVersion.getUpdateNote());

		vo.setExistAddition(false);
		vo.setExistCustom(false);
		vo.setCreatedTime(appVersion.getCreatedTime());
		vo.setUpdateTime(appVersion.getUpdateTime());
		vo.setUpdateRule(appVersion.getUpdateRule());
		VerAppUpdateRule rule = JSON.parseObject(appVersion.getUpdateRule(), VerAppUpdateRule.class);

		setVerAppUpdateRule(rule, vo);

		return vo;
	}

	private static void setVerAppUpdateRule(VerAppUpdateRule rule, VerAppVersionVo vo) {

		if (null == rule) {
			return;
		}

		setCommonRule(vo, rule.getCommonRule());
		setCustomRules(vo, rule.getCustomRules());
	}

	private static void setCustomRules(VerAppVersionVo vo, List<UpdateRule> customRules) {

		if (ListUtils.isEmpty(customRules)) {
			vo.setExistCustom(false);
			return;
		}

		vo.setExistCustom(true);

		if (vo.getExistAddition()) {
			return;
		}

		for (UpdateRule updateRule : customRules) {

			if (ListUtils.isEmpty(updateRule.getAdditions())) {
				continue;
			}

			vo.setExistAddition(true);
			break;
		}
	}

	private static void setCommonRule(VerAppVersionVo vo, UpdateRule commonUpdateRule) {
		if (null == commonUpdateRule) {
			vo.setExistAddition(false);
			return;
		}

		UpdateTypeEnum typeEnum = UpdateTypeEnum.fromValue(commonUpdateRule.getDefaultUpdateType());

		if (null != typeEnum) {
			vo.setCommonRuleUpdateType(typeEnum.getValue());
			vo.setCommonRuleUpdateTypeDesc(typeEnum.getDesc());
		}

		if (ListUtils.isNotEmpty(commonUpdateRule.getAdditions())) {
			vo.setExistAddition(true);
		}
	}

	public VerAppVersion toVerAppVersion() {
		VerAppVersion version = new VerAppVersion();
		if (StringUtils.isNotBlank(id)) {
			version.setId(Long.valueOf(id));
		}
		version.setAppCode(appCode);
		version.setOsType(osType);
		version.setAppVersion(appVersion);
		version.setInsideVersion(insideVersion);
		version.setUpdateNote(updateNote);
		version.setUpdateRule(updateRule);
		return version;
	}
}
