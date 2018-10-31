/**
 * @author
 * @version 1.0
 * @desc 用户持有对应信用产品记录
 * @since 2018-08-06 09:28:35
 */

package com.feitai.admin.backend.opencard.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.feitai.admin.backend.auth.entity.AuthdataTempAuth;
import com.feitai.admin.backend.auth.service.AuthdataAuthService;
import com.feitai.admin.backend.auth.service.AuthdataLinkfaceLivenessIdnumberVerificationService;
import com.feitai.admin.backend.auth.service.AuthdataLinkfaceLivenessSelfieVerificationService;
import com.feitai.admin.backend.auth.service.AuthdataTempAuthService;
import com.feitai.admin.backend.config.service.AppConfigService;
import com.feitai.admin.backend.customer.service.*;
import com.feitai.admin.backend.opencard.entity.CardMore;
import com.feitai.admin.backend.opencard.service.CardService;
import com.feitai.admin.backend.properties.MapProperties;
import com.feitai.admin.backend.service.AttachPhotoService;
import com.feitai.admin.core.service.*;
import com.feitai.admin.core.web.BaseListableController;
import com.feitai.admin.core.web.PageBulider;
import com.feitai.jieya.server.dao.attach.model.PhotoAttach;
import com.feitai.jieya.server.dao.authdata.model.AuthData;
import com.feitai.jieya.server.dao.authdata.model.BaseAuthData;
import com.feitai.jieya.server.dao.callback.model.linkface.LinkfaceLivenessIdNumberVerifcation;
import com.feitai.jieya.server.dao.callback.model.linkface.LinkfaceLivenessSelfieVerification;
import com.feitai.jieya.server.dao.data.model.*;
import com.feitai.jieya.server.dao.product.model.Product;
import com.feitai.jieya.server.dao.user.model.User;
import com.feitai.utils.CollectionUtils;
import com.feitai.utils.Desensitization;
import com.feitai.utils.StringUtils;
import com.google.common.base.Joiner;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;


@Controller
@RequestMapping(value = "/backend/opencard")
@Slf4j
public class OpenCardController extends BaseListableController<CardMore> {

    @Autowired
    private CardService cardService;

    @Autowired
    private IdcardService idcardService;

    @Autowired
    private UserInService userInService;

    @Autowired
    private PersonService personService;

    @Autowired
    private AppConfigService appConfigService;

    @Autowired
    private WorkService workService;

    @Autowired
    private AuthdataAuthService authdataAuthService;

    @Autowired
    private AuthdataTempAuthService authdataTempAuthService;

    @Autowired
    private ContactService contactService;

    @Autowired
    private AreaService areaService;

    @Autowired
    private AuthdataLinkfaceLivenessIdnumberVerificationService authdataLinkfaceLivenessIdnumberVerificationService;

    @Autowired
    private AuthdataLinkfaceLivenessSelfieVerificationService authdataLinkfaceLivenessSelfieVerificationService;

    @Autowired
    private MapProperties mapProperties;

    @Autowired
    private AttachPhotoService attachPhotoService;


    @RequestMapping("/index")
    public String index() {
        return "/backend/opencard/index";
    }


