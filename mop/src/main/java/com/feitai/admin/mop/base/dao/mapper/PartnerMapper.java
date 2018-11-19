package com.feitai.admin.mop.base.dao.mapper;

import com.feitai.admin.mop.base.dao.entity.Partner;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface PartnerMapper extends Mapper<Partner> {

    @Select({"<script>",
                "select t.*, s.detail as settle_json from t_partner t, t_settle_info s",
                " where t.user_id = s.partner_user_id ",
                "<when test='userId != null'> and t.user_id = #{userId} </when>",
                "<when test='type != null'> and t.type = #{type} </when>",
                "<when test='phone != null'> and t.phone = #{phone} </when>",
                " order by t.created_time desc ",
            "</script>"})
    List<Partner> listWithSettleJson(Map param);

}