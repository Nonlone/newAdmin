package com.feitai.admin.backend.fund.service;

import com.feitai.admin.core.service.ClassPrefixDynamicSupportService;
import com.feitai.jieya.server.dao.fund.model.FundAmountDetail;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

import java.util.List;
import java.util.Objects;

@Service
public class FundAmountDetailService extends ClassPrefixDynamicSupportService<FundAmountDetail> {

	public List<FundAmountDetail> queryFundChargeByFundId(Integer pageNo, Integer pageSize, Long fundId, Byte type){
		if (Objects.isNull(pageNo) || Objects.isNull(pageSize)){
			pageNo = 0;
			pageSize = 15;
		}
		PageHelper.startPage(pageNo, pageSize);
		Example example = Example.builder(FundAmountDetail.class)
				.andWhere(Sqls.custom().andEqualTo("fundId",fundId).andEqualTo("type",type))
				.orderByDesc("id")
				.build();
		return getMapper().selectByExample(example);
	}

	public List<FundAmountDetail> queryFundChargeByFundId(Integer pageNo, Integer pageSize, Long fundId) {
		if (Objects.isNull(pageNo) || Objects.isNull(pageSize)){
			pageNo = 0;
			pageSize = 15;
		}
		PageHelper.startPage(pageNo, pageSize);
		Example example = Example.builder(FundAmountDetail.class)
				.andWhere(Sqls.custom().andEqualTo("fundId",fundId))
				.orderByDesc("id")
				.build();
		return getMapper().selectByExample(example);
	}


}
