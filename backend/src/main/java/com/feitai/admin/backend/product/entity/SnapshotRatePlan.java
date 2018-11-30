/**
 * @author
 * @version 1.0
 * @desc 产品费率方案表
 * @since 2018-08-10 13:12:44
 */

package com.feitai.admin.backend.product.entity;

import com.feitai.jieya.server.dao.fund.model.Fund;
import com.feitai.jieya.server.dao.product.model.Product;
import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Table(name = "t_rate_plan_snapshot")
@Data
public class SnapshotRatePlan implements Serializable {
    private static final long serialVersionUID = -1L;
    //alias
    public static final String TABLE_ALIAS = "产品费率方案表";
    public static final String ALIAS_ID = "主键";
    public static final String ALIAS_NAME = "方案名称";
    public static final String ALIAS_FUND_ID = "资金方id";
    public static final String ALIAS_PRODUCT_ID = "产品ID";
    public static final String ALIAS_ENABLE = "0=无效,1=有效";
    public static final String ALIAS_CURRENT_VERSION = "当前版本";
    public static final String ALIAS_CREATED_TIME = "创建时间";
    public static final String ALIAS_UPDATE_TIME = "更新时间";

    @Id
    private Long id;

    private String name;
    private Long fundId;
    private Long productId;

    private String enable;

    private Integer currentVersion;
    private Date createdTime;
    private Date updateTime;
    //columns END

    private List<SnapshotRatePlanTerm> snapshotRatePlanTerms = new ArrayList<SnapshotRatePlanTerm>(0);
    private Fund fund;
    private Product product;


}

