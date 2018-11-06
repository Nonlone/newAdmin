/**
 * @author 
 * @version 1.0
 * @since  2018-08-06 18:34:31
 * @desc UserBankCard
 */

package com.feitai.admin.backend.customer.service;

import com.feitai.admin.core.service.ClassPrefixDynamicSupportService;
import com.feitai.admin.core.service.DynamitSupportService;
import com.feitai.jieya.server.dao.bank.model.UserBankCard;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

import java.util.List;

@Service
@Slf4j
public class UserBankCardService extends ClassPrefixDynamicSupportService<UserBankCard> {

    public List<UserBankCard> findByUserIdAndPay(Long id) {
		Example example = Example.builder(UserBankCard.class).andWhere(Sqls.custom().andEqualTo("id",id).andEqualTo("pay","1")).build();
		return this.mapper.selectByExample(example);
    }

	public List<UserBankCard> findByUserIdAndRepay(Long id) {
		Example example = Example.builder(UserBankCard.class).andWhere(Sqls.custom().andEqualTo("id",id).andEqualTo("repay","1")).build();
		return this.mapper.selectByExample(example);
	}

	public List<UserBankCard> findByUserId(Long id) {
		Example example = Example.builder(UserBankCard.class).andWhere(Sqls.custom().andEqualTo("userId",id)).build();
		return this.mapper.selectByExample(example);
	}

}
