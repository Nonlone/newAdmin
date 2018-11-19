package com.feitai.admin.mop.base.enums;

import lombok.Getter;
import lombok.ToString;


/**
 * 订单状态
 */
@ToString
@Getter
public enum SuperPartnerOrderStatusEnum {
	
	NEW(1, "新建"),
	
	PROCESSING(10, "处理中"),
	
	AUDIT_PASS(20, "审核通过"),
	
	AUDIT_REJECT(21, "审核拒绝"),
	
	FINISH(30, "已完成"),
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
    SuperPartnerOrderStatusEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static SuperPartnerOrderStatusEnum getByValue(int value) {
        for (SuperPartnerOrderStatusEnum statusEnum : SuperPartnerOrderStatusEnum.values()) {
            if (statusEnum.getValue() == value) {
                return statusEnum;
            }
        }
        return null;
    }
}
