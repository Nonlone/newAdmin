package com.feitai.admin.messagecenter.base;

import com.feitai.admin.core.service.Page;
import com.feitai.admin.core.web.BaseController;
import com.feitai.admin.messagecenter.config.MessageConfig;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@Controller
public class MessageBaseController extends BaseController {
    @Autowired
    private MessageConfig messageConfig;

    protected RestTemplate restTemplate = new RestTemplate();
    /**
     * 将PageInfo 转Page
     * @param pageInfo
     * @return
     */
    protected static Page convert(PageInfo pageInfo){
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

    /**
     * 设置请求的uri
     * @param requestUrl
     * @return
     */
    protected URI getURI(String requestUrl){
        String url = messageConfig.getMessagecenterServerUrl() + requestUrl;
        URI uri = null;
        try {
            uri = new URI(url);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return uri;
    }
}
