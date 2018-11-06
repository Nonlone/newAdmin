/**
 * @author 
 * @version 1.0
 * @since  2018-07-25 19:25:12
 * @desc Person
 */

package com.feitai.admin.backend.customer.service;

import com.feitai.admin.core.service.ClassPrefixDynamicSupportService;
import com.feitai.admin.core.service.DynamitSupportService;
import com.feitai.jieya.server.dao.data.model.IdCardData;
import com.feitai.jieya.server.dao.data.model.PersonData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;


@Service
@Slf4j
public class PersonService extends ClassPrefixDynamicSupportService<PersonData> {


	public PersonData findByUserId(Long id) {
		Example example = Example.builder(IdCardData.class).andWhere(Sqls.custom().andEqualTo("userId",id)).build();
		return mapper.selectOneByExample(example);
	}

}
