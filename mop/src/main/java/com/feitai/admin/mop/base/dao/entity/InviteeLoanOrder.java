package com.feitai.admin.mop.base.dao.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
* InviteeLoanOrder数据实体类
*/
@Data
@ToString(callSuper = true)
@Table(name = "t_invitee_loan_order")
public class InviteeLoanOrder {
    /**
	 *
	 */
	protected static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;
    /**
     * 被邀请用户ID
     */
    private Long userId;
    /**
     * 邀请人用户ID
     */
    private Long partnerUserId;
    /**
     * 授信卡ID
     */
    private Long cardId;
    /**
     * 贷款订单ID
     */
    private Long loanOrderId;
    /**
     * 申请金额
     */
    private BigDecimal applyAmount;
    /**
     * 实际借款金额
     */
    private BigDecimal loanAmount;
    /**
     * 订单状态
     */
    private Integer status;
    /**
     * 申请时间
     */
    private Date applyTime;
    /**
     * 放款时间
     */
    private Date payLoanTime;
    /**
     * 创建时间
     */
    private Date createdTime;
    /**
     * 更新时间
     */
    private Date updateTime;
}
