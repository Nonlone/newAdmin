/**
 * @author
 * @version 1.0
 * @desc 方案相关期数配置
 * @since 2018-08-10 13:12:45
 */

package com.feitai.admin.backend.product.entity;

import com.feitai.jieya.server.dao.rateplan.model.RatePlanDetailSnapshot;
import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Table(name = "t_rate_plan_term_snapshot")
@Data
public class SnapshotRatePlanTerm implements Serializable {
    private static final long serialVersionUID = -1L;

    //alias
    public static final String TABLE_ALIAS = "方案相关期数配置";
    public static final String ALIAS_ID = "主键";
    public static final String ALIAS_RATE_PLAN_ID = "方案id";
    public static final String ALIAS_TERM = "期数";
    public static final String ALIAS_WEIGHT = "权重";
    public static final String ALIAS_CREATED_TIME = "创建时间";
    public static final String ALIAS_UPDATE_TIME = "更新时间";
    public static final String ALIAS_VERSION = "版本";


    @Id
    private Long id;
    private Long ratePlanId;
    private Short term;
    private Byte weight;
    private Date createdTime;
    private Date updateTime;

    private Integer version;

    //columns END
    private List<RatePlanDetailSnapshot> snapshotRatePlanDetails = new ArrayList<RatePlanDetailSnapshot>(0);

}

