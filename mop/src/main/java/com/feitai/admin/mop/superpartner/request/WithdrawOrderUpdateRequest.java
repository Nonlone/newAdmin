package com.feitai.admin.mop.superpartner.request;

import lombok.Data;

/**
 * @Author qiuyunlong
 */
@Data
public class WithdrawOrderUpdateRequest {


    private Long userId;

    private Long orderId;

    private Integer status;

    private String operator;

    private String remark;

    private String type;

}
