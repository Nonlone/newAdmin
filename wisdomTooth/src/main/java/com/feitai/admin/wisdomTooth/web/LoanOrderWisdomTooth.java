package com.feitai.admin.wisdomTooth.web;

import com.feitai.admin.backend.customer.service.UserService;
import com.feitai.admin.backend.properties.AppProperties;
import com.feitai.admin.wisdomTooth.service.SupportStaffService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;

/**
 * detail:
 * author:
 * date:2018/11/6
 */
@Controller
@RequestMapping(value = "/wisdomTooth/loanOrder")
@Slf4j
public class LoanOrderWisdomTooth {

    @Autowired
    private UserService userInService;

    @Autowired
    private SupportStaffService supportStaffService;

    @Autowired
    private AppProperties appProperties;

    /***
     * 提现订单智齿客服入口
     * @param
     * @return
     */
    @RequestMapping(value= "wisdomTooth", method = RequestMethod.GET)
    public String wisdomTooth(Model model, ServletRequest request){
        String phone = request.getParameter("uphone")==null?request.getParameter("tel"):request.getParameter("uphone");
        String email = request.getParameter("email");
        String sign = request.getParameter("sign");
        if(!supportStaffService.checkHaveSupport(email,sign)){//查找是否有此客服人员
            return "/noSupport";
        }
        try{
            Long id = userInService.findUserIdByPhone(phone);
            String ipAndPort = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()+"/";
            model.addAttribute("IP",ipAndPort);
            model.addAttribute("isOut",true);
            model.addAttribute("userId",id);
            String rejectCash = appProperties.getRejectCash();
            model.addAttribute("rejectCash",rejectCash);
            return "/wisdomTooth/loanOrder/index";
        }catch (Exception e){
            log.error("findUserError:",e);
            return "/noUser";
        }

    }
}
