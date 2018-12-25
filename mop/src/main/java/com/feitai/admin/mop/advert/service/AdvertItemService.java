package com.feitai.admin.mop.advert.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.feitai.admin.mop.advert.dao.entity.AdvertBlock;
import com.feitai.admin.mop.advert.dao.entity.AdvertBlockItem;
import com.feitai.admin.mop.advert.dao.entity.AdvertEditCopy;
import com.feitai.admin.mop.advert.dao.entity.AdvertEditCopy.AdvertEditCopyContent;
import com.feitai.admin.mop.advert.dao.entity.AdvertItem;
import com.feitai.admin.mop.advert.dao.mapper.AdvertBlockItemMapper;
import com.feitai.admin.mop.advert.dao.mapper.AdvertBlockMapper;
import com.feitai.admin.mop.advert.dao.mapper.AdvertItemMapper;
import com.feitai.admin.mop.advert.enums.AdvertBlockTypeEnum;
import com.feitai.admin.mop.advert.enums.AdvertEditCopyRelTypeEnum;
import com.feitai.admin.mop.advert.enums.AdvertItemStatusEnum;
import com.feitai.admin.mop.base.BusinessException;
import com.feitai.admin.mop.base.ListUtils;
import com.feitai.admin.mop.base.RpcResult;
import com.feitai.admin.mop.base.Symbol;
import com.feitai.utils.SnowFlakeIdGenerator;
import com.feitai.utils.http.OkHttpClientUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

/**
 * @Author qiuyunlong
 */
@Service
public class AdvertItemService {

	@Autowired
	private AdvertItemMapper advertItemMapper;

	@Autowired
	private AdvertBlockItemMapper advertBlockItemMapper;
	
	@Autowired
	private AdvertBlockMapper advertBlockMapper;

	@Autowired
	private AdvertBlockService advertBlockService;

	@Autowired
	private AdvertEditCopyService advertEditCopyService;

	@Value("${mop.advert.item.evictCache.url}")
	private String itemEvictCacheUrl;

	public AdvertBlockTypeEnum getItemBlockType(long itemId) {
        return AdvertBlockTypeEnum.fromValue(advertItemMapper.selectBlockType(itemId));
    }

	public List<String> listTitle() {
	    return advertItemMapper.listTitle();
    }

