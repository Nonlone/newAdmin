package com.feitai.admin.mop.base.enums;

import lombok.Getter;
import lombok.ToString;


/**
 * 奖金账户变更类型
 */
@ToString
@Getter
public enum BonusAccountChangeTypeEnum {
	
	BONUS_LOAN_NORMAL(1, "奖金-实时"),
	
	BONUS_LOAN_ADDITIONAL(2, "奖金-定时"),
	
	WITHDRAW_FROZEN(10, "提现-冻结"),
	
	WITHDRAW_SUCCESS(11, "提现-成功"),
	
	WITHDRAW_FAIL(12, "提现-失败"),
	;
	
	/**
     * 状态值
     */
    private int value;


    /**
     * 状态描述
     */
    private String desc;


    /**
     * @param value
     * @param desc
     */
    BonusAccountChangeTypeEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }
}
