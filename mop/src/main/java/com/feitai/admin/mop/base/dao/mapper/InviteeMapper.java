package com.feitai.admin.mop.base.dao.mapper;

import com.feitai.admin.mop.base.dao.entity.Invitee;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.Date;
import java.util.List;

public interface InviteeMapper extends Mapper<Invitee> {
	
	@Select("SELECT distinct `partner_user_id` FROM t_invitee WHERE `register_time` >= #{beginTime} AND `register_time` < #{endTime} limit #{startNum},#{pageSize}")
	List<Long> queryPartnerUserIds(
            @Param("beginTime") Date beginTime,
            @Param("endTime") Date endTime,
            @Param("startNum") int startNum,
            @Param("pageSize") int pageSize);


	@Select("SELECT * FROM t_invitee WHERE `partner_user_id`= #{partnerUserId}"
			+ " AND id < #{index} AND `register_time` >= #{beginTime} AND `register_time` < #{endTime}"
			+ " order by id desc limit #{pageSize}")
	List<Invitee> queryList(
            @Param("partnerUserId") long partnerUserId,
            @Param("beginTime") Date beginTime,
            @Param("endTime") Date endTime,
            @Param("index") long index,
            @Param("pageSize") int pageSize);
}