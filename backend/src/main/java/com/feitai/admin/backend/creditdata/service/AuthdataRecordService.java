package com.feitai.admin.backend.creditdata.service;

import com.feitai.admin.backend.creditdata.model.AuthdataRecord;
import com.feitai.admin.core.service.ClassPrefixDynamicSupportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

import java.util.List;

/**
 * detail:
 * author:
 * date:2018/12/28
 */
@Service
@Slf4j
public class AuthdataRecordService extends ClassPrefixDynamicSupportService<AuthdataRecord> {

    public AuthdataRecord findByUserAndCode(String code ,Long userId){
        Example example = Example.builder(AuthdataRecord.class).andWhere(Sqls.custom().andEqualTo("code",code).andEqualTo("userId",userId)).build();
        List<AuthdataRecord> authdataRecords = getMapper().selectByExample(example);
        if(authdataRecords.size()>0){
            return authdataRecords.get(0);
        }
        return null;
    }

}
