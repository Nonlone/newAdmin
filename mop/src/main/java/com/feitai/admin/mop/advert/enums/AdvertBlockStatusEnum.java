package com.feitai.admin.mop.advert.enums;

import lombok.Getter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;


/**
 * 广告块状态
 */
@ToString
@Getter
public enum AdvertBlockStatusEnum {
	
	NEW(1, "新建"),
	
	ENABLE(2, "可用"),
	
	DISABLE(3, "暂停"),
	;
	
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
    AdvertBlockStatusEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }
    
    
    private static Map<Integer, AdvertBlockStatusEnum> VALUEMAP = new HashMap<Integer, AdvertBlockStatusEnum>();
    
    static {
    	for (AdvertBlockStatusEnum enums : AdvertBlockStatusEnum.values()) {
    		VALUEMAP.put(enums.getValue(), enums);
		}
    }
    
    public static AdvertBlockStatusEnum fromValue(int value) {
		return VALUEMAP.get(value);
	}
}
