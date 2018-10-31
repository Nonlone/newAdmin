/**
 * @author
 * @version 1.0
 * @desc 产品费率方案表
 * @since 2018-08-10 13:12:44
 */

package com.feitai.admin.backend.product.service;

import com.feitai.admin.backend.product.entity.RatePlanMore;
import com.feitai.admin.core.service.DynamitSupportService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;


@Service
public class RatePlanService extends DynamitSupportService<RatePlanMore> {

    public RatePlanMore findByIdAndCurrentVersion(Long id, Integer currentVersion){
        Example example = Example.builder(RatePlanMore.class).andWhere(Sqls.custom().andEqualTo("id",id).andEqualTo("currentVersion",currentVersion)).build();
        return this.mapper.selectOneByExample(example);
    }


}
