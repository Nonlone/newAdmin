package com.feitai.admin.backend.customer.entity;

import com.feitai.base.mybatis.One;
import com.feitai.jieya.server.dao.data.model.IdCardData;
import com.feitai.jieya.server.dao.user.mapper.UserMapper;
import com.feitai.jieya.server.dao.user.model.User;

import javax.persistence.Transient;

public class Idcard extends IdCardData {

    @Transient
    @One(classOfMapper = UserMapper.class, targetField = "userId")
    private User user;
}
