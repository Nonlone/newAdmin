package com.feitai.admin.backend.customer.service;

import com.feitai.admin.core.service.ClassPrefixDynamicSupportService;
import com.feitai.jieya.server.dao.callback.model.linkface.LinkfaceLivenessIdNumberVerifcation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;


@Service
@Slf4j
public class AuthdataLinkfaceLivenessIdnumberVerificationService extends ClassPrefixDynamicSupportService<LinkfaceLivenessIdNumberVerifcation> {

	public LinkfaceLivenessIdNumberVerifcation findByUserId(Long userId) {
		Example example = Example.builder(LinkfaceLivenessIdNumberVerifcation.class).andWhere(Sqls.custom().andEqualTo("userId",userId)).build();
		return getMapper().selectOneByExample(example);
	}

}
