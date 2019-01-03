package com.feitai.admin.messagecenter.controller;

import com.feitai.admin.core.service.Page;
import com.feitai.admin.core.vo.AjaxResult;
import com.feitai.admin.messagecenter.base.MessageBaseController;
import com.feitai.admin.messagecenter.config.MessageConfig;
import com.feitai.admin.messagecenter.constants.MessageConstants;
import com.feitai.admin.messagecenter.dto.NoticeTemplateDto;
import com.feitai.admin.messagecenter.dto.NoticeTemplateQueryDto;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.WebUtils;

import javax.servlet.ServletRequest;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

/**
 * 消息模板管理
 * @author zengzhihui
 */
@Controller
@RequestMapping(value = "/message/noticeTemplate")
@Slf4j
public class NoticeTemplateController extends MessageBaseController {
    @RequestMapping(value = "index",method = RequestMethod.GET)
    public String index() {
        return "/message/noticeTemplate/index";
    }
    /**
     * 查询列表
     * @param request
     * @return
     */
    @RequiresPermissions("/message/noticeTemplate:list")
    @RequestMapping(value = "list",method = RequestMethod.POST)
    @ResponseBody
    public Page listPage(ServletRequest request) {
        URI uri = getURI( MessageConstants.URL_NOTICE_TEMPLATE_LIST_PAGE);
        Map<String, Object> searchMap = WebUtils.getParametersStartingWith(request, "");
        NoticeTemplateQueryDto queryParam = new NoticeTemplateQueryDto();
        queryParam.setPageNum(Integer.valueOf(searchMap.get("pageIndex").toString()) + 1);
        queryParam.setPageSize(Integer.valueOf(searchMap.get("limit").toString()));
        if (searchMap.get("code") != null) {
            queryParam.setCode(searchMap.get("code").toString());
        }
        if (searchMap.get("name") != null) {
            queryParam.setName(searchMap.get("name").toString());
        }
        PageInfo pageInfo = restTemplate.postForObject(uri, queryParam, PageInfo.class);
        Page page = convert(pageInfo);
        return page;
    }

    /**
     * 新建数据
     * @param noticeTemplate
     * @return
     */
    @RequiresPermissions("/message/noticeTemplate:add")
    @RequestMapping(value = "add",method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult addNotice(NoticeTemplateDto noticeTemplate){
        URI uri = getURI( MessageConstants.URL_NOTICE_TEMPLATE_ADD);
        Long recordId = restTemplate.postForObject(uri, noticeTemplate, Long.class);
        if (recordId.compareTo(0L) > 0){
            return successResult;
        }else {
            return failResult;
        }
    }

    /**
     * 更新数据
     * @param noticeTemplate
     * @return
     */
    @RequiresPermissions("/message/noticeTemplate:update")
    @RequestMapping(value = "update",method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult updateNotice(NoticeTemplateDto noticeTemplate){
        URI uri = getURI(MessageConstants.URL_NOTICE_TEMPLATE_UPDATE);
        boolean update = restTemplate.postForObject(uri, noticeTemplate, Boolean.class);
        return update ? successResult : failResult;
    }

    /**
     * 删除数据
     * @param ids
     * @return
     */
    @RequiresPermissions("/message/noticeTemplate:del")
    @RequestMapping(value = "delete",method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult del(@RequestParam(value = "ids[]") Long[] ids) {
        URI uri = getURI(MessageConstants.URL_NOTICE_TEMPLATE_DELETE);
        int delete = restTemplate.postForObject(uri,ids,Integer.class);
        return delete > 0 ? successResult : failResult;
    }
}