	public PageInfo listWithEditCopyByCode(int pageNo, int pageSize, String title, Integer status, Long blockId, Date startTime,
			Date endTime, String orderFiled, String order) {
		PageHelper.startPage(pageNo, pageSize);
		HashMap param = new HashMap();
        param.put("startTime", startTime);
        param.put("endTime", endTime);
        param.put("status", status);
        param.put("blockId", blockId);
        param.put("title", StringUtils.isNotEmpty(title) ? title : null);
        if (StringUtils.isNotEmpty(orderFiled)) {
            param.put("orderFiled","order by " + com.feitai.utils.StringUtils.humpToLine(orderFiled) + " " +order);
        } else {
			param.put("orderFiled","order by created_time desc");
		}
		List<AdvertItem> itemList = advertItemMapper.listByBlockId(param);
		PageInfo result = new PageInfo(itemList);
		List<Object> list = new ArrayList<Object>();

		if (null != itemList && !itemList.isEmpty()) {
			for (AdvertItem item : itemList) {

				JSONObject obj = (JSONObject) JSONObject.toJSON(item);
				List<AdvertBlock> parents = new ArrayList<AdvertBlock>();
				if (0 < item.getEditCopyId()) {
					AdvertEditCopy editCopy = advertEditCopyService.getEditCopy(item.getEditCopyId());

					AdvertEditCopyContent content = JSONObject.parseObject(editCopy.getContent(),
							AdvertEditCopyContent.class);

					obj = JSONObject.parseObject(content.getEditInfo());

					if (null != content.getRelations() && !content.getRelations().isEmpty()) {
						for (Long _blockId : content.getRelations()) {

							Example example = new Example(AdvertBlock.class);
							example.createCriteria().andEqualTo("id", _blockId);
							AdvertBlock read = advertBlockMapper.selectOneByExample(example);

							parents.add(read);
						}
					}
				} else {
					parents = advertBlockMapper.queryAdvertBlockCodesByItemId(item.getId());
				}
				obj.put("originalStatus", item.getStatus());
				obj.put("parents", parents);
				list.add(obj);
			}
		}
		result.setList(list);
		return result;
	}
	
	
	public List<AdvertItem> previewListByBlockId(long blockId, int limit) {
		Map<String, Object> param = new HashMap<>();
		param.put("blockId", blockId);
		param.put("status", AdvertItemStatusEnum.ENABLE.getValue());
		
		List<AdvertItem> items = advertItemMapper.listByBlockId(param);
		
		List<AdvertItem> editCopyItems = queryEditCopyAdvertItems(blockId);
		
		List<AdvertItem> list = mergeEditCopyAdvertItems(items, editCopyItems);
		
		list = filterAndSub(list, limit);
		
		list = filterAndSort(list);
		
		return list;
	}
	
	
	public List<AdvertItem> filterAndSub(List<AdvertItem> items, int limit) {
		
		if (0 >= limit) {
			return items;
		}
		
		if (ListUtils.isEmpty(items)) {
			return items;
		}
		
		List<AdvertItem> list = new ArrayList<>();
		
		items.forEach(item -> {
			
			Date now = new Date();
			
			//状态过滤
			if (AdvertItemStatusEnum.ENABLE.getValue() != item.getStatus()) {
				return;
			}
			
			//开始时间过滤
			if (null != item.getBeginTime() && 0 > now.compareTo(item.getBeginTime())) {
				return;
			}
			
			//结束时间过滤
			if (null != item.getEndTime() && 0 < now.compareTo(item.getEndTime())) {
				return;
			}
			
			list.add(item);
		});
		
		Collections.sort(items, new Comparator<AdvertItem>() {

			@Override
			public int compare(AdvertItem o1, AdvertItem o2) {
				return o2.getCreatedTime().compareTo(o1.getCreatedTime());
			}
		});
		
		if (items.size() <= limit) {
			return items;
		}
		
		return items.subList(0, limit);
	}
	
	
	private List<AdvertItem> filterAndSort(List<AdvertItem> items) {
		
		if (ListUtils.isEmpty(items)) {
			return items;
		}
		
		List<AdvertItem> list = new ArrayList<>();
		
		items.forEach(item -> {
			
			Date now = new Date();
			
			//状态过滤
			if (AdvertItemStatusEnum.ENABLE.getValue() != item.getStatus()) {
				return;
			}
			
			//开始时间过滤
			if (null != item.getBeginTime() && 0 > now.compareTo(item.getBeginTime())) {
				return;
			}
			
			//结束时间过滤
			if (null != item.getEndTime() && 0 < now.compareTo(item.getEndTime())) {
				return;
			}
			
			list.add(item);
		});
		
		list.sort(new Comparator<AdvertItem>() {

			@Override
			public int compare(AdvertItem o1, AdvertItem o2) {
				return o1.getWeight() - o2.getWeight();
			}
			
		});
		
		return list;
	}
	
	
	private List<AdvertItem> mergeEditCopyAdvertItems(List<AdvertItem> items, List<AdvertItem> editCopyItems) {
		
		if (ListUtils.isEmpty(items)) {
			return editCopyItems;
		}
		
		if (ListUtils.isEmpty(editCopyItems)) {
			return items;
		}
		
		List<AdvertItem> list = new ArrayList<>();
		
		Map<Long, AdvertItem> itemMap = new HashMap<>();
		Map<Long, AdvertItem> editCopyItemMap = new HashMap<>();
		
		items.forEach(item -> {
			itemMap.put(item.getId(), item);
		});
		
		editCopyItems.forEach(editCopyItem -> {
			
			if (!itemMap.containsKey(editCopyItem.getId())) {
				AdvertItem item = get(editCopyItem.getId());
				list.add(mergeEditCopyAdvertItem(item, editCopyItem));
			}
			
			editCopyItemMap.put(editCopyItem.getId(), editCopyItem);
		});
		
		items.forEach(item -> {
			
			if (0 == item.getEditCopyId()) {
				list.add(item);
				return;
			}
			
			AdvertItem editCopyItem = editCopyItemMap.get(item.getId());
			
			if (null != editCopyItem) {
				list.add(mergeEditCopyAdvertItem(item, editCopyItem));
			}
			
		});
		
		return list;
	}
	
	
	private AdvertItem mergeEditCopyAdvertItem(AdvertItem item, AdvertItem editCopyItem) {
		AdvertItem advertItem = new AdvertItem();
		
		advertItem.setStatus(ObjectUtils.defaultIfNull(editCopyItem.getStatus(), item.getStatus()));
		advertItem.setContent(ObjectUtils.defaultIfNull(editCopyItem.getContent(), item.getContent()));
		advertItem.setEvent(ObjectUtils.defaultIfNull(editCopyItem.getEvent(), item.getEvent()));
		advertItem.setShowConfig(ObjectUtils.defaultIfNull(editCopyItem.getShowConfig(), item.getShowConfig()));
		advertItem.setWeight(ObjectUtils.defaultIfNull(editCopyItem.getWeight(), item.getWeight()));
		advertItem.setBeginTime(ObjectUtils.defaultIfNull(editCopyItem.getBeginTime(), item.getBeginTime()));
		advertItem.setEndTime(ObjectUtils.defaultIfNull(editCopyItem.getEndTime(), item.getEndTime()));
		advertItem.setCreatedTime(item.getCreatedTime());
		
		return advertItem;
	}
	
	
	
