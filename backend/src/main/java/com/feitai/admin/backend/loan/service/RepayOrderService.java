package com.feitai.admin.backend.loan.service;

import com.feitai.admin.backend.loan.entity.RepayOrderMore;
import com.feitai.admin.core.service.ClassPrefixDynamicSupportService;
import com.feitai.jieya.server.dao.bank.mapper.UserBankCardMapper;
import com.feitai.jieya.server.dao.bank.model.UserBankCard;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

import java.util.List;

@Service
@Slf4j
public class RepayOrderService extends ClassPrefixDynamicSupportService<RepayOrderMore> {

    @Autowired
    private UserBankCardMapper userBankCardMapper;

    public List<UserBankCard> findByUserIdAndRepay(Long userId){
        Example example = Example.builder(UserBankCard.class).andWhere(Sqls.custom().andEqualTo("userId",userId).andEqualTo("repayAble",1)).build();
        return userBankCardMapper.selectByExample(example);
    }

}
