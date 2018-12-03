package com.feitai.admin.backend.loan.vo;

import lombok.Data;

/**
 * detail:还款成分列表
 * author:longhaoteng
 * date:2018/7/26
 */
@Data
public class OrderPlande {

    //    期数
    private Short term;
    //    还款日期
    private String dueDate;
    //    实还日期
    private String realDate;
    //    当期期供
    private Double amount;
    //    应还本金
    private Double pincipalAmount;
    //    应还利息
    private Double interestAmount;
    //    应还评审费
    private Double approveFeeAmount;
    //    应还担保费
    private Double guaranteeFeeAmount;
    //    应还违约金
    private Double overdueFineAmount;
    //    实还本金
    private Double pincipalBalance;
    //    实还利息
    private Double interestBalance;
    //    实还评审费
    private Double approveFeeBalance;
    //    实还担保费
    private Double guaranteeFeeBalance;
    //    实还违约金
    private Double overdueFineBalance;
    //    还款状态
    private String stuts;

}
