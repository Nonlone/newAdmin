package com.feitai.admin.backend.config.service;

import com.feitai.admin.backend.config.entity.ChannelPrimary;
import com.feitai.admin.backend.config.mapper.ChannelPrimaryMapper;
import com.feitai.admin.core.service.ClassPrefixDynamicSupportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

@Service
@Slf4j
public class ChannelPrimaryService extends ClassPrefixDynamicSupportService<ChannelPrimary> {

    private final Integer startCode = 1000;

    @Autowired
    private ChannelPrimaryMapper channelPrimaryMapper;


    public ChannelPrimary findByChannelName(String primaryChannelName) {
        Example example = Example.builder(ChannelPrimary.class).andWhere(Sqls.custom().andEqualTo("primaryChannelName", primaryChannelName)).build();
        return channelPrimaryMapper.selectOneByExample(example);
    }

    public ChannelPrimary findByChannelCode(String channelCode) {
        Example example = Example.builder(ChannelPrimary.class).andWhere(Sqls.custom().andEqualTo("channelCode", channelCode)).build();
        return channelPrimaryMapper.selectOneByExample(example);
    }
}
