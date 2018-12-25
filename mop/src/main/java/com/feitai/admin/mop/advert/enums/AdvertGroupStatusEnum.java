package com.feitai.admin.mop.advert.enums;

import lombok.Getter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;


/**
 * 广告组状态
 */
@ToString
@Getter
public enum AdvertGroupStatusEnum {
	
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
    AdvertGroupStatusEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }
    
    
    private static Map<Integer, AdvertGroupStatusEnum> VALUEMAP = new HashMap<Integer, AdvertGroupStatusEnum>();
    
    static {
    	for (AdvertGroupStatusEnum enums : AdvertGroupStatusEnum.values()) {
    		VALUEMAP.put(enums.getValue(), enums);
		}
    }
    
    public static AdvertGroupStatusEnum fromValue(int value) {
		return VALUEMAP.get(value);
	}
}
