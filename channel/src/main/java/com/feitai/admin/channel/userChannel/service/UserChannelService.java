package com.feitai.admin.channel.userChannel.service;

import com.feitai.admin.channel.userChannel.entity.UserChannel;
import com.feitai.admin.core.service.DynamitSupportService;
import com.feitai.admin.core.vo.ShiroUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

import java.util.ArrayList;
import java.util.List;

/**
 * detail:
 * author:longhaoteng
 * date:2018/12/20
 */
@Service
@Slf4j
public class UserChannelService extends DynamitSupportService<UserChannel> {

    public void saveAll(Long userId,List<String> channelCodes){
        for (String channelCode:channelCodes) {
            getMapper().insert(new UserChannel(userId,channelCode));
        }
    }

    public void updateAll(Long userId,List<String> channelCodes){
        Example example = Example.builder(UserChannel.class).andWhere(Sqls.custom().andEqualTo("userId",userId)).build();
        getMapper().deleteByExample(example);
        for (String channelCode:channelCodes) {
            getMapper().insert(new UserChannel(userId,channelCode));
        }

    }

    public List<String> findChannelCodeByUserId(){
        Example example = Example.builder(UserChannel.class).andWhere(Sqls.custom().andEqualTo("userId",getCurrentUserId())).build();
        List<UserChannel> userChannels = getMapper().selectByExample(example);
        List<String> channelCodes = new ArrayList<>();
        for (UserChannel userChannel:userChannels) {
            channelCodes.add(userChannel.getPrimaryChannelCode());
        }
        return channelCodes;
    }

    protected Long getCurrentUserId() {
        ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        return user.getId();
    }

}

