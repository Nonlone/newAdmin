package com.feitai.admin.mop.base.dao.mapper;


import com.feitai.admin.mop.base.dao.entity.BonusAccount;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

import java.math.BigDecimal;
import java.util.Date;

public interface BonusAccountMapper extends Mapper<BonusAccount> {
	
	@Update("UPDATE t_bonus_account SET `balance`=#{balance},`frozen_amount`= #{frozenAmount},`aggregate_amount`= #{aggregateAmount}"
			+ " ,`withdraw_amount`= #{withdrawAmount},`invite_count`= #{inviteCount},`updateTime`=#{aggregateAmount},`update_version`=`update_version`+1"
			+ " WHERE `id`=#{id} and `partner_user_id`=#{partnerUserId} and `update_version`=#{updateVersion} ")
	Boolean updateAccount(
            @Param("balance") BigDecimal balance,
            @Param("frozenAmount") BigDecimal frozenAmount,
            @Param("aggregateAmount") BigDecimal aggregateAmount,
            @Param("withdrawAmount") BigDecimal withdrawAmount,
            @Param("inviteCount") Integer inviteCount,
            @Param("updateTime") Date updateTime,
            @Param("partnerUserId") Long whereId,
            @Param("partnerUserId") Long wherePartnerUserId,
            @Param("updateVersion") Integer whereUpdateVersion
    );
}