package com.feitai.admin.mop.advert.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.feitai.admin.core.service.DynamitSupportService;
import com.feitai.admin.core.service.Page;
import com.feitai.admin.core.web.BaseListableController;
import com.feitai.admin.mop.advert.dao.entity.AdvertBlock;
import com.feitai.admin.mop.advert.dao.entity.AdvertGroup;
import com.feitai.admin.mop.advert.dao.entity.AdvertItem;
import com.feitai.admin.mop.advert.enums.AdvertBlockStatusEnum;
import com.feitai.admin.mop.advert.request.QueryRequest;
import com.feitai.admin.mop.advert.service.AdvertBlockService;
import com.feitai.admin.mop.advert.service.AdvertGroupService;
import com.feitai.admin.mop.advert.service.AdvertItemService;
import com.feitai.admin.mop.advert.vo.PreviewVo;
import com.feitai.admin.mop.advert.vo.SelectItem;
import com.feitai.admin.mop.base.AdaptDateEditor;
import com.github.pagehelper.PageInfo;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
@RequestMapping("/mop/advert/block")
public class AdvertBlockController extends BaseListableController<AdvertBlock>{

    @InitBinder
    public void initDate(WebDataBinder webDataBinder){
        webDataBinder.registerCustomEditor(Date.class, new AdaptDateEditor());
    }

    @Autowired
    private AdvertBlockService advertBlockService;

    @Autowired
    private AdvertGroupService advertGroupService;
    
    @Autowired
    private AdvertItemService advertItemService;
    

    @RequestMapping()
    public String index() {
        return "/mop/advert/blockIndex";
    }

    @RequiresPermissions("/mop/advert/block:list")
    @RequestMapping("list")
    @ResponseBody
    public Object list(@ModelAttribute QueryRequest queryRequest) {
        PageInfo pageInfo = advertBlockService.list(
                queryRequest.getPageIndex(),
                queryRequest.getLimit(),
                queryRequest.getBlockId(),
                queryRequest.getStartTime(),
                queryRequest.getEndTime(),
                queryRequest.getStatus(),
                queryRequest.getField(),
                queryRequest.getDirection());
        Page<AdvertBlock> blockPage = buildPage(pageInfo, queryRequest.getPageIndex(), queryRequest.getLimit());
        return blockPage;
    }

    @RequiresPermissions("/mop/advert/block:add")
    @RequestMapping("add")
    @ResponseBody
    public Object add(@ModelAttribute AdvertBlock advertBlock, String groupIds) {
        advertBlockService.addBlock(advertBlock, groupIds);
        return BaseListableController.successResult;
    }

    @RequiresPermissions("/mop/advert/block:update")
    @RequestMapping("update")
    @ResponseBody
    public Object update(@ModelAttribute AdvertBlock advertBlock, String groupIds) {
        advertBlockService.updateBlock(advertBlock, groupIds, getOperator());
        advertBlockService.evictCache(advertBlock.getId());
        return BaseListableController.successResult;
    }

    @RequestMapping("cache")
    @ResponseBody
    public Object cache(Long id) {
        advertBlockService.evictCache(id);
        return BaseListableController.successResult;
    }

    @RequiresPermissions("/mop/advert/block:update")
    @RequestMapping("delete")
    @ResponseBody
    public Object delete(Long id) {
        advertBlockService.delete(id);
        return BaseListableController.successResult;
    }

    @RequiresPermissions("/mop/advert/block:update")
    @RequestMapping("enable")
    @ResponseBody
    public Object enable(Long id) {
        advertBlockService.updateStatus(id, AdvertBlockStatusEnum.ENABLE, getOperator());
        advertBlockService.evictCache(id);
        return BaseListableController.successResult;
    }

    @RequiresPermissions("/mop/advert/block:update")
    @RequestMapping("disable")
    @ResponseBody
    public Object disable(Long id) {
        advertBlockService.updateStatus(id, AdvertBlockStatusEnum.DISABLE, getOperator());
        advertBlockService.evictCache(id);
        return BaseListableController.successResult;
    }
    
    @RequiresPermissions("/mop/advert/block:update")
    @RequestMapping("publish")
    @ResponseBody
    public Object publish(Long id) {
    	advertBlockService.publishEditCopy(id, getOperator());
    	advertBlockService.evictCache(id);
        return BaseListableController.successResult;
    }


    @RequestMapping("/groupItems")
    @ResponseBody
    public Object getGroupItems() {
        List<AdvertGroup> groupList = advertGroupService.list();
        List<SelectItem> result = new ArrayList<>(groupList.size());
        groupList.forEach(advertGroup -> {
            result.add(new SelectItem(advertGroup.getTitle(), advertGroup.getId()));
        });
        return result;
    }


    @RequestMapping("preview")
    @ResponseBody
    public Object preview(Long blockId) {
        AdvertBlock block = advertBlockService.get(blockId);
        List<AdvertItem> items = advertItemService.previewListByBlockId(blockId, block.getShowLimit());
        
        return PreviewVo.from(items);
    }

    @Override
    protected DynamitSupportService<AdvertBlock> getService() {
        return null;
    }

    /**
     * 获得操作人
     * @return
     */
    public String getOperator() {
        return SecurityUtils.getSubject().getPrincipal().toString();
    }
}