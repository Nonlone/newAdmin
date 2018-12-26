package com.feitai.admin.mop.advert.dao.mapper;

import com.feitai.admin.mop.advert.dao.entity.AdvertItem;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface AdvertItemMapper extends Mapper<AdvertItem> {

    //todo 优化sql
    @Select({"<script>",
                "SELECT * FROM t_advert_item i where 1 = 1 ",
                " <when test='blockId != null'> and EXISTS ( SELECT 1 from t_advert_block_item bi where bi.block_id = #{blockId} and bi.item_id = i.id ) </when> ",
                " <when test='status != null'> and status = #{status} </when> ",
                " <when test='title != null'> and title = #{title} </when> ",
                " <when test='startTime != null'> and begin_time <![CDATA[   >=   ]]> #{startTime,jdbcType=TIMESTAMP} </when> ",
                " <when test='endTime != null'> and end_time <![CDATA[   <=   ]]> #{endTime,jdbcType=TIMESTAMP} </when> ",
                " <when test='orderFiled != null'> ${orderFiled} </when> ",
            "</script>"})
    List<AdvertItem> listByBlockId(Map param);

    @Select("select i.block_id from t_advert_block_item i where i.item_id = #{0}")
    List<Long> listBlockCode(long itemId);

    @Select("select title from t_advert_item")
    List<String> listTitle();

    @Select("select b.block_type from t_advert_block b , t_advert_block_item i where i.block_id = b.id and i.item_id = #{0}")
    Integer selectBlockType(long itemId);
}