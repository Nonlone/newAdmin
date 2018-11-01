package com.feitai.admin.backend.customer.service;

import com.feitai.admin.backend.customer.entity.IdCardDataExtend;
import com.feitai.admin.core.service.ClassPrefixDynamicSupportService;
import com.feitai.jieya.server.dao.data.model.IdCardData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

@Service
@Slf4j
public class IdcardService extends ClassPrefixDynamicSupportService<IdCardDataExtend> {

	public IdCardData findByUserId(Long userId) {
		Example example = Example.builder(IdCardData.class).andWhere(Sqls.custom().andEqualTo("userId",userId)).build();
		return getMapper().selectOneByExample(example);
	}

}
