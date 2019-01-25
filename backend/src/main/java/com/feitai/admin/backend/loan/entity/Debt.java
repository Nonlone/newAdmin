package com.feitai.admin.backend.loan.entity;

import javax.persistence.Table;
import javax.persistence.Transient;

import com.feitai.base.mybatis.One;
import com.feitai.jieya.server.dao.data.mapper.IdCardDataMapper;
import com.feitai.jieya.server.dao.data.model.IdCardData;
import com.feitai.jieya.server.dao.loan.mapper.LoanOrderMapper;
import com.feitai.jieya.server.dao.loan.model.LoanOrder;
import com.feitai.jieya.server.dao.loan.model.RepayPlan;
import com.feitai.jieya.server.dao.user.mapper.UserMapper;
import com.feitai.jieya.server.dao.user.model.User;

import lombok.Data;

@Table(name = "t_repay_plan")
@Data
public class Debt extends RepayPlan{
	@Transient
	@One(classOfMapper = IdCardDataMapper.class, sourceField = "userId", targetField = "userId")
	private IdCardData idcard;

	@Transient
	@One(classOfMapper = LoanOrderMapper.class, sourceField = "loanOrderId")
	private LoanOrder loanOrder;

	@Transient
	@One(classOfMapper = UserMapper.class, sourceField = "userId")
	private User user;
}
