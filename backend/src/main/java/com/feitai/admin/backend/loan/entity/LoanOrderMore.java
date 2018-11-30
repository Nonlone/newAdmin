/**
 * @author 
 * @version 1.0
 * @since  2018-07-30 16:16:56
 * @desc 借款订单表
 */

package com.feitai.admin.backend.loan.entity;

import com.feitai.base.mybatis.One;
import com.feitai.jieya.server.dao.data.mapper.IdCardDataMapper;
import com.feitai.jieya.server.dao.data.model.IdCardData;
import com.feitai.jieya.server.dao.fund.mapper.FundMapper;
import com.feitai.jieya.server.dao.fund.model.Fund;
import com.feitai.jieya.server.dao.product.mapper.ProductMapper;
import com.feitai.jieya.server.dao.product.model.Product;
import com.feitai.jieya.server.dao.user.mapper.UserMapper;
import com.feitai.jieya.server.dao.user.model.User;
import lombok.Data;

import javax.persistence.Table;
import javax.persistence.Transient;

@Table(name = "t_loan_order")
@Data
public class LoanOrderMore extends com.feitai.jieya.server.dao.loan.model.LoanOrder{

	@Transient
	@One(classOfMapper = IdCardDataMapper.class, sourceField = "userId", targetField = "userId")
	private IdCardData idcard;

	@Transient
	@One(classOfMapper = ProductMapper.class, sourceField = "productId")
	private Product product;

	@Transient
	@One(classOfMapper = UserMapper.class, sourceField = "userId")
	private User user;

	@Transient
	@One(classOfMapper = FundMapper.class,sourceField = "payFundId")
	private Fund fund;

}

