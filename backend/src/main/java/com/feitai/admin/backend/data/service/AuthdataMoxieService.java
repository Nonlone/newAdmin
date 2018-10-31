/**
 * @author 
 * @version 1.0
 * @since  2018-08-29 18:55:22
 * @desc 魔蝎回调任务数据
 */

package com.feitai.admin.backend.data.service;

import com.feitai.admin.backend.data.entity.Moxie;
import com.feitai.admin.backend.data.mapper.AuthdataMoxieMapper;
import com.feitai.admin.core.service.DynamitSupportService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

import java.util.List;


@Component
@Transactional
public class AuthdataMoxieService extends DynamitSupportService<Moxie> {

	@Autowired
	private AuthdataMoxieMapper authdataMoxieMapper;

	public String getMessageByCardId(String cardId){
		Example example =  Example.builder(Moxie.class).andWhere(Sqls.custom().andEqualTo("cardId",cardId)).orderByDesc("createdTime").build();
		List<Moxie> moxieList = authdataMoxieMapper.selectByExample(example);
		if (!moxieList.isEmpty()){
			Moxie obj = moxieList.get(0);
			if (StringUtils.isNotBlank(obj.getReportMessage())){
				return moxieList.get(0).getReportMessage();
			}else {
				return null;
			}
		}
		return null;
	}
}
