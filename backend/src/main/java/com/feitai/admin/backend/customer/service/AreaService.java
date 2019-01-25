
package com.feitai.admin.backend.customer.service;

import com.feitai.admin.core.service.ClassPrefixDynamicSupportService;
import com.feitai.jieya.server.dao.base.constant.ProcessSegment;
import com.feitai.jieya.server.dao.data.model.LocationData;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

@Service
public class AreaService extends ClassPrefixDynamicSupportService<LocationData> {

    private final static String CARD_ID = "cardId";

    private final static String SEGMENT = "segment";

    public LocationData findByCardIdInAuth(Long id) {
		Example example = Example.builder(LocationData.class).andWhere(Sqls.custom().andEqualTo(CARD_ID,id).andEqualTo(SEGMENT, ProcessSegment.AUTH.getValue())).build();
    	return getMapper().selectOneByExample(example);
    }


    public LocationData findByCardIdInOpenCard(Long id) {
        Example example = Example.builder(LocationData.class).andWhere(Sqls.custom().andEqualTo(CARD_ID,id).andEqualTo(SEGMENT,ProcessSegment.OPENCARD.getValue())).build();
        return getMapper().selectOneByExample(example);
    }

    public LocationData findByCardIdInLoan(Long id) {
        Example example = Example.builder(LocationData.class).andWhere(Sqls.custom().andEqualTo(CARD_ID,id).andEqualTo(SEGMENT,ProcessSegment.LOAN.getValue())).build();
        return getMapper().selectOneByExample(example);
    }

}
