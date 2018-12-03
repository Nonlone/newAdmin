/**
 * @author 
 * @version 1.0
 * @since  2018-08-02 11:40:20
 * @desc AppConfigType
 */

package com.feitai.admin.backend.config.entity;

import com.feitai.jieya.server.dao.base.model.BaseModel;
import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;


@Table(name = "t_app_config_type")
@Data
public class AppConfigType extends BaseModel implements Serializable {
	private static final long serialVersionUID = -1L;
	
	//alias
	public static final String TABLE_ALIAS = "AppConfigType";
	public static final String ALIAS_TYPE_CODE = "typeCode";
	public static final String ALIAS_NAME = "name";
	public static final String ALIAS_REMARK = "remark";
	public static final String ALIAS_CREATED_TIME = "创建时间";
	public static final String ALIAS_UPDATE_TIME = "更新时间";
	
	

	//可以直接使用: @Length(max=50,message="用户名长度不能大于50")显示错误消息
	//columns START
	@Id
	private String typeCode;

	private String name;

	private String remark;

	private Date createdTime;

	private Date updateTime;

}