	/**
	 * 查询编辑副本广告内容列表
	 * @param blockId
	 * @return
	 */
	private List<AdvertItem> queryEditCopyAdvertItems(long blockId) {
		
		List<AdvertItem> list = new ArrayList<>();
		
		List<Long> editCopyIds = advertEditCopyService.queryEditCopyIdsByTargetRelId(blockId, AdvertEditCopyRelTypeEnum.ADVERT_BLOCK);
		
		if (ListUtils.isEmpty(editCopyIds)) {
			return list;
		}
		
		for (Long editCopyId : editCopyIds) {
			
			AdvertItem editCopyItem = advertEditCopyService.getEditInfoObj(editCopyId, AdvertItem.class);
			
			if (null != editCopyItem) {
				list.add(editCopyItem);
			}
		}
		
		return list;
	}
	

	public AdvertItem selectWithEditCopyById(long id) {
		AdvertItem item = advertItemMapper.selectByPrimaryKey(id);

		if (0 < item.getEditCopyId()) {

			AdvertEditCopy editCopy = advertEditCopyService.getEditCopy(item.getEditCopyId());

			AdvertEditCopyContent content = JSONObject.parseObject(editCopy.getContent(), AdvertEditCopyContent.class);

			item = content.getEditInfoObj(AdvertItem.class);

		}

		return item;
	}

	public List<Long> selectBlockIdsWithEditCopyByItemId(long itemId) {
		AdvertItem item = advertItemMapper.selectByPrimaryKey(itemId);

		List<Long> blockIds = new ArrayList<Long>();

		if (0 < item.getEditCopyId()) {

			AdvertEditCopy editCopy = advertEditCopyService.getEditCopy(item.getEditCopyId());

			AdvertEditCopyContent content = JSONObject.parseObject(editCopy.getContent(), AdvertEditCopyContent.class);

			blockIds = content.getRelations();
		} else {
			blockIds = advertItemMapper.listBlockCode(itemId);
		}

		return blockIds;
	}

