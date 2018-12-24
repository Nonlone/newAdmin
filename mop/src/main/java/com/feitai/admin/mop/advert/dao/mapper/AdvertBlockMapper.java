package com.feitai.admin.mop.advert.dao.mapper;

import com.feitai.admin.mop.advert.dao.entity.AdvertBlock;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface AdvertBlockMapper extends Mapper<AdvertBlock> {

    @Select({"<script>",
            "SELECT * FROM `t_advert_block` WHERE `id` IN (",
            " SELECT `block_id` FROM t_advert_block_item WHERE `item_id` = #{itemId} ) ",
            "</script>"})
    List<AdvertBlock> queryAdvertBlockCodesByItemId(@Param("itemId") long itemId);
}