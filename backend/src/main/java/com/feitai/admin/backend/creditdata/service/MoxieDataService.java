package com.feitai.admin.backend.creditdata.service;

import com.feitai.admin.core.service.ClassPrefixDynamicSupportService;
import com.feitai.jieya.server.dao.callback.model.moxie.MoxieData;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

import java.util.List;

@Service
public class MoxieDataService extends ClassPrefixDynamicSupportService<MoxieData> {

    public MoxieData findByUserId(Long userId){
        List<MoxieData> moxieData = getMapper().selectByExample(Example.builder(MoxieData.class).where(Sqls.custom().andEqualTo("userId", userId)).orderByDesc("createdTime").build());
        if(moxieData.size()>0){
            return moxieData.get(0);
        }
        return null;
    }

}
