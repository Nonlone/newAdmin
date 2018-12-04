/**
 * @author
 * @version 1.0
 * @desc Advertise
 * @since 2018-07-11 14:35:04
 */

package com.feitai.admin.backend.content.web;

import com.feitai.admin.backend.content.service.AdvertiseService;
import com.feitai.admin.core.service.DynamitSupportService;
import com.feitai.admin.core.web.BaseCrudController;
import com.feitai.jieya.server.dao.cms.model.Advertise;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping(value = "/backend/advertise")
@Slf4j
public class AdvertiseController extends BaseCrudController<Advertise> {
    @Autowired
    private AdvertiseService advertiseService;

    @RequestMapping(value = "")
    public String index() {
        return "/backend/advertise/index";
    }




    @Override
    protected DynamitSupportService<Advertise> getService() {
        return this.advertiseService;
    }


}
