/**
 * @author 
 * @version 1.0
 * @since  2018-08-27 10:05:35
 * @desc UserBankCardBind
 */

package com.feitai.admin.backend.fund.service;
import com.feitai.admin.core.service.DynamitSupportService;
import com.feitai.jieya.server.dao.bank.model.UserBankCardBind;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

import java.util.List;


@Component
@Transactional
@Slf4j
public class UserBankCardBindService extends DynamitSupportService<UserBankCardBind> {

	public List<UserBankCardBind> findByBankCardNo(String bankCardNo) {
		Example example = Example.builder(UserBankCardBind.class).andWhere(Sqls.custom().andEqualTo("bankCardNo",bankCardNo)).build();
		return getMapper().selectByExample(example);
	}
}
