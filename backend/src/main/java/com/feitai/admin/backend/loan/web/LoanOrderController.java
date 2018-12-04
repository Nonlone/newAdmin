/**
 * @author
 * @version 1.0
 * @desc 借款订单表
 * @since 2018-07-25 12:16:07
 */

package com.feitai.admin.backend.loan.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.feitai.admin.backend.config.service.AppConfigService;
import com.feitai.admin.backend.creditdata.service.AuthdataAuthService;
import com.feitai.admin.backend.creditdata.vo.PhotoAttachViewVo;
import com.feitai.admin.backend.customer.service.*;
import com.feitai.admin.backend.fund.service.FundService;
import com.feitai.admin.backend.loan.entity.LoanOrderMore;
import com.feitai.admin.backend.loan.entity.RepayOrderMore;
import com.feitai.admin.backend.loan.service.LoanOrderService;
import com.feitai.admin.backend.loan.service.RepayOrderService;
import com.feitai.admin.backend.loan.service.RepayPlanComponentService;
import com.feitai.admin.backend.loan.service.RepayPlanService;
import com.feitai.admin.backend.loan.vo.BackendLoanRequest;
import com.feitai.admin.backend.loan.vo.OrderPlande;
import com.feitai.admin.backend.opencard.service.CardService;
import com.feitai.admin.backend.opencard.service.TongDunDataService;
import com.feitai.admin.backend.product.service.ProductService;
import com.feitai.admin.backend.product.service.ProductTermFeeFeatureService;
import com.feitai.admin.backend.properties.AppProperties;
import com.feitai.admin.backend.properties.MapProperties;
import com.feitai.admin.backend.vo.BackendResponse;
import com.feitai.admin.core.annotation.LogAnnotation;
import com.feitai.admin.core.service.*;
import com.feitai.admin.core.vo.ListItem;
import com.feitai.admin.core.web.BaseListableController;
import com.feitai.admin.core.web.PageBulider;
import com.feitai.jieya.server.dao.attach.model.PhotoAttach;
import com.feitai.jieya.server.dao.authdata.model.AuthData;
import com.feitai.jieya.server.dao.bank.model.BankSupport;
import com.feitai.jieya.server.dao.bank.model.UserBankCard;
import com.feitai.jieya.server.dao.card.model.Card;
import com.feitai.jieya.server.dao.contract.model.ContractFaddDetail;
import com.feitai.jieya.server.dao.data.model.IdCardData;
import com.feitai.jieya.server.dao.data.model.LocationData;
import com.feitai.jieya.server.dao.data.model.TongDunData;
import com.feitai.jieya.server.dao.fund.model.Fund;
import com.feitai.jieya.server.dao.loan.model.RepayPlan;
import com.feitai.jieya.server.dao.product.model.Product;
import com.feitai.jieya.server.dao.product.model.ProductTermFeeFeature;
import com.feitai.jieya.server.dao.user.model.User;
import com.feitai.utils.CollectionUtils;
import com.feitai.utils.Desensitization;
import com.feitai.utils.ObjectUtils;
import com.feitai.utils.datetime.DateTimeStyle;
import com.feitai.utils.datetime.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletRequest;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.*;


@Controller
@RequestMapping(value = "/backend/loanOrder")
@Slf4j
public class LoanOrderController extends BaseListableController<LoanOrderMore> {

    @Autowired
    private LoanOrderService loanOrderService;

    @Autowired
    private ProductTermFeeFeatureService productTermFeeFeatureService;

    @Autowired
    private IdCardService idcardService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CardService cardService;

    @Autowired
    private BankSupportService bankSupportService;

    @Autowired
    private RepayPlanService repayPlanService;

    @Autowired
    private RepayPlanComponentService repayPlanComponentService;

    @Autowired
    private AppConfigService appConfigService;

    @Autowired
    private MapProperties mapProperties;

    @Autowired
    private AppProperties appProperties;

    @Autowired
    private FundService fundService;

    @Autowired
    private RepayOrderService repayOrderService;

    @Autowired
    private TongDunDataService tongDunDataService;

    @Autowired
    private AuthDataService authDataService;

    @Autowired
    private AreaService areaService;

    @Autowired
    private AuthdataAuthService authdataAuthService;

    @Autowired
    private PhotoService photoService;

    private RestTemplate restTemplate = new RestTemplate();

    private final static String LOAN_PURPOSE = "loanPurpose";

