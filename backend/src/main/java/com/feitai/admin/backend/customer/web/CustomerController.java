/**
 * @author
 * @version 1.0
 * @desc IdCardDataExtend
 * @since 2018-07-13 16:25:40
 */

package com.feitai.admin.backend.customer.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.feitai.admin.backend.auth.service.AuthdataLinkfaceLivenessIdnumberVerificationService;
import com.feitai.admin.backend.auth.service.AuthdataLinkfaceLivenessSelfieVerificationService;
import com.feitai.admin.backend.config.service.AppConfigService;
import com.feitai.admin.backend.customer.entity.IdCardDataExtend;
import com.feitai.admin.backend.customer.service.*;
import com.feitai.admin.backend.fund.service.FundService;
import com.feitai.admin.backend.fund.service.UserBankCardBindService;
import com.feitai.admin.backend.properties.MapProperties;
import com.feitai.admin.backend.service.AttachPhotoService;
import com.feitai.admin.core.service.*;
import com.feitai.admin.core.web.BaseListableController;
import com.feitai.admin.core.web.PageBulider;
import com.feitai.jieya.server.dao.attach.model.PhotoAttach;
import com.feitai.jieya.server.dao.bank.model.UserBankCardBind;
import com.feitai.jieya.server.dao.callback.model.linkface.LinkfaceLivenessIdNumberVerifcation;
import com.feitai.jieya.server.dao.callback.model.linkface.LinkfaceLivenessSelfieVerification;
import com.feitai.jieya.server.dao.data.model.IdCardData;
import com.feitai.jieya.server.dao.data.model.PersonData;
import com.feitai.jieya.server.dao.data.model.WorkData;
import com.feitai.jieya.server.dao.fund.model.Fund;
import com.feitai.jieya.server.dao.user.model.User;
import com.feitai.utils.Desensitization;
import com.feitai.utils.IdCardUtils;
import com.feitai.utils.StringUtils;
import com.feitai.utils.datetime.DateTimeStyle;
import com.feitai.utils.datetime.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping(value = "/backend/customer")
@Slf4j
public class CustomerController extends BaseListableController<IdCardDataExtend> {

    @Autowired
    private IdcardService idcardService;

    @Autowired
    private PersonService personService;

    @Autowired
    private AppConfigService appConfigService;

    @Autowired
    private UserService userService;

    @Autowired
    private WorkService workService;

    @Autowired
    private AttachPhotoService attachPhotoService;

    @Autowired
    private MapProperties mapProperties;

    @Autowired
    private AuthdataLinkfaceLivenessIdnumberVerificationService authdataLinkfaceLivenessIdnumberVerificationService;

    @Autowired
    private AuthdataLinkfaceLivenessSelfieVerificationService authdataLinkfaceLivenessSelfieVerificationService;

    @Autowired
    private UserBankCardBindService userBankCardBindService;

    @Autowired
    private FundService fundService;

    @Autowired
    private UserBankCardService userBankCardService;

    @RequestMapping(value = "/index")
    public String index() {
        return "/backend/customer/index";
    }




    @RequiresUser
    @RequiresPermissions("/backend/customer:list")
    @RequestMapping(value = "list")
    @ResponseBody
    public Object listPage(ServletRequest request) {
        //根据request获取page
        int pageNo = PageBulider.getPageNo(request);
        int pageSize = PageBulider.getPageSize(request);
        Page<IdCardDataExtend> idCardPage = list(getCommonSqls(request, getSelectMultiTable().buildSqlString()), pageNo, pageSize, getCountSqls(request), SelectMultiTable.COUNT_ALIAS);
        List<IdCardDataExtend> idCardDataExtendList = idCardPage.getContent();
        List<JSONObject> resultList = new ArrayList();
        for (IdCardDataExtend idCardDataExtend : idCardDataExtendList) {
            JSONObject json = (JSONObject) JSON.toJSON(idCardDataExtend);
            try {
                if (StringUtils.isNotBlank(idCardDataExtend.getIdCard())) {
                    json.put("birthday", DateUtils.format(IdCardUtils.getBirthdayByIdCard(idCardDataExtend.getIdCard()), DateTimeStyle.DEFAULT_YYYY_MM_DD));
                    json.put("age", IdCardUtils.getAgeByIdCard(idCardDataExtend.getIdCard()));
                }

                resultList.add(json);
            } catch (Exception e) {
                log.error(String.format("this json handle fail:[{}]! message:{}", json, e.getMessage()),e);
                continue;
            }
        }
        return switchContent(idCardPage, resultList);
    }


