package com.feitai.admin.messagecenter.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class WeixinReplyQueryDto extends BasePageReq{

    /**
     * 关键字词条
     */
    private String keywords;

    /**
     * 关键字类别
     */
    private String keywordsType;
}