	public void update(AdvertItem advertItem, String operator) {

		AdvertItem readItem = get(advertItem.getId());

		// 新建状态不需要保存编辑副本
		if (AdvertItemStatusEnum.NEW.getValue() == readItem.getStatus()
				&& 0 == readItem.getEditCopyId()) {
			advertItemMapper.updateByPrimaryKey(advertItem);
			return;
		}

		// 更新编辑副本
		updateEditCopy(readItem, advertItem, operator);
	}

	private void updateEditCopy(AdvertItem readItem, AdvertItem updateItem, String operator) {

		AdvertItem refItem = readItem;

		if (0 < readItem.getEditCopyId()) {
			refItem = advertEditCopyService.getEditInfoObj(readItem.getEditCopyId(), AdvertItem.class);
		}

		updateItem.setEditCopyId(refItem.getEditCopyId());
		updateItem.setVersion(refItem.getVersion());
		updateItem.setStatus(refItem.getStatus());
		updateItem.setCreatedTime(refItem.getCreatedTime());
		updateItem.setUpdateTime(new Date());
		updateItem.setPublishTime(refItem.getPublishTime());

		List<Long> blockIds = getBlockIdsWithEditCopy(readItem.getId(), readItem.getEditCopyId());

		// 更新编辑副本
		updateWithEditCopy(readItem.getId(), readItem.getEditCopyId(), updateItem, blockIds, operator);

	}

	public void updateDetail(AdvertItem advertItem, String code, String operator) {

		AdvertItem readItem = get(advertItem.getId());

		// 新建状态不需要保存编辑副本
		if (AdvertItemStatusEnum.NEW.getValue() != readItem.getStatus()
				|| 0 < readItem.getEditCopyId()) {
			updateDetailEditCopy(readItem, advertItem, code, operator);
			return;
		}

		List<Long> blockIds = new ArrayList<Long>();

		if (StringUtils.isNotBlank(code)) {
			blockIds = ListUtils.toLongList(code, ",");
		} else {
			blockIds = advertBlockItemMapper.queryBlockIdsByItemId(readItem.getId());
		}

		setActiveVersion(advertItem, ObjectUtils.defaultIfNull(advertItem.getBeginTime(), readItem.getBeginTime()));
		updateBlockItem(advertItem, blockIds);

		advertItemMapper.updateByPrimaryKeySelective(advertItem);
	}

	private void updateDetailEditCopy(AdvertItem readItem, AdvertItem updateItem, String code, String operator) {

		AdvertItem refItem = readItem;

		if (0 < readItem.getEditCopyId()) {
			refItem = advertEditCopyService.getEditInfoObj(readItem.getEditCopyId(), AdvertItem.class);
		}

		updateItem.setUpdateTime(new Date());
		updateItem.setPublishTime(refItem.getPublishTime());
		updateItem.setCreatedTime(refItem.getCreatedTime());
		updateItem.setVersion(refItem.getVersion());
		updateItem.setStatus(refItem.getStatus());
		updateItem.setEditCopyId(refItem.getEditCopyId());
		updateItem.setShowConfig(refItem.getShowConfig());
		setActiveVersion(updateItem, ObjectUtils.firstNonNull(
				updateItem.getBeginTime(), refItem.getBeginTime(), readItem.getBeginTime()));
		
		if (null != updateItem.getBeginTime() 
				&& updateItem.getBeginTime().after(new Date())) {
			updateItem.setActiveVersion(0L);
		}

		List<Long> blockIds = new ArrayList<Long>();
		if (StringUtils.isNotBlank(code)) {
			blockIds = ListUtils.toLongList(code, ",");
		} else {
			blockIds = getBlockIdsWithEditCopy(readItem.getId(), readItem.getEditCopyId());
		}

		// 更新编辑副本
		updateWithEditCopy(readItem.getId(), readItem.getEditCopyId(), updateItem, blockIds, operator);
	}

