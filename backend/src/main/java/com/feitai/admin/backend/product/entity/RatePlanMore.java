package com.feitai.admin.backend.product.entity;

import com.feitai.admin.backend.product.mapper.RatePlanTermMoreMapper;
import com.feitai.base.mybatis.Many;
import com.feitai.base.mybatis.One;
import com.feitai.jieya.server.dao.fund.mapper.FundMapper;
import com.feitai.jieya.server.dao.fund.model.Fund;
import com.feitai.jieya.server.dao.product.mapper.ProductMapper;
import com.feitai.jieya.server.dao.product.model.Product;
import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.List;


@Table(name = "t_rate_plan")
@Data
public class RatePlanMore extends com.feitai.jieya.server.dao.rateplan.model.RatePlan {

    @Id
    private Long id;

    @Transient
    @Many(classOfEntity = RatePlanTermMore.class, classOfMapper = RatePlanTermMoreMapper.class , targetField = "ratePlanId" )
    private List<RatePlanTermMore> ratePlanTerms;

    @Transient
    @One( classOfMapper = FundMapper.class , sourceField = "fundId" )
    private Fund fund;

    @Transient
    @One( classOfMapper = ProductMapper.class , sourceField = "productId" )
    private Product product;

}

