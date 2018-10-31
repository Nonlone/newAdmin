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

    /**
     * 认证数据项映射
     */
    @Value("${authdata.sourceMap}")
    private String source;

    //取消放款
    @Value("${reject.cash.url}")
    private String rejectCash;

    //获取授权项Map
    public ImmutableMap<String, String> getSourceMap() {
        String source = this.source;
        Map<String, String> map = JSONObject.parseObject(source, new TypeReference<Map<String, String>>() {
        });
        return ImmutableMap.copyOf(map);
    }


    @Value("${backend.authValueMap}")
    private String authValueMapSource;

    private static Map<String, String> authValueMap;

    @PostConstruct
    public void init() {
        authValueMap = ImmutableMap.copyOf(JSONObject.parseObject(authValueMapSource, Map.class));
    }

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

}
