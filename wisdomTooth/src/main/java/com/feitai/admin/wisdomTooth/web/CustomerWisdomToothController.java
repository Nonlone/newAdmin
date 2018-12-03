package com.feitai.admin.wisdomTooth.web;

import com.feitai.admin.backend.customer.entity.IdCardDataExtend;
import com.feitai.admin.backend.customer.service.IdCardService;
import com.feitai.admin.backend.customer.service.PersonService;
import com.feitai.admin.backend.customer.service.UserBankCardService;
import com.feitai.admin.backend.customer.service.UserService;
import com.feitai.admin.backend.fund.service.FundService;
import com.feitai.admin.backend.fund.service.UserBankCardBindService;
import com.feitai.admin.core.service.DynamitSupportService;
import com.feitai.admin.core.service.OnCondition;
import com.feitai.admin.core.service.Operator;
import com.feitai.admin.core.service.SelectMultiTable;
import com.feitai.admin.core.web.BaseListableController;
import com.feitai.admin.wisdomTooth.service.SupportStaffService;
import com.feitai.jieya.server.dao.bank.model.UserBankCard;
import com.feitai.jieya.server.dao.bank.model.UserBankCardBind;
import com.feitai.jieya.server.dao.data.model.IdCardData;
import com.feitai.jieya.server.dao.data.model.PersonData;
import com.feitai.jieya.server.dao.fund.model.Fund;
import com.feitai.jieya.server.dao.user.model.User;
import com.feitai.utils.Desensitization;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletRequest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * detail:
 * author:
 * date:2018/11/6
 */
@Controller
@RequestMapping(value = "/wisdomTooth/idCard")
@Slf4j
public class CustomerWisdomToothController extends BaseListableController<IdCardDataExtend> {

    @Autowired
    private IdCardService idcardService;

    @Autowired
    private PersonService personService;

    @Autowired
    private UserService userInService;

    @Autowired
    private SupportStaffService supportStaffService;

    @Autowired
    private UserBankCardService userBankCardService;

    @Autowired
    private UserBankCardBindService userBankCardBindService;

    @Autowired
    private FundService fundService;

    @RequestMapping(value = "wisdomTooth")
    public String wisdomTooth(Model model, ServletRequest request) {
        String phone = request.getParameter("uphone")==null?request.getParameter("tel"):request.getParameter("uphone");
        String email = request.getParameter("email");
        String sign = request.getParameter("sign");
        if(!supportStaffService.checkHaveSupport(email,sign)){//查找是否有此客服人员
            return "/noSupport";
        }
        try{
            Long id = userInService.findUserIdByPhone(phone);
            //身份证信息
            IdCardData idcard = idcardService.findOneBySql(getFindByUserIdSql(id));

            List<UserBankCard> cards = userBankCardService.findByUserId(id);
            //收款卡
            List<com.feitai.admin.wisdomTooth.vo.UserBankCard> payCardrea = new ArrayList<>();
            //还款卡
            List<com.feitai.admin.wisdomTooth.vo.UserBankCard> repayCardrea = new ArrayList<>();
            for (UserBankCard repay:
                    cards) {
                com.feitai.admin.wisdomTooth.vo.UserBankCard userBankCard = new com.feitai.admin.wisdomTooth.vo.UserBankCard();
                userBankCard.setBankCardNo(Desensitization.bankCardNo(repay.getBankCardNo()));
                userBankCard.setBankName(repay.getBankName());
                userBankCard.setPayStatus(repay.getPayAble());
                //资金方
                String funds = getFunds(repay.getBankCardNo());
                userBankCard.setFund(funds);

                if(repay.getPayAble().equals("1")){
                    payCardrea.add(userBankCard);
                }
                if(repay.getRepayAble().equals("1")){
                    repayCardrea.add(userBankCard);
                }
            }
            //年龄
            if(idcard!=null){
                int year = new Date().getYear() - idcard.getBirthday().getYear();
                model.addAttribute("year",year);
            }
            //生日
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            PersonData person = personService.findByUserId(id);


            //用户基本信息
            User user = userInService.findOne(id);

            //脱敏处理
            if(idcard!=null){
                model.addAttribute("hyIdcard",Desensitization.idCard(idcard.getIdCard()));
                //idCard.setIdCard(hyIdcard);
            }
            if(user!=null){
                model.addAttribute("hyPhone",Desensitization.phone(user.getPhone()));
                //user.setPhone(hyPhone);
            }


            model.addAttribute("payCard",payCardrea);
            model.addAttribute("repayCard",repayCardrea);
            model.addAttribute("birthday",format.format(idcard.getBirthday()));
            model.addAttribute("person", person);
            model.addAttribute("idCard",idcard);
            model.addAttribute("user", user);

            //model.addAttribute("userId", "2481652236288");
            return "/admin/appUser/idCard/out";
        }catch (Exception e){
            log.error("", e);
            return "/noUser";
        }

    }

    protected String getFindByUserIdSql(Object userId) {
        String sql = SelectMultiTable.builder(IdCardData.class)
                .leftJoin(User.class,"user_in",new OnCondition[]{
                        new OnCondition(SelectMultiTable.ConnectType.AND, "userId", Operator.EQ, "id"),
                }).buildSqlString()+" where maintable.user_id = '"+userId+"'"+" GROUP BY maintable.id";
        return sql;
    }

    /***
     * 根据银行卡号获取资金方及其状态
     * @param bankCardNo
     * @return
     */
    private String getFunds(String bankCardNo){
        List<UserBankCardBind> userBankCardBinds = userBankCardBindService.findByBankCardNo(bankCardNo);
        String funds = "";
        for (UserBankCardBind userBankCardBind:
                userBankCardBinds) {
            Fund fund = fundService.findOne(userBankCardBind.getSeqNo());
            String status = userBankCardBind.getStatus();
            switch (status){
                case "NONE":
                    funds+=fund.getFundName()+"（未绑卡）,";
                    break;
                case "AUDIT":
                    funds+=fund.getFundName()+"（审核中）,";
                    break;
                case "PASSED":
                    funds+=fund.getFundName()+"（审核通过）,";
                    break;
                case "BACK":
                    funds+=fund.getFundName()+"（审核回退）,";
                    break;
                case "REFUSED":
                    funds+=fund.getFundName()+"（审核拒绝）,";
                    break;
            }
        }
        if(funds.length()!=0){
            funds = funds.substring(0, funds.length() - 1);
        }
        return funds;
    }

    @Override
    protected DynamitSupportService<IdCardDataExtend> getService() {
        return this.idcardService;
    }
}
