package com.feitai.admin.mop.superpartner.enums;

import lombok.Getter;
import lombok.ToString;


/**
 * 奖金类型
 */
@ToString
@Getter
public enum BonusTypeEnum {
	
	BONUS_LOAN_NORMAL(1, "奖金-实时"),
	
	BONUS_LOAN_ADDITIONAL(2, "奖金-定时"),
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
    BonusTypeEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }
}
