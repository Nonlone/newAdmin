package com.feitai.admin.backend.config.entity;

import com.feitai.jieya.server.dao.base.model.BaseId;
import lombok.Data;

import javax.persistence.Table;

/**
 * detail:
 * author:
 * date:2018/10/23
 */
@Data
@Table(name = "t_channel_primary")
public class ChannelPrimary extends BaseId {

    private String primaryChannelName;

    private String channelSort;

    private Integer channelCode;
}