    private final static String AUTN_XINWANG_CODE = "xinwang_supplement_infor";

    private final static String AUTH_TOBACCO_CODE = "tobacco_supplement_infor";

    /***
     * 内审通过
     * @param loanId
     * @return
     */
    @PostMapping(value = "/dataApprovePass/{loanId}")
    @RequiresPermissions("/backend/loanOrder:auth")
    @ResponseBody
    public Object dataApprovePass(@PathVariable("loanId") String loanId) {
        LoanOrderMore loanOrderMore = loanOrderService.findOne(loanId);
        if (loanOrderMore.getStatus() != 3) {
            return new BackendResponse(-1, "订单状态已更新，不能进行内审！");
        }
        BackendLoanRequest backendLoanRequest = new BackendLoanRequest();
        backendLoanRequest.setLoanOrderId(Long.parseLong(loanId));
        String requestJsonString = JSON.toJSONString(backendLoanRequest);
        try {
            restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
            ResponseEntity<String> jsonString = restTemplate.postForEntity(appProperties.getDataApprovePassUrl(), requestJsonString, String.class);
            JSONObject jsonObject = JSON.parseObject(jsonString.getBody());
            return new BackendResponse((int) jsonObject.get("code"), (String) jsonObject.get("message"));
        } catch (Exception e) {
            return new BackendResponse(-1, "连接服务器失败！");
        }

    }


    /***
     * 取消放款
     * @param dataApprovePassRequest
     * @return
     */
    @PostMapping("/rejectCash")
    @RequiresPermissions("/backend/loanOrder:auth")
    @ResponseBody
    public Object rejectCash(@Valid BackendLoanRequest dataApprovePassRequest) {
        String requestJsonString = JSON.toJSONString(dataApprovePassRequest);
        restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        ResponseEntity<String> jsonString = restTemplate.postForEntity(appProperties.getRejectCash(), requestJsonString, String.class);
        return jsonString.getBody();
    }



