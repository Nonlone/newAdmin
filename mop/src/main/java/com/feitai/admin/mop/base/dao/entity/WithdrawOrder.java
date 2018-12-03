package com.feitai.admin.mop.base.dao.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.Date;

/**
* WithdrawOrder数据实体类
*/
@Data
@ToString(callSuper = true)
@Table(name = "t_withdraw_order")
public class WithdrawOrder {
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
     * 提现金额
     */
    private BigDecimal amount;
    /**
     * 所得税
     */
    private BigDecimal taxAmount;
    /**
     * 税后奖金
     */
    private BigDecimal afterTaxAmount;
    /**
     * 申请时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date applyTime;
    /**
     * 审核时间
     */
    private Date auditTime;
    /**
     * 订单状态(申请)
     */
    private Integer status;
    /**
     * 备注(拒绝原因)
     */
    private String remark;
    /**
     * 创建时间
     */
    private Date createdTime;
    /**
     * 更新时间
     */
    private Date updateTime;

    @Transient
    private OrderReceiverInfo orderReceiverInfo;
}
