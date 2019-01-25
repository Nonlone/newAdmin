package com.feitai.admin.mop.advert.dao.mapper;

import com.feitai.admin.mop.advert.dao.entity.AdvertGroupBlock;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface AdvertGroupBlockMapper extends Mapper<AdvertGroupBlock> {

	@Select({"<script>",
			"SELECT COUNT(1) FROM `t_advert_block_item` WHERE `block_id` IN (SELECT `block_id` FROM `t_advert_group_block` WHERE `group_id` = #{groupId} ) ",
			"</script>"})
	Long itemCountByGroupId(
            @Param("groupId") long groupId);

	@Select({"<script>",
		"SELECT COUNT(1) FROM `t_advert_group_block` WHERE `group_id` = #{groupId}",
		"</script>"})
	Long countByGroupId(
            @Param("groupId") long groupId);

	@Select({"<script>",
        "SELECT `group_id` FROM `t_advert_group_block` WHERE `block_id` = #{blockId} ",
        "</script>"})
    List<Long> queryGroupIdsByBlockId(
            @Param("blockId") long blockId);
}