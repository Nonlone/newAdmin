package com.feitai.admin.system.web;

import com.feitai.admin.core.annotation.LogAnnotation;
import com.feitai.admin.core.service.DynamitSupportService;
import com.feitai.admin.core.service.Page;
import com.feitai.admin.core.vo.ListItem;
import com.feitai.admin.core.web.BaseListableController;
import com.feitai.admin.system.model.DictionaryType;
import com.feitai.admin.system.service.DictionaryTypeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/system/dictionaryType")
@Slf4j
public class DictionaryTypeController extends BaseListableController<DictionaryType> {

    @Autowired
    private DictionaryTypeService dictionaryTypeService;

    @RequiresPermissions("/system/dictionaryType:list")
    @RequestMapping(value = "")
    public String index() {
        return "/system/dictionaryType/index";
    }


    @RequiresPermissions("/system/dictionaryType:list")
    @RequestMapping(value = "list")
    @ResponseBody
    @LogAnnotation(value = true, writeRespBody = false)// 写日志但是不打印请求的params,但不打印ResponseBody的内容
    public Object listPage(ServletRequest request) {
        Page<DictionaryType> listPage = super.list(request);
        return listPage;
    }

    @RequiresPermissions("/system/dictionary:list")
    @RequestMapping(value = "listAll")
    @ResponseBody
    @LogAnnotation(value = true, writeRespBody = false)// 写日志但是不打印请求的params,但不打印ResponseBody的内容
    public Object listAll() {
        List<DictionaryType> list = this.dictionaryTypeService.findAll();
        List<ListItem> items = new ArrayList<>(list.size());
        for (DictionaryType dictionaryType : list) {
            ListItem item = new ListItem(dictionaryType.getName(),
                    String.valueOf(dictionaryType.getId()));
            items.add(item);
        }
        return items;
    }


    // 特别设定多个ReuireRoles之间为Or关系，而不是默认的And.
    @RequiresPermissions("/system/dictionaryType:update")
    @RequestMapping(value = "update/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Object editFrom(@PathVariable("id") Long id) {
        DictionaryType dictionaryType = this.dictionaryTypeService.findOne(id);
        return dictionaryType;
    }

    @RequiresPermissions("/system/dictionaryType:add")
    @RequestMapping(value = "add", method = RequestMethod.POST)
    @ResponseBody
    public Object add(@Valid DictionaryType dictionaryType) {
        this.dictionaryTypeService.save(dictionaryType);
        return successResult;
    }

    @RequiresPermissions("/system/dictionaryType:update")
    @RequestMapping(value = "update", method = RequestMethod.POST)
    @ResponseBody
    public Object update(@Valid @ModelAttribute("dictionaryType") DictionaryType dictionaryType) {
        this.dictionaryTypeService.save(dictionaryType);
        return successResult;
    }

    @RequiresPermissions("/system/dictionaryType:del")
    @RequestMapping(value = "del")
    @ResponseBody
    public Object del(@RequestParam(value = "ids[]") Long[] ids) {
        this.dictionaryTypeService.delete(ids);
        return successResult;
    }


    /**
     * 所有RequestMapping方法调用前的Model准备方法, 实现Struts2 Preparable二次部分绑定的效果,先根据form的id从数据库查出User对象,再把Form提交的内容绑定到该对象上。
     * 因为仅update()方法的form中有id属性，因此仅在update时实际执行.
     */
    @ModelAttribute
    public void getDictionaryType(@RequestParam(value = "id", defaultValue = "-1") Long id, Model model) {
        if (id != -1) {
            model.addAttribute("dictionaryType", this.dictionaryTypeService.findOne(id));
        }
    }

    @Override
    protected DynamitSupportService<DictionaryType> getService() {
        return this.dictionaryTypeService;
    }



    @InitBinder
    public void initDate(WebDataBinder webDataBinder) {
        webDataBinder.addCustomFormatter(new DateFormatter("yyyy-MM-dd HH:mm:ss"));
        webDataBinder.addCustomFormatter(new DateFormatter("yyyy-MM-dd"));
    }

}
