package com.feitai.admin.messagecenter.dto;

import lombok.Data;
import lombok.ToString;

import java.util.Date;


/**
* 消息中心WeixinTemplate对象模型类
* @date 2018-12-17T15:19:20.595
*/
@Data
@ToString
public class WeixinTemplateDto {
    /**
     * 主键id
     */
    private Integer id;
    /**
     * 微信消息说明
     */
    private String title;
    /**
     * 微信消息固定编码
     */
    private String weixinCode;
    /**
     * 消息模板id
     */
    private String weixinTempId;
    /**
     * 消息模板内容
     */
    private String tempContent;
    /**
     * 消息模板的首行内容
     */
    private String tempFirstDetail;
    /**
     * 消息模板关键字1
     */
    private String keyword1;
    /**
     * 消息模板关键字2
     */
    private String keyword2;
    /**
     * 消息模板关键字3
     */
    private String keyword3;
    /**
     * 消息模板关键字4
     */
    private String keyword4;
    /**
     * 消息模板关键字5
     */
    private String keyword5;
    /**
     * 消息模板的末行备注内容
     */
    private String tempRemarkDetail;
    /**
     * 
     */
    private Integer isDelete;
    /**
     * 
     */
    private Date createdTime;
    /**
     * 
     */
    private Date updateTime;
    /**
     * 
     */
    private Date dbCreateTime;
    /**
     * 
     */
    private Date dbUpdateTime;
}
