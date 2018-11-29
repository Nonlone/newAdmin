package com.feitai.admin.backend.creditdata.web;

import com.feitai.admin.backend.config.service.AppConfigService;
import com.feitai.admin.backend.creditdata.service.AuthdataTobaccoService;
import com.feitai.admin.backend.creditdata.vo.AuthdataTobacooViewVo;
import com.feitai.admin.core.service.DynamitSupportService;
import com.feitai.admin.core.web.BaseListableController;
import com.feitai.jieya.server.dao.authdata.model.AuthdataTobacco;
import com.feitai.utils.ObjectUtils;
import com.sun.xml.internal.bind.v2.TODO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * detail:烟草贷补充资料接口
 * author:longhaoteng
 * date:2018/11/29
 */
@Controller
@RequestMapping(value = "/backend/tobacco/")
@Slf4j
public class AuthdataTobaccoController extends BaseListableController<AuthdataTobacco> {

    @Autowired
    private AuthdataTobaccoService authdataTobaccoService;

    @Autowired
    private AppConfigService appConfigService;

    private final static String CERTIFI_TYPE = "certificatesType";

    private final static String SOCIAL_TYPE = "socialType";

    @GetMapping(value = "detail/{userId}")
    public ModelAndView detail(@PathVariable("userId") String userId){
        ModelAndView modelAndView = new ModelAndView("/backend/credit/tobacco/detail");
        AuthdataTobacco authdataTobaccoByUserId = authdataTobaccoService.findAuthdataTobaccoByUserId(Long.parseLong(userId));
        AuthdataTobacooViewVo authdataTobacooViewVo = new AuthdataTobacooViewVo();
        if(!ObjectUtils.allNotNull(authdataTobaccoByUserId)){
            return modelAndView;
        }
        BeanUtils.copyProperties(authdataTobaccoByUserId,authdataTobacooViewVo);
        // 字典映射处理
        String spouseIdTypeName = appConfigService.findByTypeCodeAndCode(CERTIFI_TYPE, authdataTobaccoByUserId.getSpouseIdType());
        authdataTobacooViewVo.setSpouseIdTypeName(spouseIdTypeName);

        String socialTypeName = appConfigService.findByTypeCodeAndCode(SOCIAL_TYPE, authdataTobaccoByUserId.getSocialType());
        authdataTobacooViewVo.setSocialTypeName(socialTypeName);

        modelAndView.addObject("authdataTobacoo",authdataTobacooViewVo);
        return modelAndView;
    }

    @Override
    protected DynamitSupportService<AuthdataTobacco> getService() {
        return this.authdataTobaccoService;
    }
}
