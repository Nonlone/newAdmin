package com.feitai.admin.messagecenter.controller;

import com.feitai.admin.core.service.Page;
import com.feitai.admin.core.vo.AjaxResult;
import com.feitai.admin.messagecenter.base.MessageBaseController;
import com.feitai.admin.messagecenter.constants.MessageConstants;
import com.feitai.admin.messagecenter.dto.WeixinReplyDto;
import com.feitai.admin.messagecenter.dto.WeixinReplyQueryDto;
import com.feitai.admin.messagecenter.dto.WeixinTemplateDto;
import com.feitai.admin.messagecenter.dto.WeixinTemplateQueryParam;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.WebUtils;

import javax.servlet.ServletRequest;
import java.net.URI;
import java.util.Map;

/**
 * 微信消息模板管理
 * @author zengzhihui
 */
@Controller
@RequestMapping(value = "/message/weixinReplyTemplate")
@Slf4j
public class WeixinReplyController extends MessageBaseController {
    @RequestMapping(value = "index",method = RequestMethod.GET)
    public String index() {
        return "/message/weixinTemplate/replyIndex";
    }
    /**
     * 查询列表
     * @param request
     * @return
     */
    @RequiresPermissions("/message/weixinReplyTemplate:list")
    @RequestMapping(value = "list",method = RequestMethod.POST)
    @ResponseBody
    public Page listPage(ServletRequest request) {
        URI uri = getURI(MessageConstants.URL_WEIXIN_REPLY_TEMPLATE_LIST_PAGE);
        Map<String, Object> searchMap = WebUtils.getParametersStartingWith(request, "");
        WeixinReplyQueryDto queryParam = new WeixinReplyQueryDto();
        queryParam.setPageNum(Integer.valueOf(searchMap.get("pageIndex").toString()) + 1);
        queryParam.setPageSize(Integer.valueOf(searchMap.get("limit").toString()));
        if (searchMap.get("keywords") != null) {
            queryParam.setKeywords(searchMap.get("keywords").toString());
        }
        if (searchMap.get("keywordsType") != null) {
            queryParam.setKeywordsType(searchMap.get("keywordsType").toString());
        }
        PageInfo pageInfo = restTemplate.postForObject(uri, queryParam, PageInfo.class);
        Page page = convert(pageInfo);
        return page;
    }

    /**
     * 新建数据
     * @param weixinReply
     * @return
     */
    @RequiresPermissions("/message/weixinReplyTemplate:add")
    @RequestMapping(value = "add",method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult add(WeixinReplyDto weixinReply){
        URI uri = getURI( MessageConstants.URL_WEIXIN_REPLY_TEMPLATE_ADD);
        Integer recordId = restTemplate.postForObject(uri, weixinReply, Integer.class);
        if (recordId.compareTo(0) > 0){
            return successResult;
        }else {
            return failResult;
        }
    }

    /**
     * 更新数据
     * @param weixinReply
     * @return
     */
    @RequiresPermissions("/message/weixinReplyTemplate:update")
    @RequestMapping(value = "update",method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult update(WeixinReplyDto weixinReply){
        URI uri = getURI(MessageConstants.URL_WEIXIN_REPLY_TEMPLATE_UPDATE);
        boolean update = restTemplate.postForObject(uri, weixinReply, Boolean.class);
        return update ? successResult : failResult;
    }

    /**
     * 删除数据
     * @param ids
     * @return
     */
    @RequiresPermissions("/message/weixinReplyTemplate:del")
    @RequestMapping(value = "delete",method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult delete(@RequestParam(value = "ids[]") Long[] ids) {
        URI uri = getURI(MessageConstants.URL_WEIXIN_REPLY_TEMPLATE_DELETE);
        int delete = restTemplate.postForObject(uri,ids,Integer.class);
        return delete > 0 ? successResult : failResult;
    }
}