	public void delete(long itemId) {

		if (0 >= itemId) {
			throw new BusinessException("广告内容不存在");
		}

		AdvertItem read = get(itemId);

		if (null == read) {
			throw new BusinessException("广告内容不存在");
		}

		if (1 != read.getStatus()) {
			throw new BusinessException("广告内容状态不支持删除操作");
		}

		if (0 != read.getEditCopyId()) {
			throw new BusinessException("广告内容存在未发布更新");
		}

		Example delExample = new Example(AdvertItem.class);
		delExample.createCriteria().andEqualTo("id", itemId);
		advertItemMapper.deleteByExample(delExample);
		
		Example delBlockItemExample = new Example(AdvertBlockItem.class);
		delBlockItemExample.createCriteria().andEqualTo("itemId", itemId);
		advertBlockItemMapper.deleteByExample(delBlockItemExample);
	}

	public void updateStatus(long itemId, AdvertItemStatusEnum updateStatus, String operator) {

		if (0 >= itemId) {
			throw new BusinessException("广告内容不存在");
		}

		AdvertItem read = get(itemId);
		
		AdvertItem updateEntity = read;

		if (null == read) {
			throw new BusinessException("广告内容不存在");
		}

		if (0 < read.getEditCopyId()) {
			updateEntity = getAdvertItemFromEditCopy(read.getEditCopyId());
		}
		
		if (updateStatus.getValue() == updateEntity.getStatus()) {
			return;
		}
		
		checkUpdateStatus(read, updateEntity, updateStatus);

		updateEntity.setStatus(updateStatus.getValue());
		updateEntity.setUpdateTime(new Date());

		List<Long> blockIds = getBlockIdsWithEditCopy(itemId, read.getEditCopyId());

		// 更新编辑副本
		updateWithEditCopy(itemId, read.getEditCopyId(), updateEntity, blockIds, operator);
	}
	
	private void checkUpdateStatus(AdvertItem readItem, AdvertItem editCopyItem, AdvertItemStatusEnum updateStatus) {
		
		//校验是否为启用状态
		if (AdvertItemStatusEnum.ENABLE != updateStatus) {
			return;
		}
		
		List<AdvertBlock> blocks = advertBlockMapper.queryAdvertBlockCodesByItemId(readItem.getId());
		
		boolean needMultimedia = false;
		
		for (AdvertBlock block : blocks) {
			
			AdvertBlockTypeEnum typeEnum = AdvertBlockTypeEnum.fromValue(block.getBlockType());
			
			if ( ArrayUtils.contains(AdvertBlockTypeEnum.getNeedMultimediaTypes(), typeEnum) ) {
				needMultimedia = true;
				break;
			}
		}
		
		if (!needMultimedia) {
			return;
		}
		
		checkNotEmptyMultimedia(readItem, editCopyItem);
	}
	
	
	private void checkNotEmptyMultimedia(AdvertItem readItem, AdvertItem editCopyItem) {
		
		if (null != editCopyItem 
				&& StringUtils.isNotBlank(editCopyItem.getShowConfig())) {
			
			if (!hasShowConfigItem(editCopyItem)) {
				throw new BusinessException("广告内容中未配置图片");
			}
		}
		
		if (!hasShowConfigItem(readItem)) {
			throw new BusinessException("广告内容中未配置图片");
		}
	}
	
	
	private boolean hasShowConfigItem(AdvertItem item) {
		
		List<AdvertItem.AdvertItemShowConfigItem> list = JSON.parseObject(
				item.getShowConfig(),
				new TypeReference<List<AdvertItem.AdvertItemShowConfigItem>>() {
				});
		
		if (ListUtils.isNotEmpty(list)) {
			return true;
		}
		
		return false;
	}

