package com.feitai.admin.backend.supply.vo;

import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * detail:
 * author:
 * date:2018/11/30
 */
@Data
@ToString
public class LoanSupplyData {

    /***
     * 次数
     */
    private int count;

    /***
     * 提现订单id
     */
    private Long loanOrderId;

    /***
     * 补件内容（json）
     */
    private List<LoanSupplyInfo> supplyInfo;

}
