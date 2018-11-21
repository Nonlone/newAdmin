package com.feitai.admin.backend.opencard.service;

import com.feitai.admin.core.service.ClassPrefixDynamicSupportService;
import com.feitai.jieya.server.dao.base.constant.ProcessSegment;
import com.feitai.jieya.server.dao.data.model.TongDunData;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

@Service
public class TongDunDataService extends ClassPrefixDynamicSupportService<TongDunData>{

	  public TongDunData findByUserIdAndCardId(Long userId, Long cardId){
		  Example example=Example.builder(TongDunData.class).andWhere(Sqls.custom()
				  .andEqualTo("userId", userId)
				  .andEqualTo("cardId", cardId)
				  .andEqualTo("enable", true)
				  .andEqualTo("segment", ProcessSegment.OPENCARD.getValue())).build();
		  return getMapper().selectOneByExample(example);
	  }
}
