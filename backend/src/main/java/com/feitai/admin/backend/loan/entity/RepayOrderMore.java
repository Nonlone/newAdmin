/**
 * @author 
 * @version 1.0
 * @since  2018-07-25 21:19:09
 * @desc 还款支付订单表
 */

package com.feitai.admin.backend.loan.entity;

import com.feitai.base.mybatis.One;
import com.feitai.jieya.server.dao.data.mapper.IdCardDataMapper;
import com.feitai.jieya.server.dao.data.model.IdCardData;
import com.feitai.jieya.server.dao.loan.mapper.LoanOrderMapper;
import com.feitai.jieya.server.dao.loan.mapper.RepayPlanMapper;
import com.feitai.jieya.server.dao.loan.model.LoanOrder;
import com.feitai.jieya.server.dao.loan.model.RepayPlan;
import com.feitai.jieya.server.dao.user.mapper.UserMapper;
import com.feitai.jieya.server.dao.user.model.User;
import lombok.Data;

import javax.persistence.Table;
import javax.persistence.Transient;


@Table(name = "t_repay_order")
@Data
public class RepayOrderMore extends com.feitai.jieya.server.dao.loan.model.RepayOrder{

	@Transient
	@One(classOfMapper = IdCardDataMapper.class, sourceField = "userId", targetField = "userId")
	private IdCardData idcard;

	@Transient
	@One(classOfMapper = LoanOrderMapper.class, sourceField = "loanOrderId")
	private LoanOrder loanOrder;

	@Transient
	@One(classOfMapper = UserMapper.class, sourceField = "userId")
	private User user;

	@Transient
	@One(classOfMapper = RepayPlanMapper.class, sourceField = "repayPlanId")
	private RepayPlan repayPlan;

}

