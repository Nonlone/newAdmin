
CREATE TABLE `sys_supply_count_info`  (
  `id` bigint(20) NOT NULL COMMENT '提现订单对应Id',
  `count` int(3) NOT NULL DEFAULT 0 COMMENT '次数',
  `created_time` datetime(0) NOT NULL,
  `update_time` datetime(0) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) 
