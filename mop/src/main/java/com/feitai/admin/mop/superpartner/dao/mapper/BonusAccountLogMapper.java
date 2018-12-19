package com.feitai.admin.mop.superpartner.dao.mapper;

import com.feitai.admin.mop.superpartner.dao.entity.BonusAccountLog;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

public interface BonusAccountLogMapper extends Mapper<BonusAccountLog> {
	
//	@Select({"<script>",
//	    "SELECT * FROM t_bonus_account_log WHERE `partner_user_id`=#{partnerUserId} AND `id` < #{index}",
//	    " AND `created_time` > #{minCreatedTime} AND `type` in ",
//	    "<foreach collection='types' index='index' item='item' open='(' separator=',' close=')'>#{item}</foreach>",
//	    "<when test='display != null'>",
//	    " AND `display` = #{display}",
//	    "</when>",
//	    " order by id desc limit #{pageSize}",
//	    "</script>"})
//	List<BonusAccountLog> queryPageByIndex(
//            @Param("partnerUserId") long partnerUserId,
//            @Param("types") int[] types,
//            @Param("display") Boolean display,
//            @Param("index") long index,
//            @Param("pageSize") int pageSize,
//            @Param("minCreatedTime") Date minCreatedTime);


	@Update("UPDATE t_bonus_account_log SET `display`=#{display}"
			+ " WHERE `partner_user_id`=#{wherePartnerUserId} and `ref_id`=#{whereRefId}")
	Boolean updateDisplay(
            @Param("display") boolean display,
            @Param("wherePartnerUserId") long wherePartnerUserId,
            @Param("whereRefId") long whereRefId
    );
}