package com.feitai.admin.backend.customer.entity;

import com.feitai.base.mybatis.One;
import com.feitai.jieya.server.dao.data.model.IdCardData;
import com.feitai.jieya.server.dao.user.mapper.UserMapper;
import com.feitai.jieya.server.dao.user.model.User;
import lombok.Data;

import javax.persistence.Table;
import javax.persistence.Transient;

@Data
@Table(
        name = "t_data_idcard"
)
public class IdCardDataExtend extends IdCardData {

    public final static String CERTIFIED_TRUE="1";

    public final static String CERTIFIED_FALSE="0";

    @Transient
    @One(classOfMapper = UserMapper.class, sourceField = "userId")
    private User user;
}
