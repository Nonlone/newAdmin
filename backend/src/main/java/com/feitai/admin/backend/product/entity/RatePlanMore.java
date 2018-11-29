package com.feitai.admin.backend.product.entity;

import com.feitai.admin.backend.product.mapper.RatePlanTermMoreMapper;
import com.feitai.base.mybatis.Many;
import com.feitai.base.mybatis.One;
import com.feitai.jieya.server.dao.base.model.BaseModel;
import com.feitai.jieya.server.dao.fund.mapper.FundMapper;
import com.feitai.jieya.server.dao.fund.model.Fund;
import com.feitai.jieya.server.dao.product.mapper.ProductMapper;
import com.feitai.jieya.server.dao.product.model.Product;
import lombok.Data;
import lombok.ToString;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.List;


@Table(name = "t_rate_plan")
@Data
public class RatePlanMore extends  BaseModel{ //com.feitai.jieya.server.dao.rateplan.model.RatePlan

    @Id
    @KeySql(useGeneratedKeys = true)
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
    
    private Long productId;

    /**
     * 方案名称
     */
    private String name;

    /**
     * 资金方Id
     */
    private String fundId;

    /**
     * 是否启用
     */
    private String enable;
    
    /**
     * 当前版本
     */
    private Integer currentVersion;
}

