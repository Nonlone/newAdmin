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

    /**
     * 资金方充值接口
     */
    @Value("${api.server.addCharge}")
    private String fundCharge;

    /**
     * 取消放款接口（飞钛否决）
     */
    @Value("${reject.cash.url}")
    private String rejectCash;


}
