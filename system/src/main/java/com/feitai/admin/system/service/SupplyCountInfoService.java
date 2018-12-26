package com.feitai.admin.system.service;

import com.feitai.admin.core.service.DynamitSupportService;
import com.feitai.admin.system.mapper.SupplyDashuLogMapper;
import com.feitai.admin.system.model.SupplyCountInfo;
import com.feitai.admin.system.model.SupplyDashuLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.Date;

/**
 * detail:
 * author:
 * date:2018/11/30
 */
@Service
public class SupplyCountInfoService extends DynamitSupportService<SupplyCountInfo> {

    @Autowired
    private SupplyDashuLogMapper supplyDashuLogMapper;

    public void saveAndLog(SupplyCountInfo supplyCountInfo,Long supplyLogId) {
        try{
            getMapper().updateByPrimaryKey(supplyCountInfo);
            SupplyDashuLog supplyDashuLog = new SupplyDashuLog();
            supplyDashuLog.setId(supplyLogId);
            supplyDashuLog.setCreatedTime(new Date());
            supplyDashuLogMapper.insert(supplyDashuLog);
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
    }

    public boolean checkSendDashuLog(Long supplyLogId){
        SupplyDashuLog supplyDashuLog = supplyDashuLogMapper.selectByPrimaryKey(supplyLogId);
        if (supplyDashuLog==null) {
            return false;
        }else{
            return true;
        }
    }
}
