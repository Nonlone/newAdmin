package com.feitai.admin.backend.creditdata.service;

import com.alibaba.fastjson.JSONObject;
import com.feitai.admin.backend.creditdata.model.CreditData;
import com.feitai.admin.core.service.ClassPrefixDynamicSupportService;
import com.feitai.jieya.server.dao.base.constant.AuthSource;
import com.feitai.utils.CollectionUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

import java.util.List;

@Service
public class CreditDataService extends ClassPrefixDynamicSupportService<CreditData> {

    public CreditData findByCardIdAndUserIdAndSource(Long userId, Long cardId, AuthSource authSource) {
        return findByCardIdAndUserIdAndSource(userId,cardId,authSource.getValue());
    }

    public CreditData findByCardIdAndUserIdAndSource(Long userId, Long cardId, String authSource) {
        List<CreditData> creditDataList = getMapper().selectByExample(Example.builder(CreditData.class)
                .andWhere(Sqls.custom()
                        .andEqualTo("userId",userId)
                        .andEqualTo("cardId", cardId)
                        .andEqualTo("source", authSource))
                .orderByDesc("createdTime").build());
        if (!CollectionUtils.isEmpty(creditDataList)) {
            CreditData creditData = creditDataList.get(0);
            return creditData;
        }
        return null;
    }

    public String findByCardIdAndSource(long cardId, String source) {
        Example example = Example.builder(CreditData.class).andWhere(Sqls.custom().andEqualTo("cardId", cardId).andEqualTo("source", source)).orderByDesc("createdTime").build();
        List<CreditData> byCardIdAndSource = getMapper().selectByExample(example);
        if (byCardIdAndSource.size() != 0) {
            CreditData creditData = byCardIdAndSource.get(0);
            return creditData.getCreditData();
        } else {
            return null;
        }
    }
}
