package com.feitai.admin.mop.advert.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.feitai.admin.mop.advert.dao.entity.AdvertEditCopy;
import com.feitai.admin.mop.advert.dao.entity.AdvertEditCopy.AdvertEditCopyContent;
import com.feitai.admin.mop.advert.dao.entity.AdvertEditCopyRelation;
import com.feitai.admin.mop.advert.dao.mapper.AdvertEditCopyMapper;
import com.feitai.admin.mop.advert.dao.mapper.AdvertEditCopyRelationMapper;
import com.feitai.admin.mop.advert.enums.AdvertEditCopyRelTypeEnum;
import com.feitai.admin.mop.base.ListUtils;
import com.feitai.utils.SnowFlakeIdGenerator;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;


@Service
public class AdvertEditCopyService {

    @Autowired
    private AdvertEditCopyMapper advertEditCopyMapper;
    
    @Autowired
    private AdvertEditCopyRelationMapper advertEditCopyRelationMapper;
    
    /**
     * 获得编辑副本
     * @param editCopyId
     * @return
     */
    public AdvertEditCopy getEditCopy(long editCopyId) {
    	return advertEditCopyMapper.selectByPrimaryKey(editCopyId);
    }
    
    public List<Long> queryEditCopyIdsByTargetRelId(long targetRelId, AdvertEditCopyRelTypeEnum typeEnum) {
    	return advertEditCopyRelationMapper.queryEditCopyIdsByTargetRelId(
    			targetRelId, typeEnum.getValue());
    }
    
    public List<Long> queryRelIdsByTargetRelId(long targetRelId, AdvertEditCopyRelTypeEnum typeEnum) {
    	return advertEditCopyRelationMapper.queryRelIdsByTargetRelId(
    			targetRelId, typeEnum.getValue());
    }
    
    public long countByTargetRelId(long targetRelId, AdvertEditCopyRelTypeEnum typeEnum) {
    	Long val = advertEditCopyRelationMapper.countByTargetRelId(
    			targetRelId, typeEnum.getValue());
    	return ObjectUtils.defaultIfNull(val, 0L);
    }
    
    
    
