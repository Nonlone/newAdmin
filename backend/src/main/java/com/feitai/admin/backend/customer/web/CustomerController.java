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
import com.feitai.admin.backend.service.AttachPhotoService;
import com.feitai.admin.core.service.*;
import com.feitai.admin.core.web.BaseListableController;
import com.feitai.admin.core.web.PageBulider;
import com.feitai.jieya.server.dao.attach.model.PhotoAttach;
import com.feitai.jieya.server.dao.bank.model.UserBankCardBind;
import com.feitai.jieya.server.dao.callback.model.linkface.LinkfaceLivenessIdNumberVerifcation;
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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletRequest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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


    /***
     * 根据银行卡号获取资金方及其状态
     * @param bankCardNo
     * @return
     */
    private String getFunds(String bankCardNo) {
        List<UserBankCardBind> userBankCardBinds = userBankCardBindService.findByBankCardNo(bankCardNo);
        String funds = "";
        for (UserBankCardBind userBankCardBind :
                userBankCardBinds) {
            Fund fund = fundService.findOne(userBankCardBind.getSeqNo());
            String status = userBankCardBind.getStatus();
            switch (status) {
                case "NONE":
                    funds += fund.getFundName() + "（未绑卡）,";
                    break;
                case "AUDIT":
                    funds += fund.getFundName() + "（审核中）,";
                    break;
                case "PASSED":
                    funds += fund.getFundName() + "（审核通过）,";
                    break;
                case "BACK":
                    funds += fund.getFundName() + "（审核回退）,";
                    break;
                case "REFUSED":
                    funds += fund.getFundName() + "（审核拒绝）,";
                    break;
            }
        }
        if (funds.length() != 0) {
            funds = funds.substring(0, funds.length() - 1);
        }
        return funds;
    }


    @RequiresUser
    @RequiresPermissions("/backend/customer:list")
    @RequestMapping(value = "list")
    @ResponseBody
    public Object listPage(ServletRequest request) {
        //根据request获取page
        int pageNo = PageBulider.getPageNo(request);
        int pageSize = PageBulider.getPageSize(request);
        Page<IdCardDataExtend> idCardPage = list(getSqls(request), pageNo, pageSize, getCountSqls(request), SelectMultiTable.COUNT_ALIAS);
        List<IdCardDataExtend> idCardDataExtendList = idCardPage.getContent();
        List<JSONObject> resultList = new ArrayList();
        for (IdCardDataExtend idCardDataExtend : idCardDataExtendList) {
            JSONObject json = (JSONObject) JSON.toJSON(idCardDataExtend);
            if (StringUtils.isNotBlank(idCardDataExtend.getIdCard())) {
                json.put("birthday", DateUtils.format(IdCardUtils.getBirthdayByIdCard(idCardDataExtend.getIdCard()), DateTimeStyle.DEFAULT_YYYY_MM_DD));
                json.put("age", IdCardUtils.getAgeByIdCard(idCardDataExtend.getIdCard()));
            }
            resultList.add(json);
        }
        return switchContent(idCardPage, resultList);
    }


    @RequestMapping(value = "detail/{id}", method = RequestMethod.GET)
    public String detail(@PathVariable("id") String id, Model model) {
        //身份证信息
        IdCardDataExtend idCardDataExtend = getService().findOne(id);
        Long userId = idCardDataExtend.getUserId();

        //年龄
        int year = new Date().getYear() - idCardDataExtend.getBirthday().getYear();
        model.addAttribute("year", year);
        //生日
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        PersonData person = personService.findByUserId(userId);

        //婚姻状况最高学历
        String marital = "";
        String educationLevel = "";
        if (person != null) {
            marital = appConfigService.findByTypeCodeAndCode("maritalStatus", person.getMaritalStatus());
            educationLevel = appConfigService.findByTypeCodeAndCode("educationLevel", person.getEducationLevel());
        }
        //用户基本信息
        User user = userService.findOne(userId);
        //单位信息
        WorkData work = workService.findByUserId(userId);
        //相片地址
        List<PhotoAttach> photos = attachPhotoService.findByUserId(userId);
        for (PhotoAttach attachPhoto : photos) {
            model.addAttribute("photo" + attachPhoto.getType(), attachPhoto.getPath());

        }
        //脱敏处理
        if (idCardDataExtend != null) {
            String hyIdcard = Desensitization.idCard(idCardDataExtend.getIdCard());
            model.addAttribute("hyIdcard", hyIdcard);
        }
        if (user != null) {
            String hyPhone = Desensitization.phone(user.getPhone());
            model.addAttribute("hyPhone", hyPhone);
        }
        //商汤
        LinkfaceLivenessIdNumberVerifcation livenessIdnumberVerification = authdataLinkfaceLivenessIdnumberVerificationService.findByUserId(userId);
        //AuthdataLinkfaceLivenessSelfieVerification livenessSelfieVerification =authdataLinkfaceLivenessSelfieVerificationService.findByUserId(userId);
        if (livenessIdnumberVerification == null) {
            model.addAttribute("gongan", "未对比");
        } else {
            model.addAttribute("gongan", livenessIdnumberVerification.getVerifyScore() * 100);
        }
        //model.addAttribute("huoti", livenessSelfieVerification.getVerifyScore());

        model.addAttribute("birthday", format.format(idCardDataExtend.getBirthday()));
        model.addAttribute("person", person);
        model.addAttribute("marital", marital);
        model.addAttribute("educationLevel", educationLevel);
        model.addAttribute("idCardDataExtend", idCardDataExtend);
        model.addAttribute("user", user);
        model.addAttribute("work", work);
        return "backend/customer/detail";
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

    private String getSqls(ServletRequest request) {
        StringBuffer sbSql = new StringBuffer();
        sbSql.append(getSelectMultiTable().buildSqlString());
        List<SearchParams> searchParamsList = bulidSearchParamsList(request);
        searchParamsList.add(new SearchParams(IdCardData::getCertified, Operator.EQ, true));
        sbSql.append(getService().buildSqlWhereCondition(searchParamsList, SelectMultiTable.MAIN_ALAIS));
        return sbSql.toString();
    }

    private String getCountSqls(ServletRequest request) {
        StringBuffer sbSql = new StringBuffer();
        sbSql.append(getSelectMultiTable().buildCountSqlString());
        List<SearchParams> searchParamsList = bulidSearchParamsList(request);
        searchParamsList.add(new SearchParams(IdCardData::getCertified, Operator.EQ, Boolean.TRUE.toString()));
        sbSql.append(getService().buildSqlWhereCondition(searchParamsList, SelectMultiTable.MAIN_ALAIS));
        return sbSql.toString();
    }


    protected String getFindByIdSql(Object id) {
        String sql = SelectMultiTable.builder(IdCardData.class)
                .leftJoin(User.class, "user_in", new OnCondition[]{
                        new OnCondition(SelectMultiTable.ConnectType.AND, "userId", Operator.EQ, "id"),
                }).buildSqlString() + " where maintable.id = '" + id + "'" + " GROUP BY maintable.id";
        return sql;
    }

    protected String getFindByUserIdSql(Object userId) {
        String sql = SelectMultiTable.builder(IdCardData.class)
                .leftJoin(User.class, "user_in", new OnCondition[]{
                        new OnCondition(SelectMultiTable.ConnectType.AND, "userId", Operator.EQ, "id"),
                }).buildSqlString() + " where maintable.user_id = '" + userId + "'" + " GROUP BY maintable.id";
        return sql;
    }


}
