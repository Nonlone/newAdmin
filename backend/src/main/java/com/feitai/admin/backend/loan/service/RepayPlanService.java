package com.feitai.admin.backend.loan.service;

import com.feitai.admin.core.service.ClassPrefixDynamicSupportService;
import com.feitai.jieya.server.dao.loan.model.RepayPlan;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

import java.util.List;

@Service
@Slf4j
public class RepayPlanService extends ClassPrefixDynamicSupportService<RepayPlan> {

	public List<RepayPlan> findByLoanOrderId(Long loanOrderId){
		Example example = Example.builder(RepayPlan.class).andWhere(Sqls.custom().andEqualTo("loanOrderId",loanOrderId)).build();
		return getMapper().selectByExample(example);
	}
    
	public List<RepayPlan> findByLoanOrderIdAndTerm(Long loanOrderId,Short term){
		Example example = Example.builder(RepayPlan.class).andWhere(Sqls.custom().andEqualTo("loanOrderId",loanOrderId)
				.andEqualTo("term", term)).build();
		return getMapper().selectByExample(example);
	}
}
