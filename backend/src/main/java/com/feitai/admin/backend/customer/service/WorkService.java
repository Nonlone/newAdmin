/**
 * @author 
 * @version 1.0
 * @since  2018-07-25 19:24:34
 * @desc Work
 */

package com.feitai.admin.backend.customer.service;

import com.feitai.admin.core.service.ClassPrefixDynamicSupportService;
import com.feitai.admin.core.service.DynamitSupportService;
import com.feitai.jieya.server.dao.data.model.WorkData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

@Service
@Slf4j
public class WorkService extends ClassPrefixDynamicSupportService<WorkData> {

	public WorkData findByUserId(Long userId) {
		Example example = Example.builder(WorkData.class).andWhere(Sqls.custom().andEqualTo("userId",userId).andEqualTo("enable",true)).build();
		return getMapper().selectOneByExample(example);
	}

}
