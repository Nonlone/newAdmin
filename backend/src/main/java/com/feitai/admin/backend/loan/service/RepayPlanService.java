package com.feitai.admin.backend.loan.service;

import com.feitai.admin.core.service.DynamitSupportService;
import com.feitai.jieya.server.dao.loan.model.RepayPlan;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

import java.util.List;

@Service
@Slf4j
public class RepayPlanService extends DynamitSupportService<RepayPlan> {

	public List<RepayPlan> findByLoanOrderId(Long loanOrderId){
		Example example = Example.builder(RepayPlan.class).andWhere(Sqls.custom().andEqualTo("loanOrderId",loanOrderId)).build();
		return this.mapper.selectByExample(example);
	}

}
