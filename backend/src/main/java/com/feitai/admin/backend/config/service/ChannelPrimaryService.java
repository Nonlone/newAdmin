package com.feitai.admin.backend.config.service;

import com.feitai.admin.backend.config.entity.ChannelPrimary;
import com.feitai.admin.backend.config.mapper.ChannelPrimaryMapper;
import com.feitai.admin.core.service.ClassPrefixDynamicSupportService;
import com.feitai.jieya.server.dao.channel.mapper.ChannelMapper;
import com.feitai.jieya.server.dao.channel.model.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

import java.util.List;

@Service
@Slf4j
public class ChannelPrimaryService extends ClassPrefixDynamicSupportService<ChannelPrimary> {

    private final Integer startCode = 1000;

    @Autowired
    private ChannelPrimaryMapper channelPrimaryMapper;

    @Autowired
    private ChannelMapper channelMapper;


    public ChannelPrimary findByChannelName(String primaryChannelName) {
        Example example = Example.builder(ChannelPrimary.class).andWhere(Sqls.custom().andEqualTo("primaryChannelName", primaryChannelName)).build();
        return channelPrimaryMapper.selectOneByExample(example);
    }

    public ChannelPrimary findByChannelCode(String channelCode) {
        Example example = Example.builder(ChannelPrimary.class).andWhere(Sqls.custom().andEqualTo("channelCode", channelCode)).build();
        return channelPrimaryMapper.selectOneByExample(example);
    }


    public void saveChannelPrimaryAndChannel(ChannelPrimary channelPrimary) {
        Example example = Example.builder(Channel.class).andWhere(Sqls.custom().andEqualTo("mainPackageCode",channelPrimary.getChannelCode())).build();
        List<Channel> channels = channelMapper.selectByExample(example);
        for (Channel channel:channels){
            channel.setMainPackgage(channelPrimary.getPrimaryChannelName());
            channel.setChannelSort(channelPrimary.getChannelSort());
            channelMapper.updateByPrimaryKey(channel);
        }
        this.save(channelPrimary);
    }
}
