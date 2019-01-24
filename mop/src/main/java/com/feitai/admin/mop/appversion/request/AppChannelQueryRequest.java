package com.feitai.admin.mop.appversion.request;

import lombok.Data;

@Data
public class AppChannelQueryRequest {

	/**
	 * 渠道大类
	 */
	private String channelSort;

	/**
	 * 渠道号
	 */
	private String channelCode;
}
