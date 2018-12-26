package com.feitai.admin.mop.advert.dao.mapper;

import com.feitai.admin.mop.advert.dao.entity.AdvertBlockItem;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface AdvertBlockItemMapper extends Mapper<AdvertBlockItem> {

    @Select({"<script>",
            "SELECT COUNT(1) FROM `t_advert_block_item` WHERE `block_id` = #{blockId} ",
            "</script>"})
    Long countByBlockId(
            @Param("blockId") long blockId);

    @Select({"<script>",
        "SELECT `block_id` FROM `t_advert_block_item` WHERE `item_id` = #{itemId} ",
        "</script>"})
    List<Long> queryBlockIdsByItemId(
            @Param("itemId") long itemId);

}