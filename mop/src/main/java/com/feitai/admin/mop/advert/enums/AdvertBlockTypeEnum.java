package com.feitai.admin.mop.advert.enums;

import lombok.Getter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;


/**
 * 广告块类型
 */
@ToString
@Getter
public enum AdvertBlockTypeEnum {
	
	BANNER(1, "横幅组"),
	
	VIEWPAGER(2, "轮播图"),
	
	NOTICE(3, "公告"),
	
	FLASH(4, "闪屏"),
	;
	
	/**
     * 值
     */
    private int value;


    /**
     * 描述
     */
    private String desc;


    /**
     * @param value
     * @param desc
     */
    AdvertBlockTypeEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }
    
    private static final AdvertBlockTypeEnum[] NeedMultimediaTypes = new AdvertBlockTypeEnum[] {
    		BANNER,VIEWPAGER,FLASH};
    
    private static Map<Integer, AdvertBlockTypeEnum> VALUEMAP = new HashMap<Integer, AdvertBlockTypeEnum>();
    
    static {
    	for (AdvertBlockTypeEnum enums : AdvertBlockTypeEnum.values()) {
    		VALUEMAP.put(enums.getValue(), enums);
		}
    }
    
    public static AdvertBlockTypeEnum fromValue(int value) {
		return VALUEMAP.get(value);
	}
    
    public static AdvertBlockTypeEnum[] getNeedMultimediaTypes() {
		return NeedMultimediaTypes;
	}
}
