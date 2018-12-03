package com.feitai.admin.system.web;

import com.feitai.admin.core.annotation.LogAnnotation;
import com.feitai.admin.core.service.*;
import com.feitai.admin.core.vo.TreeItem;
import com.feitai.admin.core.web.BaseListableController;
import com.feitai.admin.system.model.Resource;
import com.feitai.admin.system.service.ResourceService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Controller
@RequestMapping(value = "/system/resource")
@Slf4j
public class ResourceController extends BaseListableController<Resource> {

    @Autowired
    private ResourceService resourceService;

    @RequestMapping(value = "")
    public String index() {
        return "/system/resource/index";
    }

    @RequiresPermissions(value = {"/system/resource:list", "/system/role:update"}, logical = Logical.OR)
    @RequestMapping(value = "resourceTree")
    @ResponseBody
    @LogAnnotation(value = true, writeRespBody = false)// 写日志但是不打印请求的params,但不打印ResponseBody的内容
    public Object resourceTree() {
        List<TreeItem> rsourceList = this.resourceService.getRsourceList();
        return rsourceList;
    }

    @RequiresPermissions("/system/resource:list")
    @RequestMapping(value = "list")
    @ResponseBody
    @LogAnnotation(value = true, writeRespBody = false)// 写日志但是不打印请求的params,但不打印ResponseBody的内容
    public Object listPage(ServletRequest request) {
        final Page<Resource> listPage = super.list(request, getSql());
        return listPage;
    }

    // 特别设定多个ReuireResources之间为Or关系，而不是默认的And.
    @RequiresPermissions("/system/resource:update")
    @RequestMapping(value = "update/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Object editFrom(@PathVariable("id") Long id) {
        Resource resource = this.resourceService.findOne(id);
        return resource;
    }


    @RequiresPermissions("/system/resource:add")
    @RequestMapping(value = "add", method = RequestMethod.POST)
    @ResponseBody
    public Object add(@Valid Resource resource) {
        //如果Parent的ID为空，则需要把Parent设置为空，否则会报错
        if (StringUtils.isBlank(resource.getParentId())) {
            resource.setParentId(null);
        }
        this.resourceService.save(resource);
        return successResult;
    }

    @RequiresPermissions("/system/resource:update")
    @RequestMapping(value = "update", method = RequestMethod.POST)
    @ResponseBody
    public Object update(@Valid @ModelAttribute("resource") Resource resource) {
        //如果Parent的ID为空，则需要把Parent设置为空，否则会报错
        if (resource.getParent() != null && (resource.getParent().getId() == null || resource.getParent().getId() == 0)) {
            resource.setParent(null);
        }
        this.resourceService.save(resource);
        return successResult;
    }

    @RequiresPermissions("/system/resource:del")
    @RequestMapping(value = "del")
    @ResponseBody
    public Object del(@RequestParam(value = "ids[]") Long[] ids) {
        this.resourceService.delete(ids);
        return successResult;
    }

    /**
     * 所有RequestMapping方法调用前的Model准备方法, 实现Struts2
     * Preparable二次部分绑定的效果,先根据form的id从数据库查出User对象,再把Form提交的内容绑定到该对象上。
     * 因为仅update()方法的form中有id属性，因此仅在update时实际执行.
     */
    @ModelAttribute
    public void getResource(@RequestParam(value = "id", defaultValue = "-1") Long id, Model model) {
        if (id != -1) {
            model.addAttribute("resource", this.resourceService.findOne(id));
        }
    }

    @Override
    protected DynamitSupportService<Resource> getService() {
        return this.resourceService;
    }

    protected String getSql() {
        String sql = SelectMultiTable.builder(Resource.class)
                .leftJoin(Resource.class, "resource", new OnCondition[]{
                        new OnCondition(SelectMultiTable.ConnectType.AND, "id", Operator.EQ, "parentId"),
                }).buildSqlString();
        return sql;
    }


    @InitBinder
    public void initDate(WebDataBinder webDataBinder) {
        webDataBinder.addCustomFormatter(new DateFormatter("yyyy-MM-dd HH:mm:ss"));
        webDataBinder.addCustomFormatter(new DateFormatter("yyyy-MM-dd"));
    }

}
