/**
 * @author
 * @version 1.0
 * @desc Advertise
 * @since 2018-07-11 14:35:04
 */

package com.feitai.admin.backend.content.web;

import com.feitai.admin.backend.content.service.AdvertiseService;
import com.feitai.admin.core.service.DynamitSupportService;
import com.feitai.admin.core.web.BaseListableController;
import com.feitai.jieya.server.dao.cms.model.Advertise;
import com.feitai.utils.SnowFlakeIdGenerator;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

import javax.servlet.ServletRequest;
import javax.validation.Valid;


@Controller
@RequestMapping(value = "/backend/advertise")
@Slf4j
public class AdvertiseController extends BaseListableController<Advertise> {
    @Autowired
    private AdvertiseService advertiseService;

    @RequestMapping(value = "index")
    public String index() {
        return "/backend/advertise/index";
    }

    @RequiresPermissions("/backend/advertise:list")
    @RequestMapping(value = "list")
    @ResponseBody
    public Object listPage(ServletRequest request) {
        return list(request);
    }

    @RequiresPermissions("/backend/advertise:update")
    @RequestMapping(value = "update/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Object editFrom(@PathVariable("id") Long id) {
        Advertise advertise = this.advertiseService.findOne(id);
        return advertise;
    }

    @RequiresPermissions("/backend/advertise:add")
    @RequestMapping(value = "add", method = RequestMethod.POST)
    @ResponseBody
    public Object add(@Valid @ModelAttribute Advertise advertise) {
        advertise.setId(SnowFlakeIdGenerator.getDefaultNextId());
        advertise.setUpdateTime(new Date());
        this.advertiseService.save(advertise);
        return successResult;
    }

    @RequiresPermissions("/backend/advertise:update")
    @RequestMapping(value = "update", method = RequestMethod.POST)
    @ResponseBody
    public Object update(@Valid @ModelAttribute Advertise advertise) {
    	advertise.setUpdateTime(new Date());
        this.advertiseService.updateByPrimaryKey(advertise);
        return successResult;
    }

    @RequiresPermissions("/backend/advertise:del")
    @RequestMapping(value = "del")
    @ResponseBody
    public Object del(@RequestParam(value = "ids[]") Long[] ids) {
        this.advertiseService.delete(ids);
        return successResult;
    }


    /**
     * 所有RequestMapping方法调用前的Model准备方法, 实现Struts2 Preparable二次部分绑定的效果,先根据form的id从数据库查出User对象,再把Form提交的内容绑定到该对象上。
     * 因为仅update()方法的form中有id属性，因此仅在update时实际执行.
     */
    @ModelAttribute
    public void getadvertise(@RequestParam(value = "id", defaultValue = "-1") Long id, Model model) {
        if (id != -1) {
            model.addAttribute("advertise", this.advertiseService.findOne(id));
        }
    }


    @Override
    protected DynamitSupportService<Advertise> getService() {
        return this.advertiseService;
    }


}
