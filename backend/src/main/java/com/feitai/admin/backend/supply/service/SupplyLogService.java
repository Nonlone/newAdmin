package com.feitai.admin.backend.supply.service;

import com.feitai.admin.backend.supply.entity.SupplyLogMore;
import com.feitai.admin.core.service.ClassPrefixDynamicSupportService;
import com.feitai.jieya.server.dao.loan.mapper.LoanSupplyLogMapper;
import com.feitai.jieya.server.dao.loan.model.LoanSupplyLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

import java.util.List;

/**
 * detail:补件记录service
 * author:longhaoteng
 * date:2018/11/19
 */

@Service
@Slf4j
public class SupplyLogService extends ClassPrefixDynamicSupportService<SupplyLogMore> {

    @Autowired
    private LoanSupplyLogMapper loanSupplyLogMapper;

    public List<LoanSupplyLog> findByLoanOrder(Long loanOrderId){
        Example example = Example.builder(LoanSupplyLog.class).andWhere(Sqls.custom().andEqualTo("loanOrderId",loanOrderId)).orderByDesc("createdTime").build();
        return loanSupplyLogMapper.selectByExample(example);
    }

}
