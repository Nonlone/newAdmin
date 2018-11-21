package com.feitai.admin.backend.creditdata.service;

import com.feitai.admin.core.service.ClassPrefixDynamicSupportService;
import com.feitai.jieya.server.dao.callback.model.moxie.MoxieData;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

@Service
public class MoxieDataService extends ClassPrefixDynamicSupportService<MoxieData> {

    public MoxieData findByUserIdAndCardId(Long userId,Long cardId){
        return getMapper().selectOneByExample(Example.builder(MoxieData.class).where(Sqls.custom().andEqualTo("userId",userId).andEqualTo("cardId",cardId)).build());
    }

}
