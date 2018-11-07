/**
 * @author
 * @version 1.0
 * @desc 产品费率方案表
 * @since 2018-08-10 13:12:44
 */

package com.feitai.admin.backend.product.entity;

import com.feitai.admin.backend.product.mapper.RatePlanTermMoreMapper;
import com.feitai.base.mybatis.Many;
import com.feitai.jieya.server.dao.fund.model.Fund;
import com.feitai.jieya.server.dao.product.model.Product;
import lombok.Data;

import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.List;


@Table(name = "t_rate_plan")
@Data
public class RatePlanMore extends com.feitai.jieya.server.dao.rateplan.model.RatePlan {

    @Transient
    @Many(classOfEntity = RatePlanTermMore.class, classOfMapper = RatePlanTermMoreMapper.class , targetField = "")
    private List<RatePlanTermMore> ratePlanTerms;

    @Transient
    private Fund fund;

    @Transient
    private Product product;

}

