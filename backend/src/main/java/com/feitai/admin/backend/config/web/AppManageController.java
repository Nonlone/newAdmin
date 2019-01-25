package com.feitai.admin.backend.config.web;

import com.feitai.admin.backend.config.entity.AppManage;
import com.feitai.admin.backend.config.service.AppManageService;
import com.feitai.admin.core.service.DynamitSupportService;
import com.feitai.admin.core.service.Page;
import com.feitai.admin.core.web.BaseListableController;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletRequest;
import javax.validation.Valid;

/**
 * detail:app管理
 * author:longhaoteng
 * date:2019/1/22
 */
@Controller
@RequestMapping(value = "/backend/appManage")
@Slf4j
public class AppManageController extends BaseListableController<AppManage> {

    @Autowired
    private AppManageService appManageService;

    @RequestMapping("/index")
    @RequiresPermissions("/backend/appManage:list")
    public ModelAndView index(){
        ModelAndView modelAndView = new ModelAndView("/backend/appManage/index");
        return modelAndView;
    }

    @RequiresPermissions("/backend/appManage:list")
    @RequestMapping(value = "list")
    @ResponseBody
    public Object listPage(ServletRequest request) {
        Page<AppManage> listPage = super.list(request);
        return listPage;
    }

    @Override
    protected DynamitSupportService<AppManage> getService() {
        return this.appManageService;
    }

    @RequiresPermissions("/backend/appManage:update")
    @RequestMapping(value = "update/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Object editFrom(@PathVariable("id") Long id) {
        AppManage appManage = this.appManageService.findOne(id);
        return appManage;
    }

    @RequiresPermissions("/backend/appManage:add")
    @RequestMapping(value = "add", method = RequestMethod.POST)
    @ResponseBody
    public Object add(@Valid AppManage appManage){
        this.appManageService.save(appManage);
        return BaseListableController.successResult;
    }

    @RequiresPermissions("/backend/appManage:update")
    @RequestMapping(value = "update", method = RequestMethod.POST)
    @ResponseBody
    public Object update(@Valid @ModelAttribute("appManage") AppManage appManage){
        this.appManageService.save(appManage);
        return BaseListableController.successResult;
    }

    @RequiresPermissions("/backend/appVersion:del")
    @RequestMapping(value = "del")
    @ResponseBody
    public Object del(@RequestParam(value = "ids[]") Long[] ids){
        this.appManageService.delete(ids);
        return BaseListableController.successResult;
    }


    /**
     * 所有RequestMapping方法调用前的Model准备方法, 实现Struts2 Preparable二次部分绑定的效果,先根据form的id从数据库查出User对象,再把Form提交的内容绑定到该对象上。
     * 因为仅update()方法的form中有id属性，因此仅在update时实际执行.
     */
    @ModelAttribute
    public void getAppManage(@RequestParam(value = "id", defaultValue = "-1") Long id, Model model) {
        if (id != -1) {
            model.addAttribute("appManage", this.appManageService.findOne(id));
        }
    }

}
