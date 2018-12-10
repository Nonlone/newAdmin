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

    /***
     * 补件总部发送次数限制
     */
    @Value("${backend.supply2dashu:3}")
    private int supply2dashu;

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

    /***
     * 内审通过
     */
    @Value("${api.server.dataApprovePass}")
    private String dataApprovePassUrl;

    /***
     * 补件发送至orderCenter
     */
    @Value("${api.orderCenter.supply2OrderCenter}")
    private String supply2OrderCenter;

}
