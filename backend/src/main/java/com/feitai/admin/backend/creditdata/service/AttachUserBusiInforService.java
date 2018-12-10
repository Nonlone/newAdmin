package com.feitai.admin.backend.creditdata.service;

import com.feitai.admin.core.service.ClassPrefixDynamicSupportService;
import com.feitai.jieya.server.dao.attach.model.AttachUserBusiInfor;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

@Service
public class AttachUserBusiInforService extends ClassPrefixDynamicSupportService<AttachUserBusiInfor> {

	public AttachUserBusiInfor getAttachUserBusiInforByUserId(Long userId) {
		return getMapper().selectOneByExample(Example.builder(AttachUserBusiInfor.class)
				.andWhere(Sqls.custom().andEqualTo("userId", userId)).build());
	}
}
