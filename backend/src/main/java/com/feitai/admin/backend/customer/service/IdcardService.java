package com.feitai.admin.backend.customer.service;

import com.feitai.admin.backend.customer.entity.Idcard;
import com.feitai.admin.core.service.DynamitSupportService;
import com.feitai.jieya.server.dao.data.mapper.IdCardDataMapper;
import com.feitai.jieya.server.dao.data.model.IdCardData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

@Service
@Slf4j
public class IdcardService extends DynamitSupportService<Idcard> {

	@Autowired
	private IdCardDataMapper idCardDataMapper;

	public IdCardData findByUserId(Long userId) {
		Example example = Example.builder(IdCardData.class).andWhere(Sqls.custom().andEqualTo("userId",userId)).build();
		return idCardDataMapper.selectOneByExample(example);
	}

}
