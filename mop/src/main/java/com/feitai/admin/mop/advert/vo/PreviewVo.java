package com.feitai.admin.mop.advert.vo;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.feitai.admin.mop.advert.dao.entity.AdvertItem;
import com.feitai.admin.mop.advert.dao.entity.AdvertItem.AdvertItemEvent;
import com.feitai.admin.mop.advert.dao.entity.AdvertItem.AdvertItemShowConfigItem;
import com.feitai.admin.mop.advert.dao.entity.AdvertItem.CommonJSONConfig;
import com.feitai.admin.mop.advert.enums.AdvertItemEventTypeEnum;
import com.feitai.admin.mop.base.ListUtils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author qiuyunlong
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PreviewVo {
    private String type;
    private String content;
    private String appEventType;
    private String appEvent;
    private String h5EventType;
    private String h5Event;
    
    public static List<PreviewVo> from(List<AdvertItem> advertItems) {
    	List<PreviewVo> list = new ArrayList<>();
    	
    	if (ListUtils.isEmpty(advertItems)) {
    		return list;
    	}
    	
    	for (AdvertItem advertItem : advertItems) {
    		PreviewVo vo = from(advertItem);
    		
    		if (null != vo) {
    			list.add(vo);
    		}
    	}
    	
    	return list;
    }
    
    
    private static PreviewVo from(AdvertItem advertItem) {
    	if (null == advertItem) {
    		return null;
    	}
    	
    	String type = "";
    	String content = "";
    	String appEventType = "";
    	String appEvent = "";
    	String h5EventType = "";
    	String h5Event = "";
    	
    	//获得默认展示配置
    	AdvertItemShowConfigItem defaultItem = getDefaultShowConfig(advertItem.getShowConfig());
    	//获得事件配置
    	AdvertItemEvent appEventConfig = getAppEventConfig(advertItem.getEvent());
    	//获得事件配置
    	AdvertItemEvent h5pEventConfig = getH5EventConfig(advertItem.getEvent());
    	
    	if (null == defaultItem) {
    		content = advertItem.getContent();
    	}
    	else {
    		type = defaultItem.getType();
    		content = defaultItem.getUrl();
    	}
    	
    	if (null != appEventConfig) {
			AdvertItemEventTypeEnum typeEnum = AdvertItemEventTypeEnum.fromValue(appEventConfig.getType());
			appEventType = null == typeEnum ? "" : typeEnum.getDesc();
			appEvent = appEventConfig.getLink();
    	}
    	
    	if (null != h5pEventConfig) {
			AdvertItemEventTypeEnum typeEnum = AdvertItemEventTypeEnum.fromValue(h5pEventConfig.getType());
			h5EventType = null == typeEnum ? "" : typeEnum.getDesc();
			h5Event = h5pEventConfig.getLink();
    	}
    	
    	return new PreviewVo(type, content, appEventType, appEvent, h5EventType, h5Event);
    }
    
    
    /**
	 * 获得默认展示配置
	 * @param configValue
	 * @return
	 */
	private static AdvertItemShowConfigItem getDefaultShowConfig(String configValue) {
		
		List<AdvertItemShowConfigItem> items = JSONArray.parseArray(configValue,
				AdvertItemShowConfigItem.class);

		if (ListUtils.isEmpty(items)) {
			return null;
		}

		AdvertItemShowConfigItem defaultItem = items.get(0);

		if (1 == items.size()) {
			return defaultItem;
		}

		for (AdvertItemShowConfigItem item : items) {
			if (Boolean.TRUE.equals(item.getDefaultItem())) {
				defaultItem = item;
			}
		}

		return defaultItem;
	}
	
	
	private static AdvertItemEvent getAppEventConfig(String configValue) {
		return getEventConfig(configValue, CommonJSONConfig.APP);
	}
	
	
	private static AdvertItemEvent getH5EventConfig(String configValue) {
		return getEventConfig(configValue, CommonJSONConfig.H5);
	}
	
	
	private static AdvertItemEvent getEventConfig(String configValue, String type) {

		if (StringUtils.isBlank(configValue)) {
			return null;
		}
		
		JSONObject config = JSON.parseObject(configValue);

		return config.getObject(type, AdvertItemEvent.class);
	}
}
