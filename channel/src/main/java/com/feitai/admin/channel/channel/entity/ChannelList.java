package com.feitai.admin.channel.channel.entity;

import com.feitai.base.mybatis.One;
import com.feitai.jieya.server.dao.card.mapper.CardMapper;
import com.feitai.jieya.server.dao.card.model.Card;
import com.feitai.jieya.server.dao.data.mapper.IdCardDataMapper;
import com.feitai.jieya.server.dao.data.mapper.PersonDataMapper;
import com.feitai.jieya.server.dao.data.model.IdCardData;
import com.feitai.jieya.server.dao.data.model.PersonData;
import com.feitai.jieya.server.dao.loan.model.LoanOrder;
import com.feitai.jieya.server.dao.product.mapper.ProductMapper;
import com.feitai.jieya.server.dao.product.model.Product;
import com.feitai.jieya.server.dao.user.mapper.UserMapper;
import com.feitai.jieya.server.dao.user.model.User;
import lombok.Data;

import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * detail:第三方使用的渠道用户信息
 * author:longhaoteng
 * date:2018/12/20
 */
@Table(name = "t_loan_order")
@Data
public class ChannelList extends LoanOrder {

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
    @One(classOfMapper = CardMapper.class, sourceField = "cardId")
    private Card card;

    @Transient
    @One(classOfMapper = PersonDataMapper.class, sourceField = "userId", targetField = "userId")
    private PersonData personData;

}
