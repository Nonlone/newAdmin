package com.feitai.admin.mop.base.dao.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
* BonusOrder数据实体类
*/
@Data
@ToString(callSuper = true)
@Table(name = "t_bonus_order")
public class BonusOrder {
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
     * 奖金类型(实时奖金,定时奖金)
     */
    private Integer type;
    /**
     * 奖金标识(实时奖金使用贷款订单ID,定时奖金使用估计规则生成的ID)
     */
    private String bonusIdentify;
    /**
     * 奖金标题
     */
    private String title;
    /**
     * 奖金说明
     */
    private String desc;
    /**
     * 参考金额(实时单笔,定时汇总)
     */
    private BigDecimal refAmount;
    /**
     * 奖金
     */
    private BigDecimal amount;
    /**
     * 创建时间
     */
    private Date createdTime;
}
