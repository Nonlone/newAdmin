package com.feitai.admin.mop.appversion.enums;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.ToString;

/**
 * APP版本管理状态
 */
@ToString
@Getter
public enum AppVersionStatusEnum {

	NEW(1, "新建"),

	ENABLE(2, "可用"),

	DISABLE(3, "暂停"),;

	/**
	 * 状态值
	 */
	private int value;

	/**
	 * 状态描述
	 */
	private String desc;

	/**
	 * @param value
	 * @param desc
	 */
	AppVersionStatusEnum(Integer value, String desc) {
		this.value = value;
		this.desc = desc;
	}

	private static Map<Integer, AppVersionStatusEnum> VALUEMAP = new HashMap<Integer, AppVersionStatusEnum>();

	static {
		for (AppVersionStatusEnum enums : AppVersionStatusEnum.values()) {
			VALUEMAP.put(enums.getValue(), enums);
		}
	}

	public static AppVersionStatusEnum fromValue(int value) {
		return VALUEMAP.get(value);
	}
}
