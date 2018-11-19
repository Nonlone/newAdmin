package com.feitai.admin.mop.base.dao.mapper;

import com.feitai.admin.mop.base.dao.entity.OrderReceiverInfo;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

public interface OrderReceiverInfoMapper extends Mapper<OrderReceiverInfo> {

    @Select("select * from t_order_receiver_info where order_id = #{0}")
    OrderReceiverInfo selectByWithdrawOrderId(long withdrawOrderId);

}