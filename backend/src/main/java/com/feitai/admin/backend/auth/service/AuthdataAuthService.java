/**
 * @author 
 * @version 1.0
 * @since  2018-08-01 19:38:20
 * @desc AuthdataAuth
 */

package com.feitai.admin.backend.auth.service;

import com.feitai.admin.core.service.DynamitSupportService;
import com.feitai.jieya.server.dao.authdata.model.AuthData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

import java.util.List;


@Component
@Slf4j
public class AuthdataAuthService extends DynamitSupportService<AuthData> {


    public List<AuthData> findByCardId(Long id) {
		Example example = Example.builder(AuthData.class).andWhere(Sqls.custom().andEqualTo("cardId",id)).build();
		return this.mapper.selectByExample(example);
    }

}
