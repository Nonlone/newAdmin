package com.feitai.admin.mop.superpartner.dao.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
* OrderReceiverInfo数据实体类
*/
@Data
@ToString(callSuper = true)
@Table(name = "t_order_receiver_info")
public class OrderReceiverInfo {
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
     * 订单ID(提现)
     */
    private Long orderId;
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
     * 收款人开户银行
     */
    private String bankName;
    /**
     * 收款人开户支行
     */
    private String bankFullName;
    /**
     * 收款人手机号
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
}
