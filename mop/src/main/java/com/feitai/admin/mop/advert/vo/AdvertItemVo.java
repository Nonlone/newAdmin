package com.feitai.admin.mop.advert.vo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.feitai.admin.mop.advert.dao.entity.AdvertItem;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import java.util.Date;
import java.util.List;

/**
 * @Author qiuyunlong
 */
@Data
public class AdvertItemVo {
    private Long id;
    private String title;
    private String subTitle;
    private String content;
    private String remark;
    private Integer playTime;
    private Integer totalPlayCount;
    private Integer perPlayCount;
    private Integer perPlayUnit;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date beginTime;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;
    private String showConfig;
    private String appVersion;
    private String appEventType;
    private String appLink;
    private String appParams;
    private String h5EventType;
    private String h5Link;
    private String h5Params;
    private String appStyle;
    private String h5Style;
    private String appExt;
    private String h5Ext;
    private String matchAppVersions;
    private String matchOsTypes;
    private Integer status;
    private String code;
    private Integer weight;
    private Long version;

    public static AdvertItemVo build(AdvertItem advertItem, List<Long> blockCodeList) {
        AdvertItemVo advertItemVo = new AdvertItemVo();
        BeanUtils.copyProperties(advertItem, advertItemVo);
        if (!blockCodeList.isEmpty()) {
            advertItemVo.setCode(StringUtils.join(blockCodeList, ","));

        }
        JSONObject event = JSON.parseObject(advertItem.getEvent());
        AdvertItem.AdvertItemEvent h5Event = event.getObject("h5", AdvertItem.AdvertItemEvent.class);
        AdvertItem.AdvertItemEvent appEvent = event.getObject("app", AdvertItem.AdvertItemEvent.class);
        if (appEvent != null) {
            advertItemVo.setAppEventType(appEvent.getType());
            advertItemVo.setAppLink(appEvent.getLink());
            advertItemVo.setAppParams(appEvent.getParams().toJSONString());
        }
        if (h5Event != null) {
            advertItemVo.setH5EventType(h5Event.getType());
            advertItemVo.setH5Link(h5Event.getLink());
            advertItemVo.setH5Params(h5Event.getParams().toJSONString());
        }

        AdvertItem.CommonJSONConfig style = JSON.parseObject(advertItem.getStyle(), AdvertItem.CommonJSONConfig.class);

        if (null != style) {
            advertItemVo.setH5Style(JSON.toJSONString(style.getH5()));
            advertItemVo.setAppStyle(JSON.toJSONString(style.getApp()));
        }

        AdvertItem.CommonJSONConfig ext = JSON.parseObject(advertItem.getExt(), AdvertItem.CommonJSONConfig.class);

        if (null != ext) {
            advertItemVo.setH5Ext(JSON.toJSONString(ext.getH5()));
            advertItemVo.setAppExt(JSON.toJSONString(ext.getApp()));
        }

        AdvertItem.AdvertItemMatchConfig matchConfig = JSON.parseObject(advertItem.getMatchConfig(), AdvertItem.AdvertItemMatchConfig.class);

        if (null != matchConfig) {
            AdvertItem.AdvertItemMatchConfigItem configItem = matchConfig.getDefaultConfig();

            if (null != configItem) {
                advertItemVo.setMatchAppVersions(configItem.getAppVersions());
                advertItemVo.setMatchOsTypes(configItem.getOsTypes());
            }
        }

        return advertItemVo;
    }
}
