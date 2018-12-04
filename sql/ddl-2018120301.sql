SET FOREIGN_KEY_CHECKS=0;

CREATE TABLE `admin_web`.`sys_dictionary`  (
  `id` bigint(20) NOT NULL,
  `dictionary_type_id` bigint(20) NULL DEFAULT NULL,
  `name` varchar(255)  NULL DEFAULT NULL,
  `code` varchar(255)  NULL DEFAULT NULL,
  `value` varchar(255)  NULL DEFAULT NULL,
  `order_id` int(11) NULL DEFAULT NULL,
  `extend_1` varchar(255)  NULL DEFAULT NULL,
  `extend_2` varchar(255)  NULL DEFAULT NULL,
  `extend_3` varchar(255)  NULL DEFAULT NULL,
  `memo` varchar(255)  NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FK_comm_dictionary_dictionary_type_id`(`dictionary_type_id`) USING BTREE,
  CONSTRAINT `sys_dictionary_ibfk_1` FOREIGN KEY (`dictionary_type_id`) REFERENCES `admin_web`.`sys_dictionary_type` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB  ROW_FORMAT = Dynamic;

CREATE TABLE `admin_web`.`sys_dictionary_type`  (
  `id` bigint(20) NOT NULL,
  `name` varchar(255)  NULL DEFAULT NULL,
  `code` varchar(255)  NULL DEFAULT NULL,
  `memo` varchar(255)  NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB  ROW_FORMAT = Dynamic;

CREATE TABLE `admin_web`.`sys_permission`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100)  NULL DEFAULT NULL,
  `permission` varchar(200)  NULL DEFAULT NULL,
  `memo` varchar(200)  NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB  ROW_FORMAT = Dynamic;

CREATE TABLE `admin_web`.`sys_resource`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `parent_id` int(11) NULL DEFAULT NULL,
  `name` varchar(100)  NULL DEFAULT NULL,
  `code` varchar(100)  NULL DEFAULT NULL,
  `url` varchar(200)  NULL DEFAULT NULL,
  `ico` varchar(100)  NULL DEFAULT NULL,
  `is_root` int(11) NULL DEFAULT NULL,
  `is_show` int(11) NULL DEFAULT NULL,
  `order_id` int(11) NULL DEFAULT NULL,
  `level` int(11) NULL DEFAULT NULL,
  `parent_ids` varchar(200)  NULL DEFAULT NULL,
  `permission_ids` varchar(200)  NULL DEFAULT NULL,
  `memo` varchar(200)  NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FK_sys_resource_parent_id`(`parent_id`) USING BTREE,
  CONSTRAINT `sys_resource_ibfk_1` FOREIGN KEY (`parent_id`) REFERENCES `admin_web`.`sys_resource` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB  ROW_FORMAT = Dynamic;

CREATE TABLE `admin_web`.`sys_role`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100)  NULL DEFAULT NULL,
  `code` varchar(100)  NULL DEFAULT NULL,
  `memo` varchar(200)  NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB  ROW_FORMAT = Dynamic;

CREATE TABLE `admin_web`.`sys_role_auth`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_id` int(11) NULL DEFAULT NULL,
  `resource_id` int(11) NULL DEFAULT NULL,
  `permission_ids` varchar(100)  NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FK_sys_role_auth_resource_id`(`resource_id`) USING BTREE,
  INDEX `FK_sys_role_auth_role_id`(`role_id`) USING BTREE,
  CONSTRAINT `sys_role_auth_ibfk_1` FOREIGN KEY (`resource_id`) REFERENCES `admin_web`.`sys_resource` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `sys_role_auth_ibfk_2` FOREIGN KEY (`role_id`) REFERENCES `admin_web`.`sys_role` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB  ROW_FORMAT = Dynamic;

CREATE TABLE `admin_web`.`sys_support_staff`  (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `name` varchar(255)  NOT NULL COMMENT '客服名字',
  `email` varchar(255)  NOT NULL COMMENT '客服邮箱',
  `enable` tinyint(1) NOT NULL COMMENT '是否启用',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB  ROW_FORMAT = Dynamic;

CREATE TABLE `admin_web`.`sys_user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `creater_id` int(11) NULL DEFAULT NULL,
  `login_name` varchar(100)  NOT NULL,
  `password` varchar(40)  NOT NULL,
  `salt` varchar(20)  NULL DEFAULT NULL,
  `name` varchar(50)  NULL DEFAULT NULL,
  `email` varchar(200)  NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `status` smallint(6) NULL DEFAULT NULL COMMENT '1 正常 2 停用',
  `last_login_time` datetime(0) NULL DEFAULT NULL,
  `last_login_ip` varchar(15)  NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FK_sys_user_creater_id`(`creater_id`) USING BTREE,
  CONSTRAINT `sys_user_ibfk_1` FOREIGN KEY (`creater_id`) REFERENCES `admin_web`.`sys_user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB  ROW_FORMAT = Dynamic;

CREATE TABLE `admin_web`.`sys_user_role`  (
  `role_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  PRIMARY KEY (`role_id`, `user_id`) USING BTREE,
  INDEX `FK_sys_user_role_user_id`(`user_id`) USING BTREE,
  CONSTRAINT `sys_user_role_ibfk_1` FOREIGN KEY (`role_id`) REFERENCES `admin_web`.`sys_role` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `sys_user_role_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `admin_web`.`sys_user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB  ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS=1;