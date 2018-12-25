package com.feitai.admin.mop.advert.controller;

import java.util.Date;

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
import com.feitai.admin.mop.advert.dao.entity.AdvertGroup;
import com.feitai.admin.mop.advert.enums.AdvertGroupStatusEnum;
import com.feitai.admin.mop.advert.request.QueryRequest;
import com.feitai.admin.mop.advert.service.AdvertGroupService;
import com.feitai.admin.mop.base.AdaptDateEditor;
import com.github.pagehelper.PageInfo;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author qiuyunlong
 */
@Slf4j
@Controller
@RequestMapping("/mop/advert/group")
public class AdvertGroupController extends BaseListableController<AdvertGroup> {

    @InitBinder
    public void initDate(WebDataBinder webDataBinder){
        webDataBinder.registerCustomEditor(Date.class, new AdaptDateEditor());
    }

    @Autowired
    private AdvertGroupService advertGroupService;

    @RequestMapping()
    public String index() {
        return "/mop/advert/groupIndex";
    }

    @RequiresPermissions("/mop/advert/group:list")
    @RequestMapping("list")
    @ResponseBody
    public Object list(@ModelAttribute QueryRequest queryRequest) {
        PageInfo pageInfo = advertGroupService.list(
                queryRequest.getPageIndex(),
                queryRequest.getLimit(),
                queryRequest.getStartTime(),
                queryRequest.getEndTime(),
                queryRequest.getStatus());
        Page<AdvertGroup> groupPage = buildPage(pageInfo, queryRequest.getPageIndex(), queryRequest.getLimit());
        return groupPage;
    }

    @RequiresPermissions("/mop/advert/group:add")
    @RequestMapping("add")
    @ResponseBody
    public Object add(@ModelAttribute AdvertGroup advertGroup) {
        advertGroupService.addGroup(advertGroup);
        return BaseListableController.successResult;
    }

    @RequiresPermissions("/mop/advert/group:update")
    @RequestMapping("update")
    @ResponseBody
    public Object update(@ModelAttribute AdvertGroup advertGroup) {
        advertGroupService.updateGroup(advertGroup, getOperator());
        advertGroupService.evictCache(advertGroup.getId());
        return BaseListableController.successResult;
    }

    @RequestMapping("cache")
    @ResponseBody
    public Object cache(Long id) {
        advertGroupService.evictCache(id);
        return BaseListableController.successResult;
    }

    @RequiresPermissions("/mop/advert/group:update")
    @RequestMapping("delete")
    @ResponseBody
    public Object delete(Long id) {
        advertGroupService.delete(id);
        return BaseListableController.successResult;
    }

    @RequiresPermissions("/mop/advert/group:update")
    @RequestMapping("enable")
    @ResponseBody
    public Object enable(Long id) {
        advertGroupService.updateStatus(id, AdvertGroupStatusEnum.ENABLE, getOperator());
        advertGroupService.evictCache(id);
        return BaseListableController.successResult;
    }

    @RequiresPermissions("/mop/advert/group:update")
    @RequestMapping("disable")
    @ResponseBody
    public Object disable(Long id) {
        advertGroupService.updateStatus(id, AdvertGroupStatusEnum.DISABLE, getOperator());
        advertGroupService.evictCache(id);
        return BaseListableController.successResult;
    }

    @Override
    protected DynamitSupportService<AdvertGroup> getService() {
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