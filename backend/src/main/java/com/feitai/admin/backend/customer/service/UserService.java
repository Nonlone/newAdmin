/**
 * @author 
 * @version 1.0
 * @since  2018-07-25 19:22:05
 * @desc User
 */

package com.feitai.admin.backend.customer.service;

import com.feitai.admin.core.service.ClassPrefixDynamicSupportService;
import com.feitai.admin.core.service.DynamitSupportService;
import com.feitai.jieya.server.dao.user.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

@Service
@Slf4j
public class UserService extends ClassPrefixDynamicSupportService<User> {

    public Long findUserIdByPhone(String phone) {
		Example example = Example.builder(User.class).andWhere(Sqls.custom().andEqualTo("phone",phone)).build();
        User user = getMapper().selectOneByExample(example);
        if (user!=null){
            return user.getId();
        }else {
            return null;
        }
    }

}
