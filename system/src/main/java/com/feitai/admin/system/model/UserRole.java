package com.feitai.admin.system.model;

import lombok.Data;

import javax.persistence.Table;

/**
 * detail:
 * author:
 * date:2018/10/18
 */
@Table(name = "sys_user_role")
@Data
public class UserRole {

    private Long userId;

    private Long roleId;

}
