#清空之前一级渠道数据
DELETE FROM t_channel_primary;
#修改一级渠道channel_code类型
ALTER TABLE t_channel_primary MODIFY COLUMN channel_code VARCHAR(50);
#将二级渠道对应的一级渠道数据插入到一级渠道表中
INSERT INTO t_channel_primary SELECT t1.id,t1.main_packgage AS primary_channel_name,t1.main_package_code AS channel_code,t1.channel_sort AS channel_sort,t1.`created_time`,t1.`update_time` FROM t_channel t1 GROUP BY channel_code;