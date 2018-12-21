package com.feitai.admin.channel.userChannel.entity;

import lombok.Data;
import lombok.ToString;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * detail:管理后台用户与渠道的中间表
 * author:longhaoteng
 * date:2018/12/20
 */
@Data
@ToString
@Table(name = "sys_user_channel")
public class UserChannel {

    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;

    private Long userId;

    private String primaryChannelCode;

    public UserChannel(Long id,String primaryChannelCode){
        this.id = id;
        this.primaryChannelCode = primaryChannelCode;
    }

}
