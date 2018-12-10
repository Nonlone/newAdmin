package com.feitai.admin.backend.creditdata.service;

import com.feitai.jieya.server.dao.authdata.mapper.AuthDataMapper;
import com.feitai.jieya.server.dao.authdata.model.AuthData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

@Service
public class AuthdataAuthService {
	@Autowired
	private AuthDataMapper authDataMapper;
      
	public boolean hasUserAuthData(Long userId,String code){
		int count=authDataMapper.selectCountByExample(Example.builder(AuthData.class).
				andWhere(Sqls.custom().andEqualTo("userId", userId).andEqualTo("code", code)).build());
		return count>0;
	}
} 
