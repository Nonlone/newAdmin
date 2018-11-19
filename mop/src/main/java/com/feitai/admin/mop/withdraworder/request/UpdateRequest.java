package com.feitai.admin.mop.withdraworder.request;

import lombok.Data;

/**
 * @Author qiuyunlong
 */
@Data
public class UpdateRequest {


    private Long userId;

    private Long orderId;

    private Integer status;

    private String operator;

    private String remark;

    private String type;

}
