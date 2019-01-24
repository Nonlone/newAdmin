package com.feitai.admin.mop.appversion.enums;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.ToString;

/**
 * 更新方式
 */
@ToString
@Getter
public enum UpdateTypeEnum {

	NO_PROMPT(0, "不提示更新"),

	PROMPT(1, "提示更新"),

	FORCE(2, "强制更新"),;

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
	UpdateTypeEnum(Integer value, String desc) {
		this.value = value;
		this.desc = desc;
	}

	private static Map<Integer, UpdateTypeEnum> VALUEMAP = new HashMap<Integer, UpdateTypeEnum>();

	static {
		for (UpdateTypeEnum enums : UpdateTypeEnum.values()) {
			VALUEMAP.put(enums.getValue(), enums);
		}
	}

	public static UpdateTypeEnum fromValue(int value) {
		return VALUEMAP.get(value);
	}
}
