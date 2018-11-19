package com.feitai.admin.mop.base.dao.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * ReceiverInfo数据实体类
 */
@Data
@ToString(callSuper = true)
@Table(name = "t_receiver_info")
public class ReceiverInfo {
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
	 * 收款人姓名
	 */
	private String name;
	/**
	 * 收款人手机号
	 */
	private String phone;
	/**
	 * 收款人银行卡号
	 */
	private String bankCardNo;
	/**
	 * 收款人身份证
	 */
	private String idCardNo;
	/**
	 * 身份证详情(JSON)
	 */
	private String idCardDetail;
	/**
	 * 创建时间
	 */
	private Date createdTime;
	/**
	 * 更新时间
	 */
	private Date updateTime;

	public ReceiverInfo() {
           
	}
	

	public ReceiverInfo(Long partnerUserId) {
         this.partnerUserId=partnerUserId;
	}
	
}
