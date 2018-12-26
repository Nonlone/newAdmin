package com.feitai.admin.mop.superpartner.dao.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
* BonusAccount数据实体类
*/
@Data
@ToString(callSuper = true)
@Table(name = "t_bonus_account")
public class BonusAccount {
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
     * 可用金额
     */
    private BigDecimal balance;
    /**
     * 冻结金额
     */
    private BigDecimal frozenAmount;
    /**
     * 累计总金额
     */
    private BigDecimal aggregateAmount;
    /**
     * 提现总金额
     */
    private BigDecimal withdrawAmount;
    /**
     * 邀请人数
     */
    private Integer inviteCount;
    /**
     * 更新版本
     */
    private Integer updateVersion;
    /**
     * 创建时间
     */
    private Date createdTime;
    /**
     * 更新时间
     */
    private Date updateTime;
}
