package com.feitai.admin.messagecenter.controller;

import com.feitai.admin.core.service.Page;
import com.feitai.admin.core.vo.AjaxResult;
import com.feitai.admin.messagecenter.base.MessageBaseController;
import com.feitai.admin.messagecenter.constants.MessageConstants;
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
@RequestMapping(value = "/message/weixinTemplate")
@Slf4j
public class WeixinTemplateController extends MessageBaseController {
    @RequestMapping(value = "index",method = RequestMethod.GET)
    public String index() {
        return "/message/weixinTemplate/index";
    }
    /**
     * 查询列表
     * @param request
     * @return
     */
    @RequiresPermissions("/message/weixinTemplate:list")
    @RequestMapping(value = "list",method = RequestMethod.POST)
    @ResponseBody
    public Page listPage(ServletRequest request) {
        URI uri = getURI( MessageConstants.URL_WEIXIN_TEMPLATE_LIST_PAGE);
        Map<String, Object> searchMap = WebUtils.getParametersStartingWith(request, "");
        WeixinTemplateQueryParam queryParam = new WeixinTemplateQueryParam();
        queryParam.setPageNum(Integer.valueOf(searchMap.get("pageIndex").toString()) + 1);
        queryParam.setPageSize(Integer.valueOf(searchMap.get("limit").toString()));
        if (searchMap.get("weixinCode") != null) {
            queryParam.setWeixinCode(searchMap.get("weixinCode").toString());
        }
        if (searchMap.get("title") != null) {
            queryParam.setTitle(searchMap.get("title").toString());
        }
        PageInfo pageInfo = restTemplate.postForObject(uri, queryParam, PageInfo.class);
        Page page = convert(pageInfo);
        return page;
    }

    /**
     * 新建数据
     * @param weixinTemplate
     * @return
     */
    @RequiresPermissions("/message/weixinTemplate:add")
    @RequestMapping(value = "add",method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult add(WeixinTemplateDto weixinTemplate){
        URI uri = getURI( MessageConstants.URL_WEIXIN_TEMPLATE_ADD);
        Integer recordId = restTemplate.postForObject(uri, weixinTemplate, Integer.class);
        if (recordId.compareTo(0) > 0){
            return successResult;
        }else {
            return failResult;
        }
    }

    /**
     * 更新数据
     * @param weixinTemplate
     * @return
     */
    @RequiresPermissions("/message/weixinTemplate:update")
    @RequestMapping(value = "update",method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult update(WeixinTemplateDto weixinTemplate){
        URI uri = getURI(MessageConstants.URL_WEIXIN_TEMPLATE_UPDATE);
        boolean update = restTemplate.postForObject(uri, weixinTemplate, Boolean.class);
        return update ? successResult : failResult;
    }

    /**
     * 删除数据
     * @param ids
     * @return
     */
    @RequiresPermissions("/message/weixinTemplate:del")
    @RequestMapping(value = "delete",method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult delete(@RequestParam(value = "ids[]") Long[] ids) {
        URI uri = getURI(MessageConstants.URL_WEIXIN_TEMPLATE_DELETE);
        int delete = restTemplate.postForObject(uri,ids,Integer.class);
        return delete > 0 ? successResult : failResult;
    }
}
