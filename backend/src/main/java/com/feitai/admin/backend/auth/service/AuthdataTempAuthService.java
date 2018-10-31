/**
 * @author
 * @version 1.0
 * @desc AuthdataTempAuth
 * @since 2018-08-15 17:53:32
 */

package com.feitai.admin.backend.auth.service;

import com.feitai.admin.backend.auth.entity.AuthdataTempAuth;
import com.feitai.admin.core.service.DynamitSupportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

import java.util.List;

@Service
@Slf4j
public class AuthdataTempAuthService extends DynamitSupportService<AuthdataTempAuth> {

    public List<AuthdataTempAuth> findByCardId(Long id) {
        Example example = Example.builder(AuthdataTempAuth.class).andWhere(Sqls.custom().andEqualTo("cardId", id)).build();
        return this.mapper.selectByExample(example);
    }

}
