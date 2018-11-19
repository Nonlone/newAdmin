package com.feitai.admin.mop.base.dao.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
* SettleInfo数据实体类
*/
@Data
@ToString(callSuper = true)
@Table(name = "t_settle_info")
public class SettleInfo {
    /**
	 *
	 */
	protected static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @Id
    private Long id;
    /**
     * 合伙人用户ID
     */
    private Long partnerUserId;
    /**
     * 详情(JSON)
     */
    private String detail;
    /**
     * 创建时间
     */
    private Date createdTime;
    /**
     * 更新时间
     */
    private Date updateTime;
    
    public SettleInfo(){
    	
    }
    public SettleInfo(Long id,Long partnerUserId,String detail){
    	this.id=id;
    	this.partnerUserId=partnerUserId;
    	this.detail=detail;
    	this.createdTime=new Date();
    }
}
