package com.feitai.admin.backend.creditdata.service;

import com.feitai.admin.core.service.ClassPrefixDynamicSupportService;
import com.feitai.jieya.server.dao.authdata.model.AuthdataTobacco;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

/**
 * detail:烟草贷补充资料
 * author:longhaoteng
 * date:2018/11/29
 */
@Service
@Slf4j
public class AuthdataTobaccoService extends ClassPrefixDynamicSupportService<AuthdataTobacco> {

    public AuthdataTobacco findAuthdataTobaccoByUserId(Long userId){
        Example example = Example.builder(AuthdataTobacco.class).andWhere(Sqls.custom().andEqualTo("userId",userId)).build();
        return getMapper().selectOneByExample(example);
    }
}
