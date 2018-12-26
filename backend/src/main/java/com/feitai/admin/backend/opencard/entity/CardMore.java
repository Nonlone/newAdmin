/**
 * @author
 * @version 1.0
 * @since  2018-08-06 09:28:35
 * @desc 用户持有对应信用产品记录
 */

package com.feitai.admin.backend.opencard.entity;
import com.feitai.base.mybatis.One;
import com.feitai.jieya.server.dao.card.model.Card;
import com.feitai.jieya.server.dao.data.mapper.IdCardDataMapper;
import com.feitai.jieya.server.dao.data.model.IdCardData;
import com.feitai.jieya.server.dao.product.mapper.ProductMapper;
import com.feitai.jieya.server.dao.product.model.Product;
import com.feitai.jieya.server.dao.user.mapper.UserMapper;
import com.feitai.jieya.server.dao.user.model.User;
import lombok.Data;

import javax.persistence.Table;
import javax.persistence.Transient;


@Data
@Table(name = "t_card")
public class CardMore extends Card {


	@Transient
	@One(classOfMapper = IdCardDataMapper.class, sourceField = "userId", targetField = "userId")
	private IdCardData idCard;

	@Transient
	@One(classOfMapper = ProductMapper.class, sourceField =  "productId" )
	private Product product;

	@Transient
	@One(classOfMapper = UserMapper.class, sourceField = "userId")
	private User user;

	@Override
	public boolean getExpired() {
		return true;
	}


}

