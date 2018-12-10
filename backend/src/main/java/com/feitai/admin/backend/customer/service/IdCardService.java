package com.feitai.admin.backend.customer.service;

import com.feitai.admin.backend.customer.entity.IdCardDataExtend;
import com.feitai.admin.core.service.ClassPrefixDynamicSupportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

@Service
@Slf4j
public class IdCardService extends ClassPrefixDynamicSupportService<IdCardDataExtend> {

	public IdCardDataExtend findByUserId(Long userId) {
		Example example = Example.builder(IdCardDataExtend.class).andWhere(Sqls.custom().andEqualTo("userId",userId)).build();
		return walkProcess(getMapper().selectOneByExample(example));
	}

}
