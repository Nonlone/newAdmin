package com.feitai.admin.mop.base.dao.mapper;

import com.feitai.admin.mop.base.dao.entity.InviteeLoanOrder;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.math.BigDecimal;
import java.util.Date;

public interface InviteeLoanOrderMapper extends Mapper<InviteeLoanOrder> {
	
	@Select("SELECT SUM(`loan_amount`) FROM t_invitee_loan_order WHERE `partner_user_id`=#{partnerUserId}"
			+ " AND `status`=#{status} AND `apply_time` >= #{beginTime} AND `apply_time` < #{endTime} limit 1")
	BigDecimal totalLoanAmount(
            @Param("partnerUserId") Long partnerUserId,
            @Param("status") Integer status,
            @Param("beginTime") Date beginTime,
            @Param("endTime") Date endTime);
}