package com.feitai.admin.backend.auth.service;

import com.feitai.admin.backend.auth.entity.AuthDataTempAuth;
import com.feitai.admin.backend.auth.mapper.AuthDataTempAuthMapper;
import com.feitai.admin.backend.properties.MapProperties;
import com.feitai.jieya.server.dao.authdata.mapper.AuthDataMapper;
import com.feitai.jieya.server.dao.authdata.model.AuthData;
import com.feitai.jieya.server.dao.authdata.model.BaseAuthData;
import com.feitai.utils.CollectionUtils;
import com.feitai.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class AuthDataService {

    @Autowired
    private AuthDataMapper authDataMapper;

    @Autowired
    private AuthDataTempAuthMapper authdataTempAuthMapper;

    @Autowired
    private MapProperties mapProperties;


    public List<String> getAuthValueString(Long cardId) {
        List<String> resultList = new ArrayList();
        List<AuthData> authDataList = authDataMapper.selectByExample(Example.builder(AuthData.class).andWhere(Sqls.custom().andEqualTo("cardId", cardId)).build());
        if (!CollectionUtils.isEmpty(authDataList)) {
            for (AuthData auth : authDataList) {
                resultList.add(buildAuthValue(auth));
            }
        } else {
            List<AuthDataTempAuth> authDataTempAuthList = authdataTempAuthMapper.selectByExample(Example.builder(AuthDataTempAuth.class).andWhere(Sqls.custom().andEqualTo("cardId", cardId)).build());
            if (!CollectionUtils.isEmpty(authDataList)) {
                for (AuthDataTempAuth authdataTempAuth : authDataTempAuthList) {
                    resultList.add(buildAuthValue(authdataTempAuth));
                }
            }
        }
       return resultList;
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

}
