package com.feitai.admin.messagecenter.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class WeixinTemplateQueryParam extends BasePageReq{

    /**
     * 微信消息固定编码
     */
    private String weixinCode;

    /**
     * 微信消息说明
     */
    private String title;
}
