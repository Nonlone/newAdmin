/**
 * @author 
 * @version 1.0
 * @since  2018-08-07 18:38:41
 * @desc Area
 */

package com.feitai.admin.backend.customer.service;

import com.feitai.admin.core.service.DynamitSupportService;
import com.feitai.jieya.server.dao.data.model.LocationData;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

@Service
public class AreaService extends DynamitSupportService<LocationData> {


    public LocationData findByCardIdAndAuth(Long id) {
		Example example = Example.builder(LocationData.class).andWhere(Sqls.custom().andEqualTo("cardId",id).andEqualTo("segment",0)).build();
    	return this.mapper.selectOneByExample(example);
    }

}
