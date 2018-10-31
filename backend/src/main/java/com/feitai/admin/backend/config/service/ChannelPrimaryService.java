package com.feitai.admin.backend.config.service;

import com.feitai.admin.backend.config.entity.ChannelPrimary;
import com.feitai.admin.backend.config.mapper.ChannelPrimaryMapper;
import com.feitai.admin.core.service.DynamitSupportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

import java.util.List;

@Service
@Slf4j
public class ChannelPrimaryService extends DynamitSupportService<ChannelPrimary> {

    private final Integer startCode = 1000;

    @Autowired
    private ChannelPrimaryMapper channelPrimaryMapper;

    public Integer findLastCode() {
        Example example = Example.builder(ChannelPrimary.class).orderByDesc("channelCode").build();
        List<ChannelPrimary> channelPrimaries = this.mapper.selectByExample(example);
        if (channelPrimaries.size() == 0) {
            return startCode;
        } else {
            return (channelPrimaries.get(0).getChannelCode() + 1);
        }
    }

    public ChannelPrimary findByChannelName(String primaryChannelName) {
        Example example = Example.builder(ChannelPrimary.class).andWhere(Sqls.custom().andEqualTo("primaryChannelName", primaryChannelName)).build();
        return channelPrimaryMapper.selectOneByExample(example);
    }
}
