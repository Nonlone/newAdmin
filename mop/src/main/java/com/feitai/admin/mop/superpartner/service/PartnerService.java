package com.feitai.admin.mop.superpartner.service;


import com.feitai.admin.core.service.ClassPrefixDynamicSupportService;
import com.feitai.admin.core.service.Page;
import com.feitai.admin.mop.base.BusinessException;
import com.feitai.admin.mop.base.dao.entity.Partner;
import com.feitai.admin.mop.base.dao.entity.SettleInfo;
import com.feitai.admin.mop.base.dao.mapper.PartnerMapper;
import com.feitai.admin.mop.base.dao.mapper.SettleInfoMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @Author qiuyunlong
 */
@Service
public class PartnerService extends ClassPrefixDynamicSupportService<Partner>{

    @Autowired
    private SettleInfoMapper settleInfoMapper;
    @Autowired
    private PartnerChangeLogService partnerChangeLogService;


    public List<Partner> listPartner(Long userId, Byte type, String phone) {
        HashMap param = new HashMap();
        Example.Builder builder = Example.builder(Partner.class);
        Sqls custom = Sqls.custom();
        if (userId != null) {
            custom.andEqualTo("userId",userId);
        }
        if (type != null) {
            custom.andEqualTo("type",type);
        }
        if (StringUtils.isNotEmpty(phone)) {
            custom.andEqualTo("phone",phone);
        }
        Example example = builder.andWhere(custom).build();
        List<Partner> partnerList = getMapper().selectByExample(example);
        List<Partner> partners = walkProcessCollection(partnerList);
        return partners;
    }

    public void updatePartnerType(long userId, Integer type, String operator) {
        Partner partner = new Partner();
        partner.setUserId(userId);
        partner.setType(type);
        Example example = Example.builder(Partner.class)
                .andWhere(Sqls.custom()
                        .andEqualTo("userId", userId)
                        .andEqualTo("type", 1))
                .build();
        int result = getMapper().updateByExampleSelective(partner, example);
        if (result > 0) {
            partnerChangeLogService.addChangeUserTypeLog(userId, operator);
        }
    }

    public void updatePartnerRate(long userId, String operator, String sourceJson) {
        SettleRateHelper.RateInfo newRateInfo = SettleRateHelper.formJsonString(sourceJson);
        newRateInfo.validateRange();
        Example example = Example.builder(SettleInfo.class)
                .andWhere(Sqls.custom()
                        .andEqualTo("partnerUserId", userId))
                .build();
        SettleInfo settleInfo = settleInfoMapper.selectOneByExample(example);
        SettleRateHelper.RateInfo oldRateInfo = SettleRateHelper.formJsonString(settleInfo.getDetail());
        if (oldRateInfo.equals(newRateInfo)) {
            throw new BusinessException("详细信息没有变化");
        }
        settleInfo.setDetail(sourceJson);
        settleInfo.setUpdateTime(new Date());
        settleInfoMapper.updateByPrimaryKeySelective(settleInfo);
        partnerChangeLogService.addChangeUserRateLog(userId, operator, oldRateInfo, newRateInfo);
    }

    /**
     * 根据邀请码查询邀请人信息
     * @param inviteCode
     * @return
     */
    public Partner queryByInviteCode(String inviteCode){
        if (StringUtils.isNotBlank(inviteCode)){
            Example example = Example.builder(Partner.class)
                    .andWhere(Sqls.custom().andEqualTo("inviteCode",inviteCode)).build();
            return getMapper().selectOneByExample(example);
        }else {
            return null;
        }
    }

}
