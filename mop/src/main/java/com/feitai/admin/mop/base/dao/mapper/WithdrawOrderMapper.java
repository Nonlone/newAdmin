package com.feitai.admin.mop.base.dao.mapper;

import com.feitai.admin.mop.base.dao.entity.WithdrawOrder;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.mapping.FetchType;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface WithdrawOrderMapper extends Mapper<WithdrawOrder> {

    @Select({"<script>",
                " select * from t_withdraw_order where 1 = 1 ",
                " <when test='userId != null'> and user_id = #{userId} </when> ",
                " <when test='status != null'> and status = #{status} </when> ",
                " <when test='id != null'> and id = #{id} </when> ",
                " <when test='startTime != null'> and apply_time <![CDATA[   >=   ]]> #{startTime,jdbcType=TIMESTAMP} </when> ",
                " <when test='endTime != null'> and apply_time <![CDATA[   <=   ]]> #{endTime,jdbcType=TIMESTAMP} </when> ",
                " order by apply_time desc",
            "</script>"})
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "id", property = "orderReceiverInfo", one = @One(
            select = "com.feitai.admin.mop.base.dao.mapper.OrderReceiverInfoMapper.selectByWithdrawOrderId", fetchType = FetchType.EAGER
    ))})
    List<WithdrawOrder> listWithReceiveOrder(Map param);

    @Select({"<script>",
            " select count(*) from t_withdraw_order where 1 = 1 ",
            " <when test='userId != null'> and user_id = #{userId} </when> ",
            " <when test='status != null'> and status = #{status} </when> ",
            " <when test='id != null'> and id = #{id} </when> ",
            " <when test='startTime != null'> and apply_time <![CDATA[   >=   ]]> #{startTime,jdbcType=TIMESTAMP} </when> ",
            " <when test='endTime != null'> and apply_time <![CDATA[   <=   ]]> #{endTime,jdbcType=TIMESTAMP} </when> ",
            " order by apply_time desc",
            "</script>"})
    long countWithdrawOrder(Map param);
}