    @RequestMapping(value = "detail/{id}", method = RequestMethod.GET)
    public ModelAndView detail(@PathVariable("id") String id) {
        ModelAndView model = new ModelAndView("backend/customer/detail");
        // 身份证信息
        IdCardDataExtend idCardDataExtend = getService().findOne(id);
        model.addObject("idCardDataExtend", idCardDataExtend);
        Long userId = idCardDataExtend.getUserId();
        // 年龄
        model.addObject("age", IdCardUtils.getAgeByIdCard(idCardDataExtend.getIdCard()));
        // 生日
        model.addObject("birthday", DateUtils.format(IdCardUtils.getBirthdayByIdCard(idCardDataExtend.getIdCard()), DateTimeStyle.DEFAULT_YYYY_MM_DD));
        //脱敏处理
        if (idCardDataExtend != null) {
            String hyIdcard = Desensitization.idCard(idCardDataExtend.getIdCard());
            model.addObject("hyIdcard", hyIdcard);
        }
        //用户基本信息
        User user = userService.findOne(userId);
        model.addObject("user", user);
        if (user != null) {
            String hyPhone = Desensitization.phone(user.getPhone());
            model.addObject("hyPhone", hyPhone);
        }

        //商汤
        LinkfaceLivenessIdNumberVerifcation livenessIdnumberVerification = authdataLinkfaceLivenessIdnumberVerificationService.findByUserId(userId);
        LinkfaceLivenessSelfieVerification livenessSelfieVerification = authdataLinkfaceLivenessSelfieVerificationService.findByUserId(userId);
        if (livenessIdnumberVerification == null) {
            model.addObject("authVerify", "未对比");
        } else {
            model.addObject("authVerify", Objects.isNull(livenessIdnumberVerification.getVerifyScore()) ? "没对比" : livenessIdnumberVerification.getVerifyScore() * 100);
        }
        if (livenessSelfieVerification == null) {
            model.addObject("livingVerify", "未对比");
            model.addObject("hackScore", "未对比");
        } else {
            model.addObject("livingVerify", Objects.isNull(livenessSelfieVerification.getVerifyScore()) ? "没对比" : livenessSelfieVerification.getVerifyScore() * 100);
            model.addObject("hackScore", Objects.isNull(livenessSelfieVerification.getHackScore()) ? "没对比" : livenessSelfieVerification.getHackScore() * 100);
        }

        // 相片地址
        List<PhotoAttach> photos = attachPhotoService.findUserPhotoByUserId(userId);
        List<JSONObject> customerPhotos = new ArrayList<>();
        for (PhotoAttach photo : photos) {
            model.addObject("photo" + photo.getType(), photo.getPath());
            JSONObject json = new JSONObject();
            json.put("type", photo.getType().toString().toUpperCase());
            json.put("path", photo.getPath());
            json.put("name", mapProperties.getPhotoType(photo.getType()));
            customerPhotos.add(json);
        }
        model.addObject("customerPhotos", customerPhotos);

        // 个人信息
        PersonData person = personService.findByUserId(userId);
        model.addObject("person", person);
        // 婚姻状况最高学历
        if (person != null) {
            model.addObject("maritalStatus", appConfigService.findByTypeCodeAndCode("maritalStatus", person.getMaritalStatus()));
            model.addObject("fertilityStatus", appConfigService.findByTypeCodeAndCode("fertilityStatus", person.getFertilityStatus()));
            model.addObject("educationLevel", appConfigService.findByTypeCodeAndCode("educationLevel", person.getEducationLevel()));
            model.addObject("residentialType", appConfigService.findByTypeCodeAndCode("residentialType", person.getResidentialType()));
        } else {
            model.addObject("marital", "无");
            model.addObject("educationLevel", "无");
            model.addObject("residentialType", "无");
            model.addObject("fertilityStatus", "无");
        }

        // 工作信息
        WorkData work = workService.findByUserId(userId);
        model.addObject("work", work);
        if(work!=null){
            model.addObject("hyWorkContactPhone",Desensitization.phone(work.getContactPhone()));
            model.addObject("belongIndustry", appConfigService.findByTypeCodeAndCode("belongIndustry", work.getBelongIndustry()));
            model.addObject("jobsType", appConfigService.findByTypeCodeAndCode("jobsType", work.getJobsType()));
        }else{
            model.addObject("belongIndustry", "无");
            model.addObject("jobsType", "无");
        }



        return model;
    }


    @Override
    protected DynamitSupportService<IdCardDataExtend> getService() {
        return this.idcardService;
    }

    private SelectMultiTable getSelectMultiTable() {
        return SelectMultiTable.builder(IdCardData.class)
                .rightJoin(User.class, "user", new OnCondition[]{
                        new OnCondition(SelectMultiTable.ConnectType.AND, "userId", Operator.EQ, "id"),
                });
    }

    private String getCountSqls(ServletRequest request) {
        StringBuffer sbSql = new StringBuffer();
        sbSql.append((SelectMultiTable.builder(IdCardData.class)).buildCountSqlString());
        List<SearchParams> searchParamsList = bulidSearchParamsList(request);
        searchParamsList.add(new SearchParams(IdCardData::getCertified, Operator.EQ, Boolean.TRUE.toString()));
        sbSql.append(getService().buildSqlWhereCondition(searchParamsList, SelectMultiTable.MAIN_ALAIS));
        return sbSql.toString();
    }

}
