package com.feitai.admin.mop.superpartner.service;

import com.feitai.admin.core.service.ClassPrefixDynamicSupportService;
import com.feitai.admin.mop.superpartner.dao.entity.PartnerChangeLog;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

/**
 * @Author qiuyunlong
 */
@Service
public class PartnerChangeLogService extends ClassPrefixDynamicSupportService<PartnerChangeLog> {

    private static final String TEMPLATE_CHANGE_USER_TYPE = "操作用户ID:%s,切换为企业帐号";
    private static final String TEMPLATE_CHANGE_USER_RATE = "操作用户ID:%s,奖金结算比例:%s";
    private static final String TEMPLATE_RATE_INFO_START = "实时结算:%s%%; ";
    private static final String TEMPLATE_RATE_INFO_MID = "定时结算:0 < %s万, %s%%; ";
    private static final String TEMPLATE_RATE_INFO_MID2 = "%s < %s万, %s%%; ";
    private static final String TEMPLATE_RATE_INFO_MID3 = " 修改为,";
    private static final String TEMPLATE_RATE_INFO_END = "≥ %s万, %s%%";
    private static final int NUMBER_FORMAT = 10000;


    public List<PartnerChangeLog> listPartnerChangeLog(Long userId) {
        Example example = new Example(PartnerChangeLog.class);
        Example.Criteria criteria = example.createCriteria();
        if (userId != null) {
            criteria.andEqualTo("userId", userId);
        }
        example.orderBy("createdTime").desc();
        List<PartnerChangeLog> changeLogList = getMapper().selectByExample(example);
        return changeLogList;
    }

    public void addChangeUserTypeLog(long userId, String operator) {
        PartnerChangeLog partnerChangeLog = getBaseInsertOne(userId, operator);
        partnerChangeLog.setChangeDesc(String.format(TEMPLATE_CHANGE_USER_TYPE, userId));
        getMapper().insertSelective(partnerChangeLog);
    }

    public void addChangeUserRateLog(long userId, String operator, SettleRateHelper.RateInfo oldRateInfo, SettleRateHelper.RateInfo newRateInfo) {
        PartnerChangeLog partnerChangeLog = getBaseInsertOne(userId, operator);
        StringBuilder stringBuilder = new StringBuilder();
        formatRateInfoLog(stringBuilder, oldRateInfo);
        stringBuilder.append(TEMPLATE_RATE_INFO_MID3);
        formatRateInfoLog(stringBuilder, newRateInfo);
        partnerChangeLog.setChangeDesc(String.format(TEMPLATE_CHANGE_USER_RATE, userId, stringBuilder));
        getMapper().insertSelective(partnerChangeLog);
    }

    private StringBuilder formatRateInfoLog(StringBuilder stringBuilder, SettleRateHelper.RateInfo rateInfo) {
        SettleRateHelper.RateRange rateRange = rateInfo.getPeriodRate().get(0);
        stringBuilder.append(String.format(TEMPLATE_RATE_INFO_START, rateInfo.getImmediatelyRate() * 100));
        stringBuilder.append(String.format(TEMPLATE_RATE_INFO_MID, rateRange.getMax() / NUMBER_FORMAT, rateRange.getRate() * 100));
        for (int i = 1; i < rateInfo.getPeriodRate().size() - 1; i++) {
            rateRange = rateInfo.getPeriodRate().get(i);
            stringBuilder.append(String.format(TEMPLATE_RATE_INFO_MID2, rateRange.getMin() / NUMBER_FORMAT, rateRange.getMax() / NUMBER_FORMAT , rateRange.getRate() * 100));
        }
        if (rateInfo.getPeriodRate().size() > 1) {
            rateRange = rateInfo.getPeriodRate().get(rateInfo.getPeriodRate().size() - 1);
            stringBuilder.append(String.format(TEMPLATE_RATE_INFO_END, rateRange.getMin() / NUMBER_FORMAT, rateRange.getRate() * 100));
        }
        return stringBuilder;
    }

    private PartnerChangeLog getBaseInsertOne(long userId, String operator) {
        PartnerChangeLog partnerChangeLog = new PartnerChangeLog();
        partnerChangeLog.setCreatedTime(new Date());
        partnerChangeLog.setUserId(userId);
        partnerChangeLog.setOperator(operator);
        return partnerChangeLog;
    }

}
