package com.feitai.admin.backend.loan.vo;

import com.feitai.jieya.server.dao.loan.model.RepayOrder;
import com.feitai.jieya.server.dao.loan.model.RepayPlan;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import tk.mybatis.mapper.annotation.NameStyle;
import tk.mybatis.mapper.code.Style;

/**
 * detail:还款计划视图(Amount为应还，BalanceAmount为实还)
 * author:longhaoteng
 * date:2018/11/23
 */
@Data
public class RepayPlanVo extends RepayPlan {

    //本金
    @Builder.Default
    private String pincipalAmount = "0";
    @Builder.Default
    private String pincipalBalanceAmount = "0";

    //利息
    @Builder.Default
    private String interestAmount = "0";
    @Builder.Default
    private String interestBalanceAmount = "0";

    //罚息
    @Builder.Default
    private String overdueFineAmount = "0";
    @Builder.Default
    private String overdueFineBalanceAmount = "0";

    //罚息复利
    @Builder.Default
    private String overdueFineCompoundAmount = "0";
    @Builder.Default
    private String overdueFineCompoundBalanceAmount = "0";

    //复利
    @Builder.Default
    private String innerInteAmount = "0";
    @Builder.Default
    private String innerInteAalanceAmount = "0";

    //资金占用费
    @Builder.Default
    private String occupyFeeAmount = "0";
    @Builder.Default
    private String occupyFeeBalanceAmount = "0";

    //违约金
    @Builder.Default
    private String poundageAmtAmount = "0";
    @Builder.Default
    private String poundageAmtBalanceAmount = "0";

    //审批费
    @Builder.Default
    private String approveFeeAmount = "0";
    @Builder.Default
    private String approveFeeBalanceAmount = "0";

    //担保费
    @Builder.Default
    private String guaranteeFeeAmount = "0";
    @Builder.Default
    private String guaranteeFeeBalanceAmount = "0";

    //中间人服务费
    @Builder.Default
    private String brokerCommissionAmount = "0";
    @Builder.Default
    private String brokerCommissionBalanceAmount = "0";
}
