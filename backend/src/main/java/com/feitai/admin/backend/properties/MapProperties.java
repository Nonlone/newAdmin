package com.feitai.admin.backend.properties;

import com.alibaba.fastjson.JSONObject;
import com.feitai.jieya.server.dao.base.constant.PhotoType;
import com.google.common.collect.ImmutableMap;
import lombok.Data;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * 映射属性，提供Backend对应键值映射配置
 */
@Component
@Data
@ToString
public class MapProperties {


    @PostConstruct
    public void init() {
        authValueMap = ImmutableMap.copyOf(JSONObject.parseObject(authValueMapSource, Map.class));
        loanStatusMap = ImmutableMap.copyOf(JSONObject.parseObject(loanStatusSource, Map.class));
        photoTypeMap = ImmutableMap.copyOf(JSONObject.parseObject(photoTypeMapSource, Map.class));
    }

    @Value("${openCard.statusMap}")
    private String cardStatus;


    @Value("${backend.photoTypeMap}")
    private String photoTypeMapSource;

    private Map<String, String> photoTypeMap;


    /**
     * 获取授权项Map
     *
     * @param key
     * @return
     */
    public String getPhotoType(PhotoType key) {
        if (!CollectionUtils.isEmpty(photoTypeMap)
                && photoTypeMap.containsKey(key.toString().toUpperCase())) {
            return photoTypeMap.get(key.toString().toUpperCase());
        }
        return null;
    }


    /**
     * 认证数据项映射
     */
    @Value("${authdata.sourceMap}")
    private String source;

    private static Map<String,String> authdataSource;


    @Value("${backend.authValueMap}")
    private String authValueMapSource;

    private static Map<String, String> authValueMap;

    //放款状态
    @Value("${loanOrder.statusMap}")
    private String loanStatus;

    private static Map<String,String> loanStatusMap;


    /**
     * 获取授权项Map
     * @param key
     * @return
     */
    public String getAuthValue(String key) {
        if (!CollectionUtils.isEmpty(authValueMap)
                && authValueMap.containsKey(key)) {
            return authValueMap.get(key);
        }
        return null;
    }


    /**
     * 放款状态
     */
    @Value("${backend.loanStatusMap}")
    private String loanStatusSource;

    private static Map<String, String> loanStatusMap;


    /***
     * 获取取消放款状态
     * @param key
     * @return
     */
    public String getloanStatus(String key) {
        if (!CollectionUtils.isEmpty(loanStatusMap)
                && loanStatusMap.containsKey(key)) {
            return loanStatusMap.get(key);
        }
        return null;
    }

}
