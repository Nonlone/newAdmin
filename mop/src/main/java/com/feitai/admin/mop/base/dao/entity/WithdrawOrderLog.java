package com.feitai.admin.mop.base.dao.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
* WithdrawOrderLog数据实体类
*/
@Data
@ToString(callSuper = true)
@Table(name = "t_withdraw_order_log")
public class WithdrawOrderLog {
    /**
	 *
	 */
	protected static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @Id
    private Long id;
    /**
     * 合伙人用户ID
     */
    private Long partnerUserId;
    /**
     * 订单ID(提现)
     */
    private Long orderId;
    /**
     * 审核时间
     */
    private Date auditTime;
    /**
     * 订单状态
     */
    private Integer originalStatus;
    /**
     * 订单状态
     */
    private Integer status;
    /**
     * 备注
     */
    private String remark;
    /**
     * 审核人账号
     */
    private String operator;
    /**
     * 创建时间
     */
    private Date createdTime;
}
