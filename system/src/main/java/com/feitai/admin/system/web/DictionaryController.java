package com.feitai.admin.system.web;

import com.feitai.admin.core.annotation.LogAnnotation;
import com.feitai.admin.core.service.DynamitSupportService;
import com.feitai.admin.core.service.Page;
import com.feitai.admin.core.service.Sort;
import com.feitai.admin.core.vo.ListItem;
import com.feitai.admin.core.web.BaseListableController;
import com.feitai.admin.system.model.Dictionary;
import com.feitai.admin.system.service.DictionaryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/system/dictionary")
@Slf4j
public class DictionaryController extends BaseListableController<Dictionary> {

    @Autowired
    private DictionaryService dictionaryService;

    @RequiresPermissions("/system/dictionary:list")
    @RequestMapping(value = "")
    public String index() {
        return "/system/dictionary/index";
    }

    @RequiresPermissions("/system/dictionary:list")
    @RequestMapping(value = "list")
    @ResponseBody
    @LogAnnotation(value = true, writeRespBody = false)
    public Object listPage(ServletRequest request) {
        Page<Dictionary> listPage = super.list(request);
        return listPage;
    }

    /**
     * 请求地址： /system/dictionary/search?search_EQ_dictionaryType.code=city
     * <p>
     * 如果需要在头部加上一个item 则可以访问
     * <p>
     * /system/dictionary/search?search_EQ_dictionaryType.code=city&headText=
     * 全部地市 则会在返回的json头部增加一个 item text为全部地市 value为空
     * <p>
     * 当然headValue也可以赋值
     *
     * @param request
     * @return [{"text":"广州","value":"020"},{"text":"深圳","value":"0754"}
     * @throws UnsupportedEncodingException
     */
    @RequestMapping(value = "search")
    @ResponseBody
    @LogAnnotation(value = true, writeRespBody = false)
    public Object listByDictionaryTypeId(
            HttpServletRequest request,
            @RequestParam(value = "headText", defaultValue = "") String headText,
            @RequestParam(value = "headValue", defaultValue = "") String headValue,
            @RequestParam(value = "headCharsetName", defaultValue = "iso8859-1") String headCharsetName) throws UnsupportedEncodingException {
        Sort sort = new Sort( "orderId",Sort.Direction.ASC);
        Map<String, Object> searchParams = WebUtils.getParametersStartingWith(request, "search_");
        //当使用GET操作时需要传编码进行转码
        if(request.getMethod()=="GET"){
            headText = new String(headText.getBytes(headCharsetName));
        }
        List<Dictionary> list = this.dictionaryService.searchListItem(
                buildSearchParams(searchParams), sort, headText, headValue);
        List<ListItem> items = new ArrayList<>(list.size() + 1);
        if (StringUtils.isNotBlank(headText)) {
            ListItem item = new ListItem(headText, headValue);
            items.add(item);
        }
        for (Dictionary dictionary : list) {
            ListItem item = new ListItem(dictionary.getName(), dictionary.getValue());
            items.add(item);
        }
        return items;
    }


    /**
     * 返回枚举类型如 ： {"020":"广州","0755":"深圳"}
     * 请求地址： /system/dictionary/searchEnum?search_EQ_dictionaryType.code=city
     * <p>
     * 如果需要在头部加上一个item 则可以访问
     * <p>
     * /system/dictionary/search?search_EQ_dictionaryType.code=city&headText=
     * 全部地市 则会在返回的json头部增加一个 item text为全部地市 value为空
     * <p>
     * 当然headValue也可以赋值
     *
     * @param request
     * @return [{"text":"广州","value":"020"},{"text":"深圳","value":"0754"}
     * @throws UnsupportedEncodingException
     */
    @RequestMapping(value = "searchEnum")
    @ResponseBody
    @LogAnnotation(value = true, writeRespBody = false)
    public Object searchEnumByDictionaryTypeId(HttpServletRequest request) {
        Map<String, Object> searchParams = WebUtils.getParametersStartingWith(request, "search_");
        Map<String, String> map = this.dictionaryService.searchEnum(buildSearchParams(searchParams));
        return map;
    }

    @RequiresPermissions("/system/dictionary:update")
    @RequestMapping(value = "update/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Object editFrom(@PathVariable("id") Long id) {
        Dictionary dictionary = this.dictionaryService.findOne(id);
        return dictionary;
    }

    @RequiresPermissions("/system/dictionary:add")
    @RequestMapping(value = "add", method = RequestMethod.POST)
    @ResponseBody
    public Object add(@Valid Dictionary dictionary) {
        this.dictionaryService.save(dictionary);
        return successResult;
    }

    @RequiresPermissions("/system/dictionary:update")
    @RequestMapping(value = "update", method = RequestMethod.POST)
    @ResponseBody
    public Object update(
            @Valid @ModelAttribute("dictionary") Dictionary dictionary) {
        this.dictionaryService.save(dictionary);
        return successResult;
    }

    @RequiresPermissions("/system/dictionary:del")
    @RequestMapping(value = "del")
    @ResponseBody
    public Object del(@RequestParam(value = "ids[]") Long[] ids) {
        this.dictionaryService.delete(ids);
        return successResult;
    }

    /**
     * 所有RequestMapping方法调用前的Model准备方法, 实现Struts2
     * Preparable二次部分绑定的效果,先根据form的id从数据库查出User对象,再把Form提交的内容绑定到该对象上。
     * 因为仅update()方法的form中有id属性，因此仅在update时实际执行.
     */
    @ModelAttribute
    public void getDictionary(
            @RequestParam(value = "id", defaultValue = "-1") Long id,
            Model model) {
        if (id != -1) {
            model.addAttribute("dictionary", this.dictionaryService.findOne(id));
        }
    }

    @Override
    protected DynamitSupportService<Dictionary> getService() {
        return this.dictionaryService;
    }


    @InitBinder
    public void initDate(WebDataBinder webDataBinder) {
        webDataBinder.addCustomFormatter(new DateFormatter("yyyy-MM-dd HH:mm:ss"));
        webDataBinder.addCustomFormatter(new DateFormatter("yyyy-MM-dd"));
    }

}
