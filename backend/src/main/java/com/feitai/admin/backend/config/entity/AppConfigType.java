/**
 * @author 
 * @version 1.0
 * @since  2018-08-02 11:40:20
 * @desc AppConfigType
 */

package com.feitai.admin.backend.config.entity;

import com.feitai.jieya.server.dao.base.model.BaseModel;
import lombok.Data;

import javax.persistence.Table;
import java.util.Date;


@Table(name = "t_app_config_type")
@Data
public class AppConfigType extends BaseModel  {

	private String typeCode;

	private String name;

	private String remark;

	private Date createdTime;

	private Date updateTime;

}

