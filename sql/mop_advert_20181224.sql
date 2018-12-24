-- 修改原菜单
update sys_resource set `code` = replace(`code`, '/admin', ""), `url` = replace(`url`, '/admin', "")  where name in ("超级合伙人","提现订单");
-- 增加广告平台菜单
select @num:=id from sys_resource where name = '运营平台';

INSERT INTO `sys_resource`(`parent_id`, `name`, `code`, `url`, `ico`, `is_root`, `is_show`, `order_id`, `level`, `parent_ids`, `permission_ids`, `memo`)
VALUES (@num, '广告管理', '', '', 'iconfont-qingdan', NULL, NULL, 2, 2, NULL, '', NULL);

select @num:=id from sys_resource where name = '广告管理';

INSERT INTO `sys_resource`(`parent_id`, `name`, `code`, `url`, `ico`, `is_root`, `is_show`, `order_id`, `level`, `parent_ids`, `permission_ids`, `memo`)
VALUES (@num, '广告模组管理', '/mop/advert/group', '/mop/advert/group', 'iconfont-qunzu', NULL, NULL, 1, 3, NULL, '1,2,3,4', NULL);

INSERT INTO `sys_resource`(`parent_id`, `name`, `code`, `url`, `ico`, `is_root`, `is_show`, `order_id`, `level`, `parent_ids`, `permission_ids`, `memo`)
VALUES (@num, '广告模块管理', '/mop/advert/block', '/mop/advert/block', 'iconfont-qunzu', NULL, NULL, 2, 3, NULL, '1,2,3,4', NULL);

INSERT INTO `sys_resource`(`parent_id`, `name`, `code`, `url`, `ico`, `is_root`, `is_show`, `order_id`, `level`, `parent_ids`, `permission_ids`, `memo`)
VALUES (@num, '广告内容管理', '/mop/advert/item', '/mop/advert/item', 'iconfont-chongzhi', NULL, NULL, 3, 3, NULL, '1,2,3,4', NULL);