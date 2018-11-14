package com.feitai.admin.backend.properties;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.feitai.jieya.server.dao.base.constant.CardStatus;
import com.feitai.jieya.server.dao.base.constant.PhotoType;
import com.feitai.jieya.server.dao.base.constant.ProcessSegment;
import com.feitai.jieya.server.dao.base.constant.UserAuthStatus;
import com.feitai.utils.StringUtils;
import com.google.common.collect.ImmutableMap;
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
@ToString
public class MapProperties {


    @PostConstruct
    public void init() {
        cardStatusMap = ImmutableMap.copyOf(JSONObject.parseObject(cardStatusSource, Map.class));
        authValueMap = ImmutableMap.copyOf(JSONObject.parseObject(authValueMapSource, Map.class));
        loanStatusMap = ImmutableMap.copyOf(JSONObject.parseObject(loanStatusSource, Map.class));
        photoTypeMap = ImmutableMap.copyOf(JSONObject.parseObject(photoTypeMapSource, Map.class));
        valveRejectMap = ImmutableMap.copyOf(JSONObject.parseObject(valveRejectSource,Map.class));
        userAuthMap = ImmutableMap.copyOf(JSONObject.parseObject(userAuthMapSource,Map.class));
        segmentMap = ImmutableMap.copyOf(JSON.parseObject(segmentMapSource,Map.class));
    }

    @Value("${backend.segmentMap}")
    private String segmentMapSource;

    private Map<String,String> segmentMap;

    public String getSegment(ProcessSegment key){
        return getMapValue(segmentMap,key.name().toUpperCase());
    }

    @Value("${backend.userAuthMap}")
    private String userAuthMapSource;

    private Map<String,String> userAuthMap;

    public String getUserAuth(UserAuthStatus key){
        return getMapValue(userAuthMap,key.name().toUpperCase());
    }


    @Value("${backend.valveRejectMap}")
    private String valveRejectSource;

    private Map<String,String> valveRejectMap;

    public String getValveReject(String key){
        String value =  getMapValue(valveRejectMap,key);
        if(StringUtils.isBlank(value)){
            return key;
        }
        return value;
    }


    @Value("${backend.cardStatusMap}")
    private String cardStatusSource;

    private Map<String, String> cardStatusMap;

    public String getCardStatus(CardStatus key) {
        return getMapValue(cardStatusMap, Integer.toString(key.getValue()));
    }


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
        return getMapValue(photoTypeMap, key.toString().toUpperCase());
    }


    @Value("${backend.authValueMap}")
    private String authValueMapSource;

    private static Map<String, String> authValueMap;

    /**
     * 获取授权项Map
     *
     * @param key
     * @return
     */
    public String getAuthValue(String key) {
        return getMapValue(authValueMap, key);
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
        return getMapValue(loanStatusMap, key);
    }


    private String getMapValue(Map<String, String> map, String key) {
        if (!CollectionUtils.isEmpty(map)
                && map.containsKey(key)) {
            return map.get(key);
        }
        return null;
    }

}
