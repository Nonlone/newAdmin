
CREATE TABLE `sys_supply_dashu_log`  (
  `id` binary(20) NOT NULL,
  `created_time` datetime(0) NOT NULL COMMENT '发送时间',
  `update_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) 