    @GetMapping(value = "/index")
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("/backend/loanOrder/index");
        String rejectCash = appProperties.getRejectCash();
        modelAndView.addObject("rejectCash", rejectCash);
        Map<String, String> loanStatusMap = mapProperties.getLoanStatusMap();
        modelAndView.addObject("loanStatusMap", JSON.toJSONString(loanStatusMap));
        return modelAndView;
    }


    @RequestMapping(value = "getLoanStatusList")
    @ResponseBody
    @LogAnnotation(value = true, writeRespBody = false)
    public Object getLoanStatusList() {
        Map<String, String> loanStatusMap = mapProperties.getLoanStatusMap();
        List<ListItem> list = new ArrayList<ListItem>();
        list.add(new ListItem("全部", " "));
        for (String key : loanStatusMap.keySet()) {
            list.add(new ListItem(loanStatusMap.get(key), key));
        }
        return list;
    }


    @RequiresPermissions("/backend/loanOrder:list")
    @PostMapping("/list")
    @ResponseBody
    public Object listPage(ServletRequest request) {
        //根据request获取page
        int pageNo = PageBulider.getPageNo(request);
        int pageSize = PageBulider.getPageSize(request);
        String sql = getSql(request, getSelectMultiTable()) + " ORDER BY " + SelectMultiTable.MAIN_ALAIS + ".created_time DESC";
        Page<LoanOrderMore> loanOrderMorePage = list(sql, pageNo, pageSize, getCountSqls(request), SelectMultiTable.COUNT_ALIAS);
        List<LoanOrderMore> content = loanOrderMorePage.getContent();
        List<JSONObject> resultList = new ArrayList<>();

        //遍历page中内容，修改或添加非数据库的自定义字段
        for (LoanOrderMore loanOrderMore :
                content) {
            JSONObject json = (JSONObject) JSON.toJSON(loanOrderMore);
            try {
                JSONObject jsonObject = handleSingleData(json);
                resultList.add(jsonObject);
            } catch (Exception e) {
                log.error("this json handle fail:[{}]! message:{}", json, e.getMessage());
                continue;
            }
        }
        return switchContent(loanOrderMorePage, resultList);
    }


    /***
     * 获取详细页面
     * @param id
     * @return
     */
    @RequiresPermissions("/backend/loanOrder:list")
    @RequestMapping(value = "detail/{id}", method = RequestMethod.GET)
    @RequiresPermissions("/backend/loanOrder:list")
    public ModelAndView detail(@PathVariable("id") String id) {
        ModelAndView modelAndView = new ModelAndView("/backend/loanOrder/detail");
        LoanOrderMore loanOrder = loanOrderService.findOneBySql(getOneSql(id));

        Long userId = loanOrder.getUserId();

        User user = loanOrder.getUser();
        IdCardData idcard = loanOrder.getIdcard();
        Product product = loanOrder.getProduct();


        //资金方
        Fund fund = loanOrder.getFund();
        if (fund != null) {
            modelAndView.addObject("fundName", fund.getFundName());
        }

        modelAndView.addObject("loanId", id);
        modelAndView.addObject("loanOrder", loanOrder);
        modelAndView.addObject("user", user);
        modelAndView.addObject("idCard", idcard);
        modelAndView.addObject("product", product);
        modelAndView.addObject("status", mapProperties.getloanStatus(loanOrder.getStatus()));
        if (loanOrder.getStatus() == 3) {
            modelAndView.addObject("dataApprovePass", true);
        } else {
            modelAndView.addObject("dataApprovePass", false);
        }

        //借款用途
        modelAndView.addObject("loanPurpose", appConfigService.findByTypeCodeAndCode(LOAN_PURPOSE, loanOrder.getLoanPurposeCode()));

        //脱敏处理
        modelAndView.addObject("hyPhone", Desensitization.phone(user.getPhone()));
        modelAndView.addObject("hyPhone", user.getPhone());
        modelAndView.addObject("hyIdcard", Desensitization.idCard(idcard.getIdCard()));



        //获取产品详细信息，需要产品id和期数
        List<ProductTermFeeFeature> byProductIdAndTerm = productTermFeeFeatureService.findByProductIdAndTerm(loanOrder.getProductId(), loanOrder.getLoanTerm().shortValue());
        if (byProductIdAndTerm.size() != 0) {
            modelAndView.addObject("productIdAndTerm", byProductIdAndTerm.get(0));
        }
        int year = new Date().getYear() - idcard.getBirthday().getYear();
        modelAndView.addObject("year", year);


        //授信
        Card card = cardService.findSingleById(loanOrder.getCardId());
        if (card != null) {
            BigDecimal creditSum = card.getCreditSum();
            modelAndView.addObject("shouxin", creditSum);
        }

        if (loanOrder.getBankCode() != null) {
            BankSupport bankSuppor = bankSupportService.findByBankCode(loanOrder.getBankCode());
            modelAndView.addObject("bank", bankSuppor.getBankName());
        }

        List<RepayPlan> byLoanOrderId = repayPlanService.findByLoanOrderId(Long.parseLong(id));

        //还款计划
        List<OrderPlande> orderPlandes = repayPlanComponentService.findOrderPlandesByRepayPlans(byLoanOrderId);
        if (orderPlandes.size() == 0) {
            modelAndView.addObject("havePlan", false);
        } else {
            modelAndView.addObject("havePlan", true);
        }
        modelAndView.addObject("repayPlan", orderPlandes);

        //日期类
        if (loanOrder.getPayLoanTime() != null) {
            modelAndView.addObject("payLoanTime", DateUtils.format(loanOrder.getPayLoanTime(), DateTimeStyle.DEFAULT_YYYY_MM_DD_HH_MM_SS));
        }
        if (loanOrder.getApplyTime() != null) {
            modelAndView.addObject("applyTime", DateUtils.format(loanOrder.getApplyTime(), DateTimeStyle.DEFAULT_YYYY_MM_DD_HH_MM_SS));
        }
        if (loanOrder.getBankApproveTime() != null) {
            modelAndView.addObject("bankApproveTime", DateUtils.format(loanOrder.getBankApproveTime(), DateTimeStyle.DEFAULT_YYYY_MM_DD_HH_MM_SS));
        }
        if (loanOrder.getApproveTime() != null) {
            modelAndView.addObject("approveTime", DateUtils.format(loanOrder.getApproveTime(), DateTimeStyle.DEFAULT_YYYY_MM_DD_HH_MM_SS));
        }
        if (loanOrder.getLoanTime() != null) {
            modelAndView.addObject("loanTime", DateUtils.format(loanOrder.getLoanTime(), DateTimeStyle.DEFAULT_YYYY_MM_DD_HH_MM_SS));
        }
        modelAndView.addObject("createdTime", DateUtils.format(loanOrder.getCreatedTime(), DateTimeStyle.DEFAULT_YYYY_MM_DD_HH_MM_SS));

        //还款银行卡
        List<UserBankCard> byUserIdAndRepay = repayOrderService.findByUserIdAndRepay(userId);
        StringBuffer bankCardDetail = getBankCardDetail(byUserIdAndRepay);
        String repayCardString = "";
        if (bankCardDetail.length() > 0) {
            repayCardString = bankCardDetail.substring(0, bankCardDetail.length() - 1);
        }
        modelAndView.addObject("repayCard", repayCardString);

        //收款银行卡
        List<UserBankCard> byUserIdAndPay = repayOrderService.findByUserIdAndPay(userId);
        StringBuffer payCardBuffer = getBankCardDetail(byUserIdAndPay);
        String payCardString = "";
        if (payCardBuffer.length() > 0) {
            payCardString = payCardBuffer.substring(0, payCardBuffer.length() - 1);
        }
        modelAndView.addObject("payCard", payCardString);

        //地区信息
        LocationData authArea = areaService.findByCardIdInLoan(card.getId());
        if (authArea != null) {
            JSONObject jsonObject = (JSONObject) JSON.toJSON(authArea);
            jsonObject.put("segmentName", mapProperties.getSegment(authArea.getSegment()));
            modelAndView.addObject("areaList", Collections.singletonList(jsonObject));
        }

        //发大大合同
        List<ContractFaddDetail> faddDetails = loanOrderService.getFaddByLoanOrder(loanOrder.getId());
        modelAndView.addObject("faddDetails", faddDetails);

        // 获取同盾数据
        TongDunData tongDunData = tongDunDataService.findByUserIdAndCardIdInLoan(userId, card.getId());
        if (tongDunData != null) {
            modelAndView.addObject("tongDunData", tongDunData);
        }

        //是否有新网征信数据
        List<AuthData> xinwangAuthDataList = authDataService.findByCardIdAndCode(card.getId(), AUTN_XINWANG_CODE);
        if (CollectionUtils.isNotEmpty(xinwangAuthDataList)) {
            modelAndView.addObject("xinwangAuth", true);
        } else {
            modelAndView.addObject("xinwangAuth", false);
        }


        //是否有烟草补充资料
        List<AuthData> tobaccoAuthDataList = authDataService.findByCardIdAndCode(card.getId(), AUTH_TOBACCO_CODE);
        if (CollectionUtils.isNotEmpty(tobaccoAuthDataList)) {
            modelAndView.addObject("tobaccoAuth", true);
        } else {
            modelAndView.addObject("tobaccoAuth", false);
        }

        //图片
        List<PhotoAttach> loanVoucherPhotoList = photoService.findLoanVoucherPhotoByUserId(userId);
        List<PhotoAttachViewVo> commonPhoto = new ArrayList<>();
        for (PhotoAttach photoAttach : loanVoucherPhotoList) {
            PhotoAttachViewVo photoAttachViewVo = new PhotoAttachViewVo();
            BeanUtils.copyProperties(photoAttach, photoAttachViewVo);
            photoAttachViewVo.setName(mapProperties.getPhotoType(photoAttach.getType()));
            photoAttachViewVo.setTypeName(photoAttach.getType().toString().toUpperCase());
            commonPhoto.add(photoAttachViewVo);
        }
        modelAndView.addObject("loanVoucherPhoto", commonPhoto);

        return modelAndView;
    }



    private StringBuffer getBankCardDetail(List<UserBankCard> byUserIdAndPay) {
        StringBuffer cardBuffer = new StringBuffer();
        for (UserBankCard userBankCard : byUserIdAndPay) {
            String hyCard = Desensitization.bankCardNo(userBankCard.getBankCardNo());
            String bankNameByCode = repayOrderService.findBankNameByCode(userBankCard.getBankCode());
            String bankCardType = mapProperties.getBankCardType(userBankCard.getBankCardType());
            cardBuffer.append(hyCard + "（" + bankNameByCode + bankCardType + "）</br>");
        }
        return cardBuffer;
    }

    /**
     * 所有RequestMapping方法调用前的Model准备方法, 实现Struts2 Preparable二次部分绑定的效果,先根据form的id从数据库查出User对象,再把Form提交的内容绑定到该对象上。
     * 因为仅update()方法的form中有id属性，因此仅在update时实际执行.
     */
    @ModelAttribute
    public void getloanOrder(@RequestParam(value = "id", defaultValue = "-1") String id, Model model) {
        if (!id.equals("-1")) {
            model.addAttribute("loanOrder", this.loanOrderService.findOneBySql(getOneSql(id)));
        }
    }


    /***
     * 处理单条数据
     * @return
     */
    private JSONObject handleSingleData(JSONObject json) {
        List<ProductTermFeeFeature> search = new ArrayList<ProductTermFeeFeature>();
        Long productId = (Long) json.get("productId");
        Integer loanTerm = (Integer) json.get("loanTerm");
        Long cardId = (Long) json.get("cardId");

        search = productTermFeeFeatureService.findByProductIdAndTerm(productId.longValue(), loanTerm.shortValue());

        if (search.size() > 0) {
            ProductTermFeeFeature productTermFeeFeature = search.get(0);
            try {
                Map<String, Object> stringObjectMap = ObjectUtils.objectToMap(productTermFeeFeature);
                json.put("productTermFeeFeature", stringObjectMap);
            } catch (Exception e) {
                log.error("productTermFeeFeature can't achieve", e);
            }
        }

        Long payFundId = (Long) json.get("payFundId");
        if (payFundId != null) {
            Fund fund = fundService.findOne(payFundId);
            if (fund != null)
                json.put("fundName", fund.getFundName());
        } else {
            json.put("fundName", "");
        }

        //授信
        Card card = cardService.findSingleById(cardId);
        if (card != null) {
            BigDecimal creditSum = card.getCreditSum();
            json.put("card.creditSum", creditSum);
            json.put("cardStatus", card.getStatus());
            json.put("card", card);
        }
        return json;
    }


    private String getCountSqls(ServletRequest request) {
        StringBuffer sbSql = new StringBuffer();
        String searchSql = getService().buildSqlWhereCondition(bulidSearchParamsList(request), SelectMultiTable.MAIN_ALAIS);
        if (searchSql.equals(DynamitSupportService.WHERE_COMMON)) {
            sbSql.append(SelectMultiTable.builder(LoanOrderMore.class).buildCountSqlString());
        } else {
            sbSql.append(getSelectMultiTable().buildCountSqlString());
        }
        sbSql.append(searchSql);
        return sbSql.toString();
    }

    private SelectMultiTable getSelectMultiTable() {
        return SelectMultiTable.builder(LoanOrderMore.class)
                .leftJoin(RepayOrderMore.class, "repay_order", new OnCondition[]{
                        new OnCondition(SelectMultiTable.ConnectType.AND, "id", Operator.EQ, "loanOrderId"),
                }).leftJoin(IdCardData.class, "idcard", new OnCondition[]{
                        new OnCondition(SelectMultiTable.ConnectType.AND, "userId", Operator.EQ, "userId")
                }).leftJoin(Product.class, "product", new OnCondition[]{
                        new OnCondition(SelectMultiTable.ConnectType.AND, "productId", Operator.EQ, "id")
                }).leftJoin(User.class, "user", new OnCondition[]{
                        new OnCondition(SelectMultiTable.ConnectType.AND, "userId", Operator.EQ, "id")
                }).leftJoin(Card.class, "opencard", new OnCondition[]{
                        new OnCondition(SelectMultiTable.ConnectType.AND, "userId", Operator.EQ, "userId")
                });
    }

    @Override
    protected DynamitSupportService<LoanOrderMore> getService() {
        return this.loanOrderService;
    }


    protected String getOneSql(String id) {
        String sql = SelectMultiTable.builder(LoanOrderMore.class)
                .leftJoin(RepayOrderMore.class, "repay_order", new OnCondition[]{
                        new OnCondition(SelectMultiTable.ConnectType.AND, "id", Operator.EQ, "loanOrderId"),
                }).leftJoin(IdCardData.class, "idcard", new OnCondition[]{
                        new OnCondition(SelectMultiTable.ConnectType.AND, "userId", Operator.EQ, "userId")
                }).leftJoin(Product.class, "product", new OnCondition[]{
                        new OnCondition(SelectMultiTable.ConnectType.AND, "productId", Operator.EQ, "id")
                }).leftJoin(User.class, "user", new OnCondition[]{
                        new OnCondition(SelectMultiTable.ConnectType.AND, "userId", Operator.EQ, "id")
                }).leftJoin(Card.class, "opencard", new OnCondition[]{
                        new OnCondition(SelectMultiTable.ConnectType.AND, "userId", Operator.EQ, "userId")
                }).buildSqlString() + "where maintable.id = " + id + " Group by id";
        return sql;
    }

}
