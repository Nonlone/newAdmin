package com.feitai.admin.system.model;

// Generated 2014-6-19 17:10:12 by Hibernate Tools 3.4.0.CR1

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Permission generated by hbm2java
 */
@Table(name = "sys_permission")
@Data
public class Permission {

	@Id
	private Integer id;

	private String name;

	private String permission;

	private String memo;

}
