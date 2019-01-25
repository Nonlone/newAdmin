package com.feitai.admin.mop.advert.enums;

import lombok.Getter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;


/**
 * 广告编辑副本关联类型枚举
 */
@ToString
@Getter
public enum AdvertEditCopyRelTypeEnum {
	
	ADVERT_ITEM(1, "广告内容"),
	
	ADVERT_BLOCK(2, "广告模块"),
	
	ADVERT_GROUP(3, "广告模组"),
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
    AdvertEditCopyRelTypeEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }
    
    
    private static Map<Integer, AdvertEditCopyRelTypeEnum> VALUEMAP = new HashMap<Integer, AdvertEditCopyRelTypeEnum>();
    
    static {
    	for (AdvertEditCopyRelTypeEnum enums : AdvertEditCopyRelTypeEnum.values()) {
    		VALUEMAP.put(enums.getValue(), enums);
		}
    }
    
    public static AdvertEditCopyRelTypeEnum fromValue(int value) {
		return VALUEMAP.get(value);
	}
}
