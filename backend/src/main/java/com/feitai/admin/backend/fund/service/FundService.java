/**
 * @author 
 * @version 1.0
 * @since  2018-08-23 16:29:39
 * @desc 记录资金方主表信息
 */

package com.feitai.admin.backend.fund.service;

import com.feitai.admin.core.service.ClassPrefixDynamicSupportService;
import com.feitai.jieya.server.dao.fund.model.Fund;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;


@Component
@Slf4j
public class FundService extends ClassPrefixDynamicSupportService<Fund> {
            
	  public Fund getFund(Long id){
		  Example example=Example.builder(Fund.class).andWhere(Sqls.custom().andEqualTo("id", id)).build();
		 return getMapper().selectOneByExample(example);
	  }
}
