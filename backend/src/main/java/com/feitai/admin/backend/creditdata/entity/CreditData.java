/**
 * @author 
 * @version 1.0
 * @since  2018-08-28 16:04:51
 * @desc 包括授权数据、及第三方调用的数据
 */

package com.feitai.admin.backend.creditdata.entity;

import lombok.Data;

import javax.persistence.Table;
import java.util.Date;


@Data
@Table(name = "t_credit_data")
public class CreditData{
	private static final long serialVersionUID = -1L;
	
	//alias
	public static final String TABLE_ALIAS = "包括授权数据、及第三方调用的数据";
	public static final String ALIAS_ID = "id";
	public static final String ALIAS_PRODUCT_CODE = "解压默认是1";
	public static final String ALIAS_USER_ID = "userId";
	public static final String ALIAS_CARD_ID = "cardId";
	public static final String ALIAS_SOURCE = "source";
	public static final String ALIAS_CODE = "code";
	public static final String ALIAS_AUTH_DATA_ID = "authDataId";
	public static final String ALIAS_CREDIT_DATA = "creditData";
	public static final String ALIAS_CALL_RESULT_FLAG = "1成功  0失败";
	public static final String ALIAS_CREATED_TIME = "创建时间";
	public static final String ALIAS_UPDATE_TIME = "更新时间";
	


	private Long id;

	private Byte productCode;
	
	private Long userId;
	
	private Long cardId;
	private String source;
	private String code;
	
	private Long authDataId;
	private String creditData;
	private Byte callResultFlag;
	
	private Date createdTime;
	
	private Date updateTime;


}

