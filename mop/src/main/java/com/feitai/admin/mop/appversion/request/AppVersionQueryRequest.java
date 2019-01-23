package com.feitai.admin.mop.appversion.request;

import com.feitai.admin.mop.base.BasePageReq;

import lombok.Data;

@Data
public class AppVersionQueryRequest extends BasePageReq {

	private String appCode;

	private String version;

	private String osType;

}
