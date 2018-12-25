package com.feitai.admin.mop.superpartner.dao.entity;

import com.feitai.base.mybatis.genid.SnowFlakeGenId;
import lombok.Data;
import lombok.ToString;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
* InviteeCredit数据实体类
*/
@Data
@ToString(callSuper = true)
@Table(name = "t_invitee_credit")
public class InviteeCredit {

    /**
     * 主键
     */
    @KeySql(genId = SnowFlakeGenId.class)
    @Id
    private Long id;
    /**
     * 被邀请用户ID
     */
    private Long userId;
    /**
     * 授信卡ID
     */
    private Long cardId;
    /**
     * 授信状态
     */
    private Integer creditStatus;
    /**
     * 授信总额度
     */
    private BigDecimal creditSum;
    /**
     * 授信时间
     */
    private Date creditTime;
    /**
     * 借款期限
     */
    private Integer loanTerm;
    /**
     * 授信过期时间
     */
    private Date expiredTime;
    /**
     * 创建时间
     */
    private Date createdTime;
}
