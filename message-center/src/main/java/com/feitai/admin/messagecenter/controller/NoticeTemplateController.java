package com.feitai.admin.messagecenter.controller;

import com.feitai.admin.core.service.Page;
import com.feitai.admin.core.web.BaseController;
import com.feitai.admin.messagecenter.config.MessageConfig;
import com.feitai.admin.messagecenter.constants.MessageConstants;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import javax.servlet.ServletRequest;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/message/noticeTemplate")
@Slf4j
public class NoticeTemplateController {
    private RestTemplate restTemplate = new RestTemplate();
    @Autowired
    private MessageConfig messageConfig;

    /**
     * 将PageInfo 转Page
     * @param pageInfo
     * @return
     */
    private static Page convert(PageInfo pageInfo){
        Page page = new Page();
        if (pageInfo != null) {
            List list = pageInfo.getList();
            if (!CollectionUtils.isEmpty(list)){
                page.setContent(list);
            }
            page.setFirst(pageInfo.isIsFirstPage());
            page.setLast(pageInfo.isIsLastPage());
            page.setNext(pageInfo.isHasNextPage());
            page.setNumber(pageInfo.getPageNum());
            page.setPrevious(pageInfo.isHasPreviousPage());
            page.setSize(pageInfo.getSize());
            page.setTotalPages(pageInfo.getPageSize());
            page.setTotalElements(pageInfo.getTotal());
        }
        return page;
    }

    @RequestMapping(value = "index")
    public String index() {
        return "/message/noticeTemplate/index";
    }
    @RequiresPermissions("/message/noticeTemplate:list")
    @RequestMapping(value = "list")
    @ResponseBody
    public Object listPage(ServletRequest request) {
        String url = messageConfig.getMessagecenterServerUrl() + MessageConstants.URL_NOTICE_TEMPLATE_LIST_PAGE;
        URI uri = null;
        try {
            uri = new URI(url);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        Map<String, Object> param = new HashMap<>();
        param.put("pageNum",1);
        param.put("pageSize",10);
        PageInfo pageInfo = restTemplate.postForObject(uri, param, PageInfo.class);
        Page page = convert(pageInfo);
        return page;
    }
}
