/**
 * @author 
 * @version 1.0
 * @since  2018-08-06 18:34:31
 * @desc UserBankCard
 */

package com.feitai.admin.backend.customer.vo;

import lombok.Data;

import java.util.Date;

@Data
public class UserBankCard{

	private Long id;
	private Long userId;
	private String bankName;
	private String bankCardNo;
	private String bankCardType;
	private boolean payStatus;
	private boolean isPay;
	private boolean isRepay;
	private String bankCode;
	private String bankIconUrl;
	private Date createdTime;
	private Date updateTime;
	private String ext1BankCode;
	private String ext2BankCode;
	private String mobile;
	private String userIdNo;
	private String failTime;
	private String limitAmt;
	private String virtualCardNo;
	private String repayStatus;
	private String fund;
	//columns END
}

