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
import com.feitai.admin.core.web.BaseCrudController;
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
public class AppConfigController extends BaseCrudController<AppConfig> {

    @Autowired
    private AppConfigService appConfigService;
    @Autowired
    private AppConfigTypeService appConfigTypeService;


    @RequestMapping(value = "index")
    @RequiresPermissions("/backend/appConfig:list")
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
    protected DynamitSupportService<AppConfig> getService() {
        return this.appConfigService;
    }

}