    /***
     * 获取详细页面
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value = "detail/{id}", method = RequestMethod.GET)
    //@RequiresPermissions("/admin/opencard/opencard:auth")
    public String detail(@PathVariable("id") Long id, Model model) {
        //Map<String,String> sourceMap = commonProperties.getSourceMap();
        //身份证信息
        CardMore card = cardService.findOneBySql(getOneSql(id));
        String cardstatus = "";
        if (card != null) {
            //用字典查询状态。（废用）
            //cardstatus = dictionaryService.findByTypeCodeAndCode("authStatus",opencard.getStatus().getValue().toString());
            cardstatus = card.getStatus().name();
        }
        //String cardstatus = appConfigService.findByTypeCodeAndCode("authStatus",opencard.getStatus().toString());
        IdCardData idcard = idcardService.findByUserId(card.getUserId());
        Long userId = idcard.getUserId();

        //银行卡号
        String bankNo = "";
        model.addAttribute("bankNo", bankNo);
        //年龄
        int year = new Date().getYear() - idcard.getBirthday().getYear();
        model.addAttribute("year", year);
        //生日
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        PersonData person = personService.findByUserId(userId);

        //授权项
        String authstr = "";
        List<AuthData> authdataAuths = authdataAuthService.findByCardId(id);
        if (authdataAuths.size() == 0) {
            authstr += "无,";
        }
        for (AuthData auth :
                authdataAuths) {
            String source = auth.getSource().getValue();
            if (source != null) {
                authstr += (mapProperties.getSourceMap().get(source) + ",");
            }

        }
        String auths = authstr.equals("") ? "" : authstr.substring(0, authstr.length() - 1);

        //婚姻状况最高学历生育状态
        String marital = "";
        String educationLevel = "";
        String fertilityStatus = "";
        String residentialType = "";
        if (person != null) {
            marital = appConfigService.findByTypeCodeAndCode("maritalStatus", person.getMaritalStatus());
            educationLevel = appConfigService.findByTypeCodeAndCode("educationLevel", person.getEducationLevel());
            fertilityStatus = appConfigService.findByTypeCodeAndCode("fertilityStatus", person.getFertilityStatus());
            residentialType = appConfigService.findByTypeCodeAndCode("residentialType", person.getResidentialType());
        }
        //用户基本信息
        User user = userInService.findOne(userId);
        //单位信息
        WorkData work = workService.findByUserId(userId);
        //工作类型,行业类型
        String jobsType = "";
        String tradeType = "";
        if (work != null) {
            jobsType = appConfigService.findByTypeCodeAndCode("jobsType", work.getJobsType());
            tradeType = appConfigService.findByTypeCodeAndCode("tradeType", work.getBelongIndustry());
        }

        //相片地址
        List<PhotoAttach> photos = attachPhotoService.findByUserId(userId);
        for (PhotoAttach attachPhoto : photos) {
            model.addAttribute("photo" + attachPhoto.getType(), attachPhoto.getPath());
        }
        //脱敏处理
        if (idcard != null) {
            String hyIdcard = Desensitization.idCard(idcard.getIdCard());
            model.addAttribute("hyIdcard", hyIdcard);
        }
        if (user != null) {
            String hyPhone = Desensitization.phone(user.getPhone());
            model.addAttribute("hyPhone", hyPhone);
        }

        //商汤
        LinkfaceLivenessIdNumberVerifcation livenessIdnumberVerification = authdataLinkfaceLivenessIdnumberVerificationService.findByUserId(userId);
        LinkfaceLivenessSelfieVerification livenessSelfieVerification = authdataLinkfaceLivenessSelfieVerificationService.findByCardId(id);
        if (livenessIdnumberVerification == null) {
            model.addAttribute("gongan", "未对比");
        } else {
            model.addAttribute("gongan", livenessIdnumberVerification.getVerifyScore() * 100);
        }
        if (livenessSelfieVerification == null) {

            model.addAttribute("huoti", "未对比");
        } else {

            model.addAttribute("huoti", livenessSelfieVerification.getVerifyScore() * 100);
        }

        //联系人
        ContactData contact = contactService.findByUserId(userId);
        //联系人关系
        String relativeRelations = "";
        String normalRelations = "";
        if (contact != null) {
            relativeRelations = appConfigService.findByTypeCodeAndCode("relativeRelations", contact.getRelativeRelationship());
            normalRelations = appConfigService.findByTypeCodeAndCode("normalRelations", contact.getOtherRelationship());
        }

        //下单地址
        LocationData area = areaService.findByCardIdAndAuth(id);

        //身份证有效时间
        String idcardTm = "";
        if (idcard != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-HH-dd");
            if (idcard.getEndTime() != null && idcard.getStartTime() != null) {
                idcardTm = dateFormat.format(idcard.getStartTime()) + "至" + dateFormat.format(idcard.getEndTime());
            }
        }

        //订单创建时间
        Date createdTime = card.getCreatedTime();
        String cardStartTm = formatDate.format(createdTime);


        //提交审批时间
        String submitTime = " ";
        if (card.getSubmitTime() != null) {

            Date submitData = card.getSubmitTime();
            submitTime = formatDate.format(submitData);
        }

        model.addAttribute("cardStartTm", cardStartTm);
        model.addAttribute("submitTime", submitTime);
        model.addAttribute("idcardTm", idcardTm);
        model.addAttribute("area", area);
        model.addAttribute("relativeRelations", relativeRelations);
        model.addAttribute("normalRelations", normalRelations);
        model.addAttribute("contact", contact);
        model.addAttribute("tradeType", tradeType);
        model.addAttribute("jobsType", jobsType);
        model.addAttribute("auths", auths);
        model.addAttribute("cardStatus", cardstatus);
        model.addAttribute("opencard", card);
        model.addAttribute("birthday", format.format(idcard.getBirthday()));
        model.addAttribute("person", person);
        model.addAttribute("residentialType", residentialType);
        model.addAttribute("marital", marital);
        model.addAttribute("educationLevel", educationLevel);
        model.addAttribute("fertilityStatus", fertilityStatus);
        model.addAttribute("idcard", idcard);
        model.addAttribute("user", user);
        model.addAttribute("work", work);
        return "/admin/opencard/opencard/detail";
    }


    @RequiresPermissions("/backend/opencard:list")
    @RequestMapping(value = "list")
    @ResponseBody
    public Object listPage(ServletRequest request) {
        //根据request获取page
        int pageNo = PageBulider.getPageNo(request);
        int pageSize = PageBulider.getPageSize(request);
        Page<CardMore> cardMorePage = list(getSqls(request), pageNo, pageSize, getCountSqls(request), SelectMultiTable.COUNT_ALIAS);
        List<CardMore> cardMoreList = cardMorePage.getContent();
        List<JSONObject> resultList = new ArrayList();

        //遍历page中内容，修改或添加非数据库的自定义字段
        for (CardMore cardMore : cardMoreList) {
            if (cardMore.getEnable()) {
                JSONObject json = (JSONObject) JSON.toJSON(cardMore);
                List<String> authNameList = new ArrayList();
                List<AuthData> authDataList = authdataAuthService.findByCardId(cardMore.getId());
                if (!CollectionUtils.isEmpty(authDataList)) {
                    for (AuthData auth : authDataList) {
                        authNameList.add(buildAuthValue(auth));
                    }
                } else {
                    List<AuthdataTempAuth> authdataTempAuthList = authdataTempAuthService.findByCardId(cardMore.getId());
                    if (!CollectionUtils.isEmpty(authDataList)) {
                        for (AuthdataTempAuth authdataTempAuth : authdataTempAuthList) {
                            authNameList.add(buildAuthValue(authdataTempAuth));
                        }
                    }
                }
                if (!CollectionUtils.isEmpty(authNameList)) {
                    json.put("auth", Joiner.on(",").join(authNameList));
                } else {
                    json.put("auth", "无");
                }
                resultList.add(json);
            }
        }
        return switchContent(cardMorePage, resultList);
    }


    private String buildAuthValue(BaseAuthData authData) {
        StringBuffer sbValue = new StringBuffer();
        if (!Objects.isNull(authData.getCode())) {
            sbValue.append(authData.getCode().getValue());
        }
        if (!Objects.isNull(authData.getSource())) {
            sbValue.append("-").append(authData.getSource().getValue());
        }
        String value = mapProperties.getAuthValue(sbValue.toString());
        if (StringUtils.isNotBlank(value)) {
            return value;
        }
        return sbValue.toString();
    }

    /**
     * 所有RequestMapping方法调用前的Model准备方法, 实现Struts2 Preparable二次部分绑定的效果,先根据form的id从数据库查出User对象,再把Form提交的内容绑定到该对象上。
     * 因为仅update()方法的form中有id属性，因此仅在update时实际执行.
     */
//    @ModelAttribute
//    public void getcard(@RequestParam(value = "id", defaultValue = "-1") Long id, Model model) {
////        if (id != -1) {
////            model.addAttribute("opencard", this.cardService.findOneBySql(getOneSql(id)));
////        }
////    }

