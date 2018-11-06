package com.feitai.admin.backend.properties;

import lombok.Data;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * detail:url配置项
 * author:longhaoteng
 * date:2018/11/6
 */
@Component
@Data
@ToString
public class AppProperties {

    //取消放款
    @Value("${reject.cash.url}")
    private String rejectCash;


}
