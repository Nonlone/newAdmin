/**
 * @author 
 * @version 1.0
 * @since  2018-08-06 15:02:35
 * @desc 产品类型表
 */

package com.feitai.admin.backend.customer.entity;

import com.feitai.jieya.server.dao.base.model.BaseAutoId;
import lombok.Data;

import javax.persistence.Table;


@Table(name = "t_product_type")
@Data
public class ProductType extends BaseAutoId{
	private static final long serialVersionUID = -1L;
	
	//alias
	public static final String TABLE_ALIAS = "产品类型表";
	public static final String ALIAS_ID = "主键";
	public static final String ALIAS_NAME = "名称";
	public static final String ALIAS_CREATED_TIME = "创建时间";
	public static final String ALIAS_UPDATE_TIME = "更新时间";
	public static final String ALIAS_VERSION = "版本";
	
	

	//可以直接使用: @Length(max=50,message="用户名长度不能大于50")显示错误消息
	//columns START
	
	private String name;
	private Integer version;
}

