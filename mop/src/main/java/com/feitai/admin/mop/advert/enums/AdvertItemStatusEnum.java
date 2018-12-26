package com.feitai.admin.mop.advert.enums;

import lombok.Getter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;


/**
 * 广告项状态
 */
@ToString
@Getter
public enum AdvertItemStatusEnum {
	
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
    AdvertItemStatusEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }
    
    private static Map<Integer, AdvertItemStatusEnum> VALUEMAP = new HashMap<Integer, AdvertItemStatusEnum>();
    
    static {
    	for (AdvertItemStatusEnum enums : AdvertItemStatusEnum.values()) {
    		VALUEMAP.put(enums.getValue(), enums);
		}
    }
    
    public static AdvertItemStatusEnum fromValue(int value) {
		return VALUEMAP.get(value);
	}
}
