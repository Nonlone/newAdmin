package com.feitai.admin.mop.appversion.enums;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.ToString;

/**
 * 系统类型
 */
@ToString
@Getter
public enum OSTypeEnum {

	ANDROID("android", "android"),

	IOS("ios", "ios"),

	;

	/**
	 * 状态值
	 */
	private String value;

	/**
	 * 状态描述
	 */
	private String desc;

	/**
	 * @param value
	 * @param desc
	 */
	OSTypeEnum(String value, String desc) {
		this.value = value;
		this.desc = desc;
	}

	private static Map<String, OSTypeEnum> ValueMap = new HashMap<String, OSTypeEnum>();

	static {
		for (OSTypeEnum enums : OSTypeEnum.values()) {
			ValueMap.put(enums.getValue(), enums);
		}
	}

	public static OSTypeEnum fromValue(String value) {
		return ValueMap.get(value);
	}
}
