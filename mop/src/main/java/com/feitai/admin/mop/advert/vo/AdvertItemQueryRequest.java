package com.feitai.admin.mop.advert.vo;


import com.feitai.admin.mop.base.BasePageReq;
import lombok.Data;
import lombok.ToString;

/**
 * @Author qiuyunlong
 */
@Data
@ToString(callSuper = true)
public class AdvertItemQueryRequest extends BasePageReq {

    private Long userId;

    private Integer status;

    private Long orderId;

    private Long blockId;

    private String title;
}
