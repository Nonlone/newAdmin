package com.feitai.admin.mop.superpartner.vo;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class PartnerSettleResp {

	private Long id;
	/**
	 * 用户ID
	 */
	private Long userId;
	/**
	 * 注册手机号
	 */
	private Long phone;
	/**
	 * 注册时间
	 */
	private Date registerTime;
	/**
	 * 邀请码
	 */
	private String inviteCode;
	/**
	 * 账号类型 1:个人,2:企业
	 */
	private Integer type;
	/**
	 * 账号状态 1:激活,2:冻结
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

	private String im;

	private String periods;

	private String originalPeriod;
}
