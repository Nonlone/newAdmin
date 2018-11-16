/**
 * @author 
 * @version 1.0
 * @since  2018-08-15 10:20:44
 * @desc Channel
 */

package com.feitai.admin.backend.config.service;

import com.feitai.admin.backend.config.entity.ChannelCms;
import com.feitai.admin.backend.config.mapper.ChannelCmsMapper;
import com.feitai.admin.core.service.ClassPrefixDynamicSupportService;
import com.feitai.admin.core.service.DynamitSupportService;
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
public class ChannelService extends ClassPrefixDynamicSupportService<ChannelCms> {

    @Autowired
    private ChannelCmsMapper channelCmsMapper;

    public ChannelCms findBySubPackageAndMainPackage(String subPackage, String mainPackage) {
        Example example = Example.builder(ChannelCms.class).andWhere(Sqls.custom().andEqualTo("subPackage",subPackage).andEqualTo("mainPackgage",mainPackage)).build();
        return channelCmsMapper.selectOneByExample(example);
    }

    public List<ChannelCms> findByPrimaryId(Long id) {
        Example example = Example.builder(ChannelCms.class).andWhere(Sqls.custom().andEqualTo("primaryId",id)).build();
        return channelCmsMapper.selectByExample(example);
    }

    public boolean checkChannel(String channelId) {
        Example example = Example.builder(ChannelCms.class).andWhere(Sqls.custom().andEqualTo("channelId",channelId)).build();
        List<ChannelCms> channelCms = getMapper().selectByExample(example);
        if(channelCms.size()>0){
            return false;
        }
        return true;
    }
}
