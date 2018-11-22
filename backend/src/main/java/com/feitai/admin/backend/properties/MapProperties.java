package com.feitai.admin.backend.properties;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.feitai.jieya.server.dao.base.constant.*;
import com.feitai.utils.StringUtils;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;

import java.util.List;
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
        supplementMaterialMap = ImmutableMap.copyOf(JSON.parseObject(supplementMaterial,Map.class));
        supplementMaterialTypeMap = ImmutableMap.copyOf(JSON.parseObject(supplementMaterialType,Map.class));
        repayOrderStatusMap = ImmutableMap.copyOf(JSON.parseObject(repayOrderStatus,Map.class));
        channelSortList=ImmutableList.copyOf(channelSort.split(","));
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

    public String getAuthValue(AuthCode authCode,AuthSource authSource) {
        return getMapValue(authValueMap, authCode.getValue()+"-"+authSource.getValue());
    }

    /**
     * 放款状态
     */
    @Value("${backend.loanStatusMap}")
    private String loanStatusSource;

    private Map<String, String> loanStatusMap;

    public Map<String,String> getLoanStatusMap(){
        return this.loanStatusMap;
    }


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


    @Value("${backend.supplementMaterial}")
    private String supplementMaterial;

    private static Map<String, String> supplementMaterialMap;

    /***
     * 获取补件资料名称
     * @param key
     * @return
     */
    public String getSupplyMarterialNm(String key) {
        return getMapValue(supplementMaterialMap, key);
    }


    @Value("${backend.supplementMaterialType}")
    private String supplementMaterialType;

    private static Map<String, String> supplementMaterialTypeMap;

    /***
     * 获取补件资料名称
     * @param key
     * @return
     */
    public String getSupplyMarterialType(String key) {
        return getMapValue(supplementMaterialTypeMap, key);
    }



    @Value("${backend.repayOrderStatus}")
    private String repayOrderStatus;

    private static Map<String, String> repayOrderStatusMap;

    /***
     * 获取补件资料名称
     * @param key
     * @return
     */
    public String getRepayOrderStatus(String key) {
        return getMapValue(repayOrderStatusMap, key);
    }
    
    @Value("${backend.channelSort}")
    private String channelSort;
    private static List<String> channelSortList;
    
    /**
     * 获取二级渠道 的渠道大类
     * @return
     */
    public List<String> getChannelSortList(){
    	return channelSortList;
    }
}
