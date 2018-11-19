package com.feitai.admin.mop.base.dao.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
* BonusAccountLog数据实体类
*/
@Data
@ToString(callSuper = true)
@Table(name = "t_bonus_account_log")
public class BonusAccountLog {
    /**
	 *
	 */
	protected static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;
    /**
     * 合伙人用户ID
     */
    private Long partnerUserId;
    /**
     * 变更类型(实时奖金,定时奖金,提现申请等)
     * BonusAccountChangeTypeEnum
     */
    private Integer type;
    /**
     * 关联变更ID(奖金使用奖金订单ID,提现时使用提现订单ID)
     */
    private Long refId;
    /**
     * 标题
     */
    private String title;
    /**
     * 变更金额
     */
    private BigDecimal offset;
    /**
     * 变更后可用金额
     */
    private BigDecimal balance;
    /**
     * 变更后冻结金额
     */
    private BigDecimal frozenAmount;
    /**
     * 变更后累计总金额
     */
    private BigDecimal aggregateAmount;
    /**
     * 变更后提现总金额
     */
    private BigDecimal withdrawAmount;
    /**
     * 变更后邀请人数
     */
    private Integer inviteCount;
    /**
     * 是否可用展示(用于账户明细)
     */
    private Boolean display;
    /**
     * 创建时间
     */
    private Date createdTime;
}
