/**
 * @author 
 * @version 1.0
 * @since  2018-08-29 18:55:22
 * @desc 魔蝎回调任务数据
 */

package com.feitai.admin.backend.data.entity;

import lombok.Data;

import javax.persistence.Table;


@Table(name = "t_authdata_moxie")
@Data
public class Moxie implements java.io.Serializable{
	private static final long serialVersionUID = -1L;
	
	//alias
	public static final String TABLE_ALIAS = "魔蝎回调任务数据";
	public static final String ALIAS_ID = "主键";
	public static final String ALIAS_TASK_ID = "任务Id";
	public static final String ALIAS_USER_ID = "用户Id";
	public static final String ALIAS_CARD_ID = "卡号";
	public static final String ALIAS_PHONE = "手机号";
	public static final String ALIAS_NAME = "姓名";
	public static final String ALIAS_IDCARD = "身份证号";
	public static final String ALIAS_STATUS = "状态";
	public static final String ALIAS_LOGIN_MESSAGE = "登录消息";
	public static final String ALIAS_LOGIN_ERROR_CODE = "登录异常码";
	public static final String ALIAS_COLLECT_FAIL_MESSAGE = "采集失败信息";
	public static final String ALIAS_COLLECT_FAIL_ERROR_CODE = "采集失败异常码";
	public static final String ALIAS_BILLS = "账单数据";
	public static final String ALIAS_REPORT_MESSAGE = "报告消息";
	public static final String ALIAS_REPORT_ERROR_CODE = "报告失败码";
	public static final String ALIAS_CREATED_TIME = "创建时间";
	public static final String ALIAS_UPDATE_TIME = "更新时间";
	
	

	//可以直接使用: @Length(max=50,message="用户名长度不能大于50")显示错误消息
	//columns START
	private Long id;
	private String taskId;
	private Long userId;
	private String cardId;
	private String phone;
	private String name;
	private String idcard;
	private Byte status;
	private String loginMessage;
	private String loginErrorCode;
	private String collectFailMessage;
	private String collectFailErrorCode;
	private String bills;
	private String reportMessage;
	private String reportErrorCode;

	private java.util.Date createdTime;

	private java.util.Date updateTime;

}

