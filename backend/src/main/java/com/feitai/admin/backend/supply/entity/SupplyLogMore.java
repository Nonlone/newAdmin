package com.feitai.admin.backend.supply.entity;

import com.feitai.admin.backend.loan.entity.LoanOrderMore;
import com.feitai.admin.backend.loan.mapper.LoanOrderMoreMapper;
import com.feitai.base.mybatis.One;
import com.feitai.jieya.server.dao.data.mapper.IdCardDataMapper;
import com.feitai.jieya.server.dao.data.model.IdCardData;
import com.feitai.jieya.server.dao.loan.model.LoanSupplyLog;
import com.feitai.jieya.server.dao.user.mapper.UserMapper;
import com.feitai.jieya.server.dao.user.model.User;
import lombok.Data;

import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * detail:补件记录
 * author:longhaoteng
 * date:2018/11/19
 */
@Data
@Table(name = "t_loan_supply_log")
public class SupplyLogMore extends LoanSupplyLog {

    @Transient
    @One(classOfMapper = LoanOrderMoreMapper.class, sourceField = "loanOrderId")
    private LoanOrderMore loanOrder;

    private Long userId;

    @Transient
    @One(classOfMapper = IdCardDataMapper.class, sourceField = "userId", targetField = "userId")
    private IdCardData idCardData;

    @Transient
    @One(classOfMapper = UserMapper.class, sourceField = "userId")
    private User user;

    @Transient
    private String fundName;

}
