package com.feitai.admin.mop.advert.enums;

import lombok.Getter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;


/**
 * 广告项事件类型
 */
@ToString
@Getter
public enum AdvertItemEventTypeEnum {
	
	IMAGE("link", "跳转链接"),
	;
	
	/**
     * 值
     */
    private String value;


    /**
     * 描述
     */
    private String desc;


    /**
     * @param value
     * @param desc
     */
    AdvertItemEventTypeEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }
    
    private static Map<String, AdvertItemEventTypeEnum> VALUEMAP = new HashMap<String, AdvertItemEventTypeEnum>();
    
    static {
    	for (AdvertItemEventTypeEnum enums : AdvertItemEventTypeEnum.values()) {
    		VALUEMAP.put(enums.getValue(), enums);
		}
    }
    
    public static AdvertItemEventTypeEnum fromValue(String value) {
		return VALUEMAP.get(value);
	}
}
