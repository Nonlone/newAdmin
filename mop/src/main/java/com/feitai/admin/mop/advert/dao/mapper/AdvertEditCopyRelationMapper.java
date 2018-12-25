package com.feitai.admin.mop.advert.dao.mapper;

import com.feitai.admin.mop.advert.dao.entity.AdvertEditCopyRelation;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface AdvertEditCopyRelationMapper extends Mapper<AdvertEditCopyRelation> {
	
	@Select({"<script>",
        "SELECT `target_rel_id` FROM `t_advert_edit_copy_relation` WHERE `edit_copy_id` = #{editCopyId} ",
        "</script>"})
    List<Long> queryTargetRelIdsByEditCopyId(
            @Param("editCopyId") long editCopyId);


	@Select({"<script>",
        "SELECT `edit_copy_id` FROM `t_advert_edit_copy_relation` WHERE `target_rel_id` = #{targetRelId} AND `target_rel_type` = #{targetRelType} ",
        "</script>"})
    List<Long> queryEditCopyIdsByTargetRelId(
            @Param("targetRelId") long targetRelId,
            @Param("targetRelType") int targetRelType);

	@Select({"<script>",
        "SELECT `rel_id` FROM `t_advert_edit_copy_relation` WHERE `target_rel_id` = #{targetRelId} AND `target_rel_type` = #{targetRelType} ",
        "</script>"})
    List<Long> queryRelIdsByTargetRelId(
            @Param("targetRelId") long targetRelId,
            @Param("targetRelType") int targetRelType);


	@Select({"<script>",
        "SELECT count(1) FROM `t_advert_edit_copy_relation` WHERE `target_rel_id` = #{targetRelId} AND `target_rel_type` = #{targetRelType}",
        "</script>"})
    Long countByTargetRelId(
            @Param("targetRelId") long targetRelId,
            @Param("targetRelType") int targetRelType);
}