    @Override
    protected DynamitSupportService<CardMore> getService() {
        return this.cardService;
    }

    private SelectMultiTable getSelectMultiTable() {
        return SelectMultiTable.builder(CardMore.class)
                .leftJoin(User.class, "user_in", new OnCondition[]{
                        new OnCondition(SelectMultiTable.ConnectType.AND, "userId", Operator.EQ, "id"),
                }).leftJoin(IdCardData.class, "id_card", new OnCondition[]{
                        new OnCondition(SelectMultiTable.ConnectType.AND, "userId", Operator.EQ, "userId")
                }).leftJoin(Product.class, "product", new OnCondition[]{
                        new OnCondition(SelectMultiTable.ConnectType.AND, "productId", Operator.EQ, "id")
                });
    }

    private String getSqls(ServletRequest request) {
        StringBuffer sbSql = new StringBuffer();
        sbSql.append(getSelectMultiTable().buildSqlString());
        sbSql.append(getService().buildSqlWhereCondition(bulidSearchParamsList(request)));
        sbSql.append(" GROUP BY " + SelectMultiTable.MAIN_ALAIS + ".id");
        return sbSql.toString();
    }


    private String getCountSqls(ServletRequest request) {
        StringBuffer sbSql = new StringBuffer();
        sbSql.append(getSelectMultiTable().buildCountSqlString());
        sbSql.append(getService().buildSqlWhereCondition(bulidSearchParamsList(request)));
        return sbSql.toString();
    }


    private String getOneSql(Object id) {
        String sql = SelectMultiTable.builder(CardMore.class)
                .leftJoin(User.class, "user_in", new OnCondition[]{
                        new OnCondition(SelectMultiTable.ConnectType.AND, "userId", Operator.EQ, "id"),
                }).leftJoin(IdCardData.class, "id_card", new OnCondition[]{
                        new OnCondition(SelectMultiTable.ConnectType.AND, "userId", Operator.EQ, "userId")
                }).leftJoin(Product.class, "product", new OnCondition[]{
                        new OnCondition(SelectMultiTable.ConnectType.AND, "productId", Operator.EQ, "id")
                }).buildSqlString() + " where maintable.id = '" + id + "' ";
        return sql;
    }


    @InitBinder
    public void initDate(WebDataBinder webDataBinder) {
        webDataBinder.addCustomFormatter(new DateFormatter("yyyy-MM-dd HH:mm:ss"));
        webDataBinder.addCustomFormatter(new DateFormatter("yyyy-MM-dd"));
    }
}
