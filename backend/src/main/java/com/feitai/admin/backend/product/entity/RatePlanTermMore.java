/**
 * @author
 * @version 1.0
 * @desc 方案相关期数配置
 * @since 2018-08-10 13:12:45
 */

package com.feitai.admin.backend.product.entity;

import com.feitai.admin.backend.product.mapper.RatePlanTermMoreMapper;
import com.feitai.base.mybatis.Many;
import com.feitai.jieya.server.dao.rateplan.mapper.RatePlanDetailMapper;
import com.feitai.jieya.server.dao.rateplan.model.RatePlanDetail;
import lombok.Data;

import javax.persistence.Table;
import javax.persistence.Transient;

import java.util.List;


@Table(name = "t_rate_plan_term")
@Data
public class RatePlanTermMore extends com.feitai.jieya.server.dao.rateplan.model.RatePlanTerm  {

	@Transient
    @Many(classOfEntity = RatePlanDetail.class, classOfMapper = RatePlanDetailMapper.class , targetField = "ratePlanTermId" )
    private List<RatePlanDetail> ratePlanDetails;

}

