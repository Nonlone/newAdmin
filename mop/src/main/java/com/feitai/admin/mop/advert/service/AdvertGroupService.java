package com.feitai.admin.mop.advert.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.feitai.admin.mop.advert.dao.entity.AdvertGroup;
import com.feitai.admin.mop.advert.dao.mapper.AdvertGroupBlockMapper;
import com.feitai.admin.mop.advert.dao.mapper.AdvertGroupMapper;
import com.feitai.admin.mop.advert.enums.AdvertGroupStatusEnum;
import com.feitai.admin.mop.base.BusinessException;
import com.feitai.admin.mop.base.RpcResult;
import com.feitai.utils.SnowFlakeIdGenerator;
import com.feitai.utils.http.OkHttpClientUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class AdvertGroupService {

    @Autowired
    private AdvertGroupMapper advertGroupMapper;

    @Autowired
    private AdvertGroupBlockMapper advertGroupBlockMapper;

    @Value("${mop.advert.group.evictCache.url}")
    private String groupEvictUrl;
    

    public PageInfo<AdvertGroup> list(int pageNo, int pageSize, Date startTime, Date endTime, Integer status) {
        PageHelper.startPage(pageNo, pageSize);
        Example example = new Example(AdvertGroup.class);
        Example.Criteria criteria = example.createCriteria();
        if (status != null) {
            criteria.andEqualTo("status", status);
        }
        if (startTime != null) {
            criteria.andGreaterThanOrEqualTo("beginTime", startTime);
        }
        if (endTime != null) {
            criteria.andLessThanOrEqualTo("endTime", endTime);
        }
        List<AdvertGroup> advertGroupList = advertGroupMapper.selectByExample(example);
        PageInfo pageInfo = new PageInfo(advertGroupList);
        List<Object> list = new ArrayList<Object>();

        if (null != advertGroupList && 0 < advertGroupList.size()) {
            for (AdvertGroup group : advertGroupList) {
                JSONObject obj = (JSONObject) JSONObject.toJSON(group);
                
                Long count = advertGroupBlockMapper.itemCountByGroupId(group.getId());
                obj.put("itemCount", null == count ? 0 : count);
                list.add(obj);
            }
        }
        pageInfo.setList(list);
        return pageInfo;
    }

    public int addGroup(AdvertGroup advertGroup) {
        advertGroup.setId(SnowFlakeIdGenerator.getDefaultNextId());
        advertGroup.setEditCopyId(0L);
        advertGroup.setCreatedTime(new Date());
        advertGroup.setPlayTime(0);
        advertGroup.setStatus(AdvertGroupStatusEnum.NEW.getValue());
        advertGroup.setVersion(System.currentTimeMillis());
        advertGroup.setUpdateTime(new Date());
        return advertGroupMapper.insertSelective(advertGroup);
    }

    
    /**
     * 更新广告模组
     * @param advertGroup
     * @param operator
     */
    public void updateGroup(AdvertGroup advertGroup, String operator) {
    	
    	AdvertGroup readGroup = get(advertGroup.getId());
        advertGroup.setUpdateTime(new Date());
    	advertGroupMapper.updateByPrimaryKeySelective(advertGroup);
    	
    	//非新建状态刷新版本号
        if (AdvertGroupStatusEnum.NEW.getValue() != readGroup.getStatus()) {
        	updateVersion(advertGroup.getId());
        }
    }

    public List<AdvertGroup> list() {
        return advertGroupMapper.selectAll();
    }

    public int updateVersion(long id) {
        AdvertGroup update = new AdvertGroup();
        update.setId(id);
        update.setUpdateTime(new Date());
        update.setVersion(System.currentTimeMillis());
        return advertGroupMapper.updateByPrimaryKeySelective(update);
    }

    public void delete(long groupId) {
        if (0 >= groupId) {
            throw new BusinessException("广告模组不存在");
        }

        Example example = new Example(AdvertGroup.class);
        example.createCriteria().andEqualTo("id", groupId);
        AdvertGroup read = advertGroupMapper.selectOneByExample(example);

        if (null == read) {
            throw new BusinessException("广告模组不存在");
        }

        if (1 != read.getStatus()) {
            throw new BusinessException("广告模组状态不支持删除操作");
        }
        
        if (0 != read.getEditCopyId()) {
        	throw new BusinessException("广告模组存在未发布更新");
        }
        
        if ( 0 < groupCount(groupId)) {
        	throw new BusinessException("广告模组存在关联广告模块不支持删除操作");
        }

        Example delExample = new Example(AdvertGroup.class);
        delExample.createCriteria().andEqualTo("id", groupId);
        advertGroupMapper.deleteByExample(delExample);
    }


    public void updateStatus(long groupId, AdvertGroupStatusEnum updateStatus, String operator) {

        if (0 >= groupId) {
            throw new BusinessException("广告模组不存在");
        }

        AdvertGroup readGroup = get(groupId);

        if (null == readGroup) {
            throw new BusinessException("广告模组不存在");
        }
        
        AdvertGroup updateGroup = readGroup;

        if (updateStatus.getValue() == updateGroup.getStatus()) {
            return;
        }

        AdvertGroup update = new AdvertGroup();
        update.setId(groupId);
        update.setStatus(updateStatus.getValue());
        advertGroupMapper.updateByPrimaryKeySelective(update);

        updateVersion(groupId);
    }
    
    
    
    public AdvertGroup get(long groupId) {
    	return advertGroupMapper.selectByPrimaryKey(groupId);
    }
    
    private long groupCount(long groupId) {
		Long count = advertGroupBlockMapper.countByGroupId(groupId);
		return null == count ? 0L : count;
	}
    
    
    public boolean evictCache(long groupId) {
        RpcResult result = null;
        try {
            result = doEvictCache(groupId);
        } catch (IOException e) {
            throw new BusinessException("缓存更新失败", e);
        }
        if (null != result && result.isSuccess()) {
            return true;
        } else {
            throw new BusinessException("缓存更新失败" + (null != result ? result.getMessage() : ""));
        }
    }

    private RpcResult doEvictCache(long id) throws IOException {
        JSONObject param = new JSONObject();
        param.put("groupId", id);
        JSONObject data = new JSONObject();
        data.put("data", param);
        String resultStr = OkHttpClientUtils.postReturnBody(groupEvictUrl, data);
        return JSON.parseObject(resultStr, RpcResult.class);
    }

}
