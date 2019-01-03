package com.feitai.admin.messagecenter.dto;

import lombok.Data;
import lombok.ToString;

import java.util.Date;


/**
* 消息中心WeixinReply对象模型类
* @date 2018-12-18T18:02:24.119
*/
@Data
@ToString
public class WeixinReplyDto {
    /**
     * 主键id
     */
    private Integer id;
    /**
     * 关键字类别
     */
    private String keywordsType;
    /**
     * 关键字词条
     */
    private String keywords;
    /**
     * 关键字回复内容
     */
    private String content;
    /**
     * 是否删除 0：不删除 1:删除
     */
    private Integer isDelete;
    /**
     * 创建时间
     */
    private Long createTime;
    /**
     * 更新时间
     */
    private Long updateTime;
    /**
     * db创建时间
     */
    private Date dbCreateTime;
    /**
     * db更新时间
     */
    private Date dbUpdateTime;
}
