package com.feitai.admin.backend.customer.service;

import com.feitai.admin.backend.properties.MapProperties;
import com.feitai.jieya.server.dao.authdata.mapper.AuthDataMapper;
import com.feitai.jieya.server.dao.authdata.model.AuthData;
import com.feitai.jieya.server.dao.authdata.model.BaseAuthData;
import com.feitai.jieya.server.dao.base.constant.AuthStatus;
import com.feitai.utils.CollectionUtils;
import com.feitai.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;
import tk.mybatis.mapper.weekend.WeekendSqls;

import java.util.*;

@Service
public class AuthDataService {

    @Autowired
    private AuthDataMapper authDataMapper;

    @Autowired
    private MapProperties mapProperties;

    /**
     * 获取已认证数据
     *
     * @param cardId
     * @return
     */
    public List<BaseAuthData> getAuthValueString(Long cardId) {
        List<BaseAuthData> resultList = new ArrayList();
        Set<String> checkSet = new HashSet<>();
        List<AuthData> authDataList = authDataMapper.selectByExample(Example.builder(AuthData.class)
                .andWhere(WeekendSqls.<AuthData>custom()
                        .andEqualTo("cardId", cardId)
                        .andEqualTo("status", AuthStatus.AUTHORIZED.getValue())).build());
        if (!CollectionUtils.isEmpty(authDataList)) {
            for (AuthData auth : authDataList) {
                if(auth.getCode()==null||auth.getSource()==null){
                    continue;
                }
                if (!checkSet.contains(auth.getCode().getValue() + "-" + auth.getSource().getValue())) {
                    checkSet.add(auth.getCode().getValue() + "-" + auth.getSource().getValue());
                    resultList.add(auth);
                }
            }
        }
        return resultList;
    }

    /**
     * 转换
     *
     * @param authDataList
     * @return
     */
    public List<String> convertBaseAuthDataListToString(List<BaseAuthData> authDataList) {
        Set<String> resultSet = new HashSet<>();
        if (!CollectionUtils.isEmpty(authDataList)) {
            for (BaseAuthData baseAuthData : authDataList) {
                resultSet.add(buildAuthValue(baseAuthData));
            }
        }
        return new ArrayList<>(resultSet);
    }

    private String buildAuthValue(BaseAuthData authData) {
        StringBuffer sbValue = new StringBuffer();
        if (!Objects.isNull(authData.getCode())) {
            sbValue.append(authData.getCode().getValue());
        }
        if (!Objects.isNull(authData.getSource())) {
            sbValue.append("-").append(authData.getSource().getValue());
        }
        String value = mapProperties.getAuthValue(sbValue.toString());
        if (StringUtils.isNotBlank(value)) {
            return value;
        }
        return sbValue.toString();
    }

    public List<AuthData> findByCardIdAndCode(Long cardId, String authTobaccoCode) {
        Example example = Example.builder(AuthData.class).andWhere(Sqls.custom().andEqualTo("code",authTobaccoCode).andEqualTo("cardId",cardId)).build();
        List<AuthData> authData = authDataMapper.selectByExample(example);
        return authData;
    }
}
