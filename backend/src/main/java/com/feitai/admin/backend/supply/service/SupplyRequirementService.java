package com.feitai.admin.backend.supply.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.feitai.admin.backend.supply.entity.SupplyRequirementMore;
import com.feitai.admin.core.service.ClassPrefixDynamicSupportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

import java.util.List;

/**
 * detail:补件需要Service
 * author:longhaoteng
 * date:2018/11/19
 */

@Service
@Slf4j
public class SupplyRequirementService extends ClassPrefixDynamicSupportService<SupplyRequirementMore> {

    public String findReasonByLoanOrderAndCount(Long id, Byte supplyCount,String supplyCode) {
        Example example =  Example.builder(SupplyRequirementMore.class).andWhere(Sqls.custom().andEqualTo("loanOrderId",id).andEqualTo("supplyCount",supplyCount)).build();
        SupplyRequirementMore supplyRequirementMore = getMapper().selectOneByExample(example);
        if(supplyRequirementMore!=null){
            String requirementInfo = supplyRequirementMore.getRequirementInfo();
            List<JSONObject> list = JSON.parseObject(requirementInfo, List.class);
            for (JSONObject json:list) {
                if(supplyCode.equals((String)json.get("code"))){
                    return (String)json.get("reason");
                }
            }
            return "";
        }
        return "";
    }
}
