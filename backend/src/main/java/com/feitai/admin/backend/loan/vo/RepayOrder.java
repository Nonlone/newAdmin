package com.feitai.admin.backend.loan.vo;

import com.feitai.admin.backend.loan.entity.LoanOrderMore;
import com.feitai.jieya.server.dao.data.model.IdCardData;
import com.feitai.jieya.server.dao.loan.model.RepayPlan;
import com.feitai.jieya.server.dao.user.model.User;
import lombok.Data;

import java.util.Date;

/**
 * @author
 * @version 1.0
 * @desc 还款支付订单表
 * @since 2018-07-25 21:19:09
 */
@Data
public class RepayOrder {
    private static final long serialVersionUID = -1L;

    private Long id;

    private Long userId;

    private Long loanOrderId;

    private Long repayPlanId;

    private Double amount;

    private Byte payType;

    private String payAccount;

    private Date createdTime;

    private Date updateTime;

    private Integer updateVersion;

    private Byte status;

    private RepayPlan repayPlan;

    private IdCardData idcard;

    private LoanOrderMore loanOrder;

    private User userIn;

    private Date repayDate;

    private Date dueDate;
    //columns END


}


