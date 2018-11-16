package com.feitai.admin.backend.opencard.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.feitai.admin.core.service.ClassPrefixDynamicSupportService;
import com.feitai.jieya.server.dao.base.constant.ProcessSegment;
import com.feitai.jieya.server.dao.data.mapper.TongDunDataMapper;
import com.feitai.jieya.server.dao.data.model.TongDunData;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;
@Service
public class TongDunDataService extends ClassPrefixDynamicSupportService<TongDunData>{
	
	  @Autowired
       private TongDunDataMapper tongDunDataMapper;
	  
	  public TongDunData getTonDunData(Long userId,Long cardId){
		  Example example=Example.builder(TongDunData.class).andWhere(Sqls.custom()
				  .andEqualTo("userId", userId).andEqualTo("cardId", cardId)
				  .andEqualTo("enable", 1).andEqualTo("segment", ProcessSegment.OPENCARD.getValue())).build();
		  return tongDunDataMapper.selectOneByExample(example);
	  }
}
