package com.feitai.admin.mop.advert.vo;

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
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author qiuyunlong
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PreviewVo {
    private String type;
    private String content;
    private String eventType;
    private String event;
    
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
    	String eventType = "";
    	String event = "";
    	
    	//获得默认展示配置
    	AdvertItemShowConfigItem defaultItem = getDefaultShowConfig(advertItem.getShowConfig());
    	//获得默认事件配置
    	AdvertItemEvent defaultEvent = getDefaultEventConfig(advertItem.getEvent());
    	
    	if (null == defaultItem) {
    		content = advertItem.getContent();
    	}
    	else {
    		type = defaultItem.getType();
    		content = defaultItem.getUrl();
    	}
    	
    	if (null != defaultEvent) {
			AdvertItemEventTypeEnum typeEnum = AdvertItemEventTypeEnum.fromValue(defaultEvent.getType());
    		eventType = null == typeEnum ? "" : typeEnum.getDesc();
    		event = defaultEvent.getLink();
    	}
    	
    	return new PreviewVo(type, content, eventType, event);
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
	
	
	public static AdvertItemEvent getDefaultEventConfig(String configValue) {

		if (StringUtils.isBlank(configValue)) {
			return null;
		}
		
		JSONObject config = JSON.parseObject(configValue);

		AdvertItemEvent event = config.getObject(CommonJSONConfig.APP, AdvertItemEvent.class);
		
		if (null != event) {
			return event;
		}
		
		return config.getObject(CommonJSONConfig.H5, AdvertItemEvent.class);
	}
}
