package com.feitai.admin.backend.customer.service;

import com.feitai.admin.core.service.ClassPrefixDynamicSupportService;
import com.feitai.jieya.server.dao.bank.model.BankSupport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

@Service
@Slf4j
public class BankSupportService extends ClassPrefixDynamicSupportService<BankSupport> {

	public BankSupport findByBankCode(String bankCode) {
		Example example = Example.builder(BankSupport.class).andWhere(Sqls.custom().andEqualTo("bankCode",bankCode)).build();
		return mapper.selectOneByExample(example);
	}

}
