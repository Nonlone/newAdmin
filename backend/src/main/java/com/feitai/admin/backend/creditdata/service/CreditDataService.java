package com.feitai.admin.backend.creditdata.service;

import com.feitai.admin.backend.creditdata.entity.CreditData;
import com.feitai.admin.backend.creditdata.mapper.CreditDataMapper;
import com.feitai.admin.core.service.ClassPrefixDynamicSupportService;
import com.feitai.admin.core.service.DynamitSupportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

import java.util.List;

@Service
public class CreditDataService extends ClassPrefixDynamicSupportService<CreditData> {

	@Autowired
	private CreditDataMapper creditDataMapper;
	

    public String findByCardIdAndSource(long cardId, String source) {

		Example example = Example.builder(CreditData.class).andWhere(Sqls.custom().andEqualTo("cardId",cardId).andEqualTo("source",source)).orderByDesc("createdTime").build();
		List<CreditData> byCardIdAndSource = creditDataMapper.selectByExample(example);
		if(byCardIdAndSource.size()!=0){
			CreditData creditData = byCardIdAndSource.get(0);
			return creditData.getCreditData();
		}else{
			return "nulldata";
		}
	}
}
