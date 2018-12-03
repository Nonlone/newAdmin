package com.feitai.admin.mop.base.dao.entity;

import com.feitai.jieya.server.utils.SnowFlakeIdGenerator;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Table;
import java.util.Date;

/**
* Invitee数据实体类
*/
@Data
@ToString(callSuper = true)
@Table(name = "t_invitee")
public class Invitee {
    public Invitee() {
        this.id = SnowFlakeIdGenerator.getDefaultNextId();
    }

    /**
	 *
	 */
	protected static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;
    /**
     * 被邀请用户ID
     */
    private Long userId;
    /**
     * 被邀请用户注册手机号
     */
    private String phone;
    /**
     * 被邀请用户注册时间
     */
    private Date registerTime;
    /**
     * 邀请人用户ID
     */
    private Long partnerUserId;
    /**
     * 首笔完成贷款提现订单
     */
    private Long firstInviteeOrderId;
    /**
     * 创建时间
     */
    private Date createdTime;
}