	/**
	 * 添加广告内容 新建状态不需要保存编辑副本
	 * 
	 * @param advertItem
	 * @param blockIds
	 * @return
	 */
	public boolean addOne(AdvertItem advertItem, String blockIds) {
		advertItem.setId(SnowFlakeIdGenerator.getDefaultNextId());
		advertItem.setEditCopyId(0L);
		advertItem.setCreatedTime(new Date());
		advertItem.setStatus(AdvertItemStatusEnum.NEW.getValue());
		advertItem.setMatchConfig(advertItem.getMatchConfig() == null ? Symbol.EMPTY_JSON : advertItem.getMatchConfig());
		advertItem.setShowConfig(advertItem.getShowConfig() == null ? Symbol.EMPTY_JSON_ARRAY : advertItem.getShowConfig());
		advertItem.setStyle(advertItem.getStyle() == null ? Symbol.EMPTY_JSON : advertItem.getStyle());
		advertItem.setEvent(advertItem.getEvent() == null ? Symbol.EMPTY_JSON : advertItem.getEvent());
		advertItem.setExt(advertItem.getExt() == null ? Symbol.EMPTY_JSON : advertItem.getExt());
		advertItem.setVersion(System.currentTimeMillis());
		setActiveVersion(advertItem, advertItem.getBeginTime());
		advertItemMapper.insertSelective(advertItem);
		if (StringUtils.isNotEmpty(blockIds)) {
			String[] blockIdArr = blockIds.split(",");
			for (String blockId : blockIdArr) {
				advertBlockItemMapper
						.insert(new AdvertBlockItem(null, Long.valueOf(blockId), advertItem.getId(), new Date()));
			}
		}
		return true;
	}

	/**
	 * 设置对媒体默认文件
	 * 
	 * @param itemId
	 * @param url
	 * @param operator
	 */
	public void setDefaultFile(long itemId, String url, String operator) {
		AdvertItem readItem = get(itemId);

		// 新建状态不需要保存编辑副本
		if (AdvertItemStatusEnum.NEW.getValue() != readItem.getStatus()
				|| 0 <= readItem.getEditCopyId()) {
			setDefaultFileEditCopy(readItem, url, operator);
			return;
		}

		setDefaultFileProperties(readItem, url);

		advertItemMapper.updateByPrimaryKey(readItem);
	}
	
	
	private void setDefaultFileProperties(AdvertItem advertItem, String url) {
		
		List<AdvertItem.AdvertItemShowConfigItem> list = JSON.parseObject(
				advertItem.getShowConfig(),
				new TypeReference<List<AdvertItem.AdvertItemShowConfigItem>>() {
				});

		list.forEach(advertItemShowConfigItem -> {
			if (url.equals(advertItemShowConfigItem.getUrl())) {
				advertItemShowConfigItem.setDefaultItem(true);
			} else {
				advertItemShowConfigItem.setDefaultItem(false);
			}
		});
		
		advertItem.setShowConfig(JSON.toJSONString(list));
	}

	
	private void setDefaultFileEditCopy(AdvertItem readItem, String url, String operator) {
		
		AdvertItem refItem = readItem;

		if (0 < readItem.getEditCopyId()) {
			refItem = advertEditCopyService.getEditInfoObj(readItem.getEditCopyId(), AdvertItem.class);
		}
		
		setDefaultFileProperties(refItem, url);
		
		List<Long> blockIds = getBlockIdsWithEditCopy(readItem.getId(), readItem.getEditCopyId());
		// 更新编辑副本
		updateWithEditCopy(readItem.getId(), readItem.getEditCopyId(), refItem, blockIds, operator);
	}
	

