package com.feitai.admin.backend.loan.service;

import com.feitai.admin.backend.loan.entity.LoanOrderMore;
import com.feitai.admin.core.service.ClassPrefixDynamicSupportService;
import com.feitai.jieya.server.dao.contract.mapper.ContractFaddDetailMapper;
import com.feitai.jieya.server.dao.contract.model.ContractFaddDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

import java.util.List;

@Service
@Slf4j
public class LoanOrderService extends ClassPrefixDynamicSupportService<LoanOrderMore> {

    @Autowired
    private ContractFaddDetailMapper contractFaddDetailMapper;

    public List<ContractFaddDetail> getFaddByLoanOrder(Long loanOrderId){
        Example example = Example.builder(ContractFaddDetail.class).andWhere(Sqls.custom().andEqualTo("seqNo",loanOrderId)).build();
        return contractFaddDetailMapper.selectByExample(example);
    }

}