    /**
     * 添加编辑副本
     * @param relTypeEnum
     * @param relId
     * @param editInfo
     * @param targetRelTypeEnum
     * @param targetRelIds
     * @param operator
     * @return
     */
    public void addEditCopy(long editCopyId, AdvertEditCopyRelTypeEnum relTypeEnum, long relId, Object editInfo, 
    		AdvertEditCopyRelTypeEnum targetRelTypeEnum, List<Long> targetRelIds, String operator) {
    	
    	Date now = new Date();
    	
    	AdvertEditCopy editCopy = new AdvertEditCopy();
    	
    	AdvertEditCopyContent content = new AdvertEditCopyContent();
    	content.setEditInfoObj(editInfo);
    	content.setRelations(targetRelIds);
    	
    	editCopy.setId(editCopyId);
    	editCopy.setRelType(relTypeEnum.getValue());
    	editCopy.setRelId(relId);
    	editCopy.setContent(JSON.toJSONString(content));
    	editCopy.setOperator(operator);
    	editCopy.setPublisher("");
    	editCopy.setCreatedTime(now);
    	editCopy.setUpdateTime(now);
    	advertEditCopyMapper.insertSelective(editCopy);
    	
    	saveOrUpdateRelation(editCopyId, relTypeEnum, relId, targetRelTypeEnum, targetRelIds);
    }
    
    
    public long addEditCopy(AdvertEditCopyRelTypeEnum relTypeEnum, long relId, Object editInfo, 
    		AdvertEditCopyRelTypeEnum targetRelTypeEnum, List<Long> targetRelIds, String operator) {
    	
    	long copyId = SnowFlakeIdGenerator.getDefaultNextId();
    	
    	addEditCopy(relTypeEnum, relId, editInfo, targetRelTypeEnum, targetRelIds, operator);
    	
    	return copyId;
    }
    
    
    /**
     * 更新编辑副本
     * @param editCopyId
     * @param editInfo
     * @param targetRelIds
     * @param operator
     */
    public void updateEditCopy(long editCopyId, AdvertEditCopyRelTypeEnum relTypeEnum, long relId, 
    		Object editInfo, AdvertEditCopyRelTypeEnum targetRelTypeEnum, List<Long> targetRelIds, String operator) {
    	
    	Date now = new Date();
    	
    	AdvertEditCopy update = new AdvertEditCopy();
    	
    	AdvertEditCopyContent content = new AdvertEditCopyContent();
    	content.setEditInfoObj(editInfo);
    	content.setRelations(targetRelIds);
    	
        update.setId(editCopyId);
        update.setUpdateTime(now);
        update.setContent(JSON.toJSONString(content));
        update.setOperator(operator);
        advertEditCopyMapper.updateByPrimaryKeySelective(update);
        
        saveOrUpdateRelation(editCopyId, relTypeEnum, relId, targetRelTypeEnum, targetRelIds);
    }
    
    
    /**
     * 删除编辑副本
     * @param editCopyId
     */
    public void deleteEditCopy(long editCopyId) {
    	Example delExample = new Example(AdvertEditCopy.class);
        delExample.createCriteria().andEqualTo("id", editCopyId);
        advertEditCopyMapper.deleteByExample(delExample);
        
        deleteRelation(editCopyId);
    }
    
    
    /**
     * 发布编辑副本
     * @param editCopyId
     * @param publisher
     */
    public void publishEditCopy(long editCopyId, String publisher) {
    	Date now = new Date();
    	
    	AdvertEditCopy update = new AdvertEditCopy();
        update.setId(editCopyId);
        update.setUpdateTime(now);
        update.setPublisher(publisher);
        advertEditCopyMapper.updateByPrimaryKeySelective(update);
        
        deleteRelation(editCopyId);
    }
    
    
    public List<Long> getRelationIdsFromEditCopy(long editCopyId) {
		AdvertEditCopy editCopy = getEditCopy(editCopyId);

		if (null == editCopy) {
			return null;
		}

		AdvertEditCopyContent content = JSONObject.parseObject(editCopy.getContent(), AdvertEditCopyContent.class);

		if (null == content) {
			return null;
		}

		return content.getRelations();
	}
    
    
    public <T> T getEditInfoObj(long editCopyId, Class<T> clazz) {
		AdvertEditCopy editCopy = getEditCopy(editCopyId);

		if (null == editCopy) {
			return null;
		}

		AdvertEditCopyContent content = JSONObject.parseObject(editCopy.getContent(), AdvertEditCopyContent.class);

		if (null == content) {
			return null;
		}

		return content.getEditInfoObj(clazz);
	}
    
    
    /**
     * 更新编辑副本关系
     * @param editCopyId
     * @param relTypeEnum
     * @param relId
     * @param targetRelTypeEnum
     * @param targetRelIds
     */
    private void saveOrUpdateRelation(long editCopyId, AdvertEditCopyRelTypeEnum relTypeEnum, 
    		long relId, AdvertEditCopyRelTypeEnum targetRelTypeEnum, List<Long> targetRelIds) {
    	List<Long> readTargetRelIds = advertEditCopyRelationMapper.queryTargetRelIdsByEditCopyId(editCopyId);

		if(ListUtils.isAllEmpty(targetRelIds, readTargetRelIds)) {
			return;
		}

    	if (ListUtils.isEqualListValue(targetRelIds, readTargetRelIds)) {
    		return;
    	}
    	
    	deleteRelation(editCopyId);
    	
    	for (Long targetRelId : targetRelIds) {
    		saveRelation(editCopyId, relTypeEnum, relId, targetRelTypeEnum, targetRelId);
    	}
    }
    
    private void saveRelation(long editCopyId, AdvertEditCopyRelTypeEnum relTypeEnum, long relId,
    		AdvertEditCopyRelTypeEnum targetRelTypeEnum, long targetRelId) {
    	AdvertEditCopyRelation relation = new AdvertEditCopyRelation();
    	relation.setId(SnowFlakeIdGenerator.getDefaultNextId());
    	relation.setEditCopyId(editCopyId);
    	relation.setRelId(relId);
    	relation.setRelType(relTypeEnum.getValue());
    	relation.setTargetRelId(targetRelId);
    	relation.setTargetRelType(targetRelTypeEnum.getValue());
    	relation.setCreatedTime(new Date());
    	
    	advertEditCopyRelationMapper.insertSelective(relation);
    }
    
    private void deleteRelation(long editCopyId) {
    	Example delExample = new Example(AdvertEditCopyRelation.class);
        delExample.createCriteria().andEqualTo("editCopyId", editCopyId);
        advertEditCopyRelationMapper.deleteByExample(delExample);
    }
    
}