	/**
	 * 删除多媒体文件
	 * 
	 * @param itemId
	 * @param url
	 */
	public void deleteFile(long itemId, String url, String operator) {

		AdvertItem readItem = get(itemId);

		// 新建状态不需要保存编辑副本
		if (AdvertItemStatusEnum.NEW.getValue() != readItem.getStatus()
				|| 0 <= readItem.getEditCopyId()) {
			deleteFileEditCopy(readItem, url, operator);
			return;
		}

		setDeleteFileProperties(readItem, url);
		
		advertItemMapper.updateByPrimaryKey(readItem);
	}
	
	
	private void setDeleteFileProperties(AdvertItem advertItem, String url) {
		
		List<AdvertItem.AdvertItemShowConfigItem> list = JSON.parseObject(advertItem.getShowConfig(),
				new TypeReference<List<AdvertItem.AdvertItemShowConfigItem>>() {
				});

		for (int i = 0; i < list.size(); i++) {
			if (url.equals(list.get(i).getUrl())) {
				list.remove(i);
				break;
			}
		}
		advertItem.setShowConfig(JSON.toJSONString(list));
	}

	
	private void deleteFileEditCopy(AdvertItem readItem, String url, String operator) {
		
		AdvertItem refItem = readItem;

		if (0 < readItem.getEditCopyId()) {
			refItem = advertEditCopyService.getEditInfoObj(readItem.getEditCopyId(), AdvertItem.class);
		}
		
		setDeleteFileProperties(refItem, url);
		
		List<Long> blockIds = getBlockIdsWithEditCopy(readItem.getId(), readItem.getEditCopyId());
		// 更新编辑副本
		updateWithEditCopy(readItem.getId(), readItem.getEditCopyId(), refItem, blockIds, operator);
	}
	
	
	private List<Long> getBlockIdsWithEditCopy(long itemId, long editCopyId) {

		if (0 < editCopyId) {
			return advertEditCopyService.getRelationIdsFromEditCopy(editCopyId);
		}

		return advertBlockItemMapper.queryBlockIdsByItemId(itemId);
	}

	private AdvertItem get(long itemId) {
		return advertItemMapper.selectByPrimaryKey(itemId);
	}

	/**
	 * 执行版本更新
	 * 
	 * @param itemId
	 */
	private void updateVersionWithBlock(long itemId) {
		Example example = Example.builder(AdvertBlockItem.class).andWhere(Sqls.custom().andEqualTo("itemId", itemId))
				.build();
		List<AdvertBlockItem> list = advertBlockItemMapper.selectByExample(example);

		// 1.先更新item版本
		AdvertItem update = new AdvertItem();
		update.setId(itemId);
		update.setVersion(System.currentTimeMillis());
		advertItemMapper.updateByPrimaryKeySelective(update);

		// 2.再更新block版本
		list.forEach(advertBlockItem -> advertBlockService.updateVersion(advertBlockItem.getBlockId()));
	}
	
	
	private void setActiveVersion(AdvertItem updateEntity, Date beginTime) {
    	if (null == beginTime || beginTime.before(new Date())) {
    		updateEntity.setActiveVersion(System.currentTimeMillis());
    	}
    	else {
    		updateEntity.setActiveVersion(0L);
    	}
    }

	/**
	 * 更新编辑副本内容
	 * 
	 * @param itemId
	 * @param editCopyId
	 * @param editInfo
	 * @param operator
	 */
	private void updateWithEditCopy(long itemId, long editCopyId, AdvertItem editInfo, List<Long> editBlockIds,
			String operator) {

		if (0 == editCopyId) {
			long copyId = SnowFlakeIdGenerator.getDefaultNextId();
			editInfo.setEditCopyId(copyId);

			// 添加副本
			advertEditCopyService.addEditCopy(copyId, AdvertEditCopyRelTypeEnum.ADVERT_ITEM, itemId, editInfo,
					AdvertEditCopyRelTypeEnum.ADVERT_BLOCK, editBlockIds, operator);

			AdvertItem update = new AdvertItem();
			update.setId(itemId);
			update.setEditCopyId(copyId);
			advertItemMapper.updateByPrimaryKeySelective(update);
		} else {
			advertEditCopyService.updateEditCopy(editCopyId, AdvertEditCopyRelTypeEnum.ADVERT_ITEM, itemId, editInfo,
					AdvertEditCopyRelTypeEnum.ADVERT_BLOCK, editBlockIds, operator);
		}
	}

	/**
	 * 发布广告内容编辑副本为正式数据
	 * 
	 * @param itemId
	 * @param operator
	 */
	public void publishAdvertItemEditCopy(long itemId, String operator) {

		// 1.查询广告内容
		AdvertItem read = get(itemId);

		// 判断广告内容是否存在
		if (null == read) {
			throw new BusinessException("广告内容不存在");
		}

		// 是否关联有判断编辑副本
		if (0 >= read.getEditCopyId()) {
			return;
		}

		AdvertEditCopy editCopy = advertEditCopyService.getEditCopy(read.getEditCopyId());

		// 判断广告内容编辑副本是否存在
		if (null == editCopy) {
			throw new BusinessException("广告内容编辑副本不存在");
		}

		// 2.发布编辑副本内容
		AdvertEditCopyContent content = JSONObject.parseObject(editCopy.getContent(), AdvertEditCopyContent.class);

		AdvertItem item = content.getEditInfoObj(AdvertItem.class);
		item.setEditCopyId(0L);
		item.setPublishTime(new Date());

		// 更新编辑副本内容
		advertItemMapper.updateByPrimaryKeySelective(item);

		updateBlockItem(item, content.getRelations());

		// 3.更新编辑副本发布信息记录
		advertEditCopyService.publishEditCopy(editCopy.getId(), operator);

		// 4.执行版本更新
		updateVersionWithBlock(itemId);

		// 5.执行清除缓存
		evictCache(itemId);
	}

	public void resetAdvertItemEditCopy(long itemId, String operator) {
		// 1.查询广告内容
		AdvertItem read = get(itemId);

		// 判断广告内容是否存在
		if (null == read) {
			throw new BusinessException("广告内容不存在");
		}

		// 是否关联有判断编辑副本
		if (0 >= read.getEditCopyId()) {
			return;
		}

		advertEditCopyService.deleteEditCopy(read.getEditCopyId());

		AdvertItem item = new AdvertItem();
		item.setId(itemId);
		item.setEditCopyId(0L);

		// 更新编辑副本内容
		advertItemMapper.updateByPrimaryKeySelective(item);
	}

	private void updateBlockItem(AdvertItem advertItem, List<Long> blockIds) {
		Example example = new Example(AdvertBlockItem.class);
		example.createCriteria().andEqualTo("itemId", advertItem.getId());
		List<AdvertBlockItem> advertBlockItemList = advertBlockItemMapper.selectByExample(example);
		Map<Long, Boolean> blockMap = new HashMap<>();
		for (Long blockId : blockIds) {
			blockMap.put(blockId, false);
		}
		advertBlockItemList.forEach(advertBlockItem -> {
			if (blockMap.containsKey(advertBlockItem.getBlockId())) {
				blockMap.put(advertBlockItem.getBlockId(), true);
			} else {
				advertBlockItemMapper.deleteByPrimaryKey(advertBlockItem.getId());
			}
		});
		blockMap.forEach((blockId, exists) -> {
			if (!exists) {
				advertBlockItemMapper.insert(new AdvertBlockItem(null, blockId, advertItem.getId(), new Date()));
			}
		});
	}
	
	
	private AdvertItem getAdvertItemFromEditCopy(long editCopyId) {
		AdvertEditCopy editCopy = advertEditCopyService.getEditCopy(editCopyId);

		if (null == editCopy) {
			return null;
		}

		AdvertEditCopyContent content = JSONObject.parseObject(editCopy.getContent(), AdvertEditCopyContent.class);

		if (null == content) {
			return null;
		}

		return content.getEditInfoObj(AdvertItem.class);
	}

	/**
	 * 清除缓存
	 * 
	 * @param itemId
	 * @return
	 */
	public boolean evictCache(long itemId) {
		RpcResult result = null;
		try {
			result = doEvictCache(itemId);
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
		param.put("itemId", id);
		JSONObject data = new JSONObject();
		data.put("data", param);
		String resultStr = OkHttpClientUtils.postReturnBody(itemEvictCacheUrl, data);
		return JSON.parseObject(resultStr, RpcResult.class);
	}
}