package com.feitai.admin.backend.content.vo;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.feitai.jieya.server.dao.cms.model.Advertise;

public class AdvertiseVo extends Advertise{
   
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@Override
	public void setPublishTime(Date publishTime) {
		super.setPublishTime(publishTime);
	}
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@Override
	public void setExpiredTime(Date expiredTime) {
		super.setExpiredTime(expiredTime);
	}
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@Override
	public void setEffectiveTime(Date effectiveTime) {
		super.setEffectiveTime(effectiveTime);
	}
}
