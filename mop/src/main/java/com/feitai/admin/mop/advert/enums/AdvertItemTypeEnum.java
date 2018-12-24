package com.feitai.admin.mop.advert.enums;

import lombok.Getter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;


/**
 * 广告项类型
 */
@ToString
@Getter
public enum AdvertItemTypeEnum {
	
	IMAGE("image", "图片"),
	
	VIDEO("video", "可用"),
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
    AdvertItemTypeEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }
    
    private static Map<String, AdvertItemTypeEnum> VALUEMAP = new HashMap<String, AdvertItemTypeEnum>();
    
    static {
    	for (AdvertItemTypeEnum enums : AdvertItemTypeEnum.values()) {
    		VALUEMAP.put(enums.getValue(), enums);
		}
    }
    
    public static AdvertItemTypeEnum fromValue(String value) {
		return VALUEMAP.get(value);
	}
}
