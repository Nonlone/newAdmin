package com.feitai.admin.system.model;

// Generated 2014-6-19 17:25:08 by Hibernate Tools 3.4.0.CR1

import com.feitai.admin.system.mapper.ResourceMapper;
import com.feitai.base.mybatis.One;
import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * RoleAuth generated by hbm2java
 */
@Table(name = "sys_role_auth")
@Data
public class RoleAuth {

    @Id
    private Long id;

    private Integer roleId;

    private Integer resourceId;

    private String permissionIds;

    @Transient
    @One(classOfMapper = ResourceMapper.class, sourceField = "resourceId")
    private Resource resource;

}
