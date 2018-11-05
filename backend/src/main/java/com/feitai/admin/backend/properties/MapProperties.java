package com.feitai.admin.backend.properties;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.ImmutableMap;
import lombok.Data;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
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
        authdataSource = ImmutableMap.copyOf(JSONObject.parseObject(source,Map.class));
        authValueMap = ImmutableMap.copyOf(JSONObject.parseObject(authValueMapSource, Map.class));
        loanStatusMap = ImmutableMap.copyOf(JSONObject.parseObject(loanStatus,Map.class));
    }

    @Value("${openCard.statusMap}")
    private String cardStatus;

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


    //取消放款
    @Value("${reject.cash.url}")
    private String rejectCash;


    /**
     * 获取授权项Map
     * @param key
     * @return
     */
    public String getAuthValue(String key) {
        if (!CollectionUtils.isEmpty(authValueMap)) {
            return authValueMap.get(key);
        }
        return null;
    }


    /***
     * 获取取消放款状态
     * @param key
     * @return
     */
    public String getloanStatus(String key){
        if(!CollectionUtils.isEmpty(loanStatusMap)){
            return loanStatusMap.get(key);
        }
        return null;
    }

}
