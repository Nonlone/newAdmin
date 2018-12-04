package com.feitai.admin.mop.base.dao.entity;


import com.feitai.admin.mop.base.dao.mapper.SettleInfoMapper;
import com.feitai.admin.mop.base.enums.PartnerStatus;
import com.feitai.admin.mop.base.enums.PartnerType;
import com.feitai.base.mybatis.One;
import com.feitai.base.mybatis.genid.SnowFlakeGenId;
import lombok.Data;
import lombok.ToString;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

/**
* Partner数据实体类
*/
@Data
@ToString(callSuper = true)
@Table(name = "t_partner")
public class Partner {

    /**
     * 主键
     */
    @KeySql(genId = SnowFlakeGenId.class)
    @Id
    private Long id;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 注册手机号
     */
    private Long phone;
    /**
     * 注册时间
     */
    private Date registerTime;
    /**
     * 邀请码
     */
    private String inviteCode;
    /**
     * 账号类型 1:个人,2:企业
     */
    private Integer type;
    /**
     * 账号状态 1:激活,2:冻结
     */
    private Integer status;
    /**
     * 创建时间
     */
    private Date createdTime;
    /**
     * 更新时间
     */
    private Date updateTime;
    
    @Transient
    @One(classOfMapper = SettleInfoMapper.class,sourceField = "userId",targetField = "partnerUserId")
    private SettleInfo settleInfo;

    public Partner(){
    	
    }
    public Partner(Long userId){
    	this.userId=userId;
    }

    public Partner(Long userId,Long phone,String inviteCode){
    	this(userId,phone,inviteCode, PartnerType.PERSONAL.getType(), PartnerStatus.ACTIVE.getStatus(),new Date());
    }

    public Partner(Long userId,Long phone,String inviteCode,Integer type,Integer status,Date createTime){
    	this.userId=userId;
    	this.phone=phone;
    	this.inviteCode=inviteCode;
    	this.type=type;
    	this.status=status;
    	this.createdTime=createTime;
    }

    
}
