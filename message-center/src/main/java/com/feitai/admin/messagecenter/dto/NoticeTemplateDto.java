package com.feitai.admin.messagecenter.dto;

import lombok.Data;
import lombok.ToString;

import java.util.Date;


/**
* 消息中心NoticeTemplate对象模型类
* @date 2018-12-17T15:19:20.595
*/
@Data
@ToString
public class NoticeTemplateDto {
    /**
     * id
     */
    private Long id;
    /**
     * 模板code
     */
    private String code;
    /**
     * 模板名称
     */
    private String name;
    /**
     * 推送渠道 1极光
     */
    private Integer sendType;
    /**
     * 内容标题
     */
    private String title;
    /**
     * 站内信
     */
    private String mailContent;
    /**
     * 短信
     */
    private String smsContent;
    /**
     * 推送
     */
    private String pushContent;
    /**
     * 
     */
    private String pushExtras;
    /**
     * 推送类型0:指定设备标识推送 1:android 2:ios 3:全平台(默认0)
     */
    private Integer pushType;
    /**
     * 是否禁用推送notification 0：不禁用 1：禁用
     */
    private Integer disablePushNotice;
    /**
     * 是否禁用推送msg 0：不禁用 1：禁用
     */
    private Integer disablePushMsg;
    /**
     * 极光短信模板id
     */
    private Integer smsTempId;
    /**
     * 短信模板状态 0审核中 1审核通过 2审核不通过 -1未创建或异常
     */
    private Integer smsTempStatus;
    /**
     * 状态 0失效 1有效
     */
    private Integer status;
    /**
     * 创建时间
     */
    private Date createdTime;
    /**
     * 更新时间
     */
    private Date updateTime;
}
