package com.feitai.admin.backend.customer.service;

import com.feitai.admin.core.service.ClassPrefixDynamicSupportService;
import com.feitai.jieya.server.dao.callback.model.linkface.LinkfaceLivenessSelfieVerification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

@Service
@Slf4j
public class AuthdataLinkfaceLivenessSelfieVerificationService extends ClassPrefixDynamicSupportService<LinkfaceLivenessSelfieVerification> {

	public LinkfaceLivenessSelfieVerification findByUserId(Long userId) {
		Example example = Example.builder(LinkfaceLivenessSelfieVerification.class).andWhere(Sqls.custom().andEqualTo("userId",userId)).build();
		return getMapper().selectOneByExample(example);
	}

	public LinkfaceLivenessSelfieVerification findByCardId(Long cardId) {
		Example example = Example.builder(LinkfaceLivenessSelfieVerification.class).andWhere(Sqls.custom().andEqualTo("cardId",cardId)).build();
		return getMapper().selectOneByExample(example);
	}

}
