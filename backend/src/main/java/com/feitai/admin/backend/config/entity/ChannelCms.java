package com.feitai.admin.backend.config.entity;

import com.feitai.jieya.server.dao.channel.model.Channel;
import lombok.Data;

import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * detail:
 * author:
 * date:2018/11/1
 */
@Data
@Table(
        name = "t_channel"
)
public class ChannelCms extends Channel {
    private Long primaryId;

    @Transient
    private String primaryCode;
}
