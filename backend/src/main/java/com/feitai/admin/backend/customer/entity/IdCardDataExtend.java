package com.feitai.admin.backend.customer.entity;

import com.feitai.base.mybatis.One;
import com.feitai.jieya.server.dao.data.model.IdCardData;
import com.feitai.jieya.server.dao.user.mapper.UserMapper;
import com.feitai.jieya.server.dao.user.model.User;
import lombok.Data;

import javax.persistence.Transient;

@Data
public class IdCardDataExtend extends IdCardData {

    @Transient
    @One(classOfMapper = UserMapper.class, sourceField = "userId")
    private User user;
}
