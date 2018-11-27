/**
 * @author 
 * @version 1.0
 * @since  2018-08-28 16:04:51
 * @desc 包括授权数据、及第三方调用的数据
 */

package com.feitai.admin.backend.creditdata.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import javax.persistence.Table;
import java.util.Date;


@Data
@Table(name = "t_credit_data")
public class CreditData{

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

