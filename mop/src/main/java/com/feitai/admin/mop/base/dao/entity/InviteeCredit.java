package com.feitai.admin.mop.base.dao.entity;

import com.feitai.jieya.server.utils.SnowFlakeIdGenerator;
import lombok.Data;
import lombok.ToString;

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
    public InviteeCredit() {
        this.id = SnowFlakeIdGenerator.getDefaultNextId();
    }
    /**
     * 主键
     */
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
