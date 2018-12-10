/**
 * @author
 * @version 1.0
 * @desc AppConfig
 * @since 2018-07-11 17:04:59
 */

package com.feitai.admin.backend.config.web;

import com.alibaba.fastjson.JSON;
import com.feitai.admin.backend.config.entity.AppConfigType;
import com.feitai.admin.backend.config.service.AppConfigService;
import com.feitai.admin.backend.config.service.AppConfigTypeService;
import com.feitai.admin.core.service.DynamitSupportService;
import com.feitai.admin.core.service.Page;
import com.feitai.admin.core.web.BaseListableController;
import com.feitai.jieya.server.dao.appconfig.model.AppConfig;
import com.feitai.utils.SnowFlakeIdGenerator;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import javax.validation.Valid;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping(value = "/backend/appConfig")
@Slf4j
public class AppConfigController extends BaseListableController<AppConfig> {

    @Autowired
    private AppConfigService appConfigService;
    @Autowired
    private AppConfigTypeService appConfigTypeService;


    @RequestMapping(value = "index")
    public String index(Model model) {
        Map<String, String> configTypeMap = new HashMap<String, String>();
        List<AppConfigType> allAppConfigType = appConfigTypeService.findAll();//所以的app配置类型
        for (AppConfigType appConfigType : allAppConfigType) {
            configTypeMap.put(appConfigType.getTypeCode(), appConfigType.getName());
        }
        String configTypeMapJson = JSON.toJSONString(configTypeMap);
        String configTypeMapJsonSingle = configTypeMapJson.replaceAll("\"", "\'");
        model.addAttribute("configTypeMap", configTypeMapJson);
        model.addAttribute("configTypeMapAdd", configTypeMapJsonSingle);
        configTypeMap.put(" ", "全部");
        configTypeMapJson = JSON.toJSONString(configTypeMap);
        configTypeMapJsonSingle = configTypeMapJson.replaceAll("\"", "\'");
        model.addAttribute("configTypeMapSearch", configTypeMapJsonSingle);
        return "/backend/appConfig/index";
    }

    @Override
    @RequiresPermissions("/backend/appConfig:list")
    @RequestMapping(value = "list")
    @ResponseBody
    public Page<AppConfig> list(ServletRequest request) {
        return super.list(request);
    }

    @RequiresPermissions("/backend/appConfig:update")
    @RequestMapping(value = "update/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Object editFrom(@PathVariable("id") Long id) {
        AppConfig appConfig = this.appConfigService.findOne(id);
        return appConfig;
    }

    @RequiresPermissions("/backend/appConfig:add")
    @RequestMapping(value = "add", method = RequestMethod.POST)
    @ResponseBody
    public Object add(@Valid AppConfig appConfig) {
        appConfig.setId(SnowFlakeIdGenerator.getDefaultNextId());
        appConfig.setUpdateTime(new Date());
        this.appConfigService.save(appConfig);
        return BaseListableController.successResult;
    }

    @RequiresPermissions("/backend/appConfig:update")
    @RequestMapping(value = "update", method = RequestMethod.POST)
    @ResponseBody
    public Object update(@Valid @ModelAttribute AppConfig appConfig) {
        this.appConfigService.save(appConfig);
        return BaseListableController.successResult;
    }

    @RequiresPermissions("/backend/appConfig:del")
    @RequestMapping(value = "del")
    @ResponseBody
    public Object del(@RequestParam(value = "ids[]") Long[] ids) {
        this.appConfigService.delete(ids);
        return BaseListableController.successResult;
    }


    /**
     * 所有RequestMapping方法调用前的Model准备方法, 实现Struts2 Preparable二次部分绑定的效果,先根据form的id从数据库查出User对象,再把Form提交的内容绑定到该对象上。
     * 因为仅update()方法的form中有id属性，因此仅在update时实际执行.
     */
    @ModelAttribute
    public void getappConfig(@RequestParam(value = "id", defaultValue = "-1") Long id, Model model) {
        if (id != -1) {
            model.addAttribute("appConfig", this.appConfigService.findOne(id));
        }
    }

    @Override
    protected DynamitSupportService<AppConfig> getService() {
        return this.appConfigService;
    }

}
