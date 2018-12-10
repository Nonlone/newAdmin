package com.feitai.admin.backend.loan.service;

import com.feitai.admin.backend.loan.mapper.RepayPlanVoMapper;
import com.feitai.admin.backend.loan.vo.OrderPlande;
import com.feitai.admin.backend.loan.vo.RepayPlanVo;
import com.feitai.admin.core.service.ClassPrefixDynamicSupportService;
import com.feitai.jieya.server.dao.loan.model.RepayPlan;
import com.feitai.jieya.server.dao.loan.model.RepayPlanComponent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class RepayPlanComponentService extends ClassPrefixDynamicSupportService<RepayPlanComponent> {

    @Autowired
    private RepayPlanVoMapper repayPlanMapper;

    public List<RepayPlanVo> findRepayPlanVoByLoanOrder(Long loanOrderId){
        return repayPlanMapper.findRepayPlanDetailByLoanOrderVo(loanOrderId);
    }


    public List<OrderPlande> findOrderPlandesByRepayPlans(List<RepayPlan> byLoanOrderId) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<OrderPlande> orderPlandes = new ArrayList<OrderPlande>();
        for (RepayPlan repayPlan :
                byLoanOrderId) {
            OrderPlande orderPlande = new OrderPlande();
            orderPlande.setTerm(repayPlan.getTerm());
            orderPlande.setDueDate(format.format(repayPlan.getDueDate()));
            orderPlande.setRealDate("");
            orderPlande.setAmount(repayPlan.getAmount().doubleValue());
            Example example = Example.builder(RepayPlanComponent.class).andWhere(Sqls.custom().andEqualTo("repayPlanId", repayPlan.getId())).build();
            List<RepayPlanComponent> byRepayPlanId = getMapper().selectByExample(example);
            Double overdueFine = 0d;
            Double overdueFineB = 0d;
            for (RepayPlanComponent rep :
                    byRepayPlanId) {
                switch (rep.getComponentType()) {
                    case 1:
                        orderPlande.setPincipalAmount(rep.getAmount().doubleValue());
                        orderPlande.setPincipalBalance(rep.getBalanceAmount().doubleValue());
                        break;
                    case 2:
                        orderPlande.setInterestAmount(rep.getAmount().doubleValue());
                        orderPlande.setInterestBalance(rep.getBalanceAmount().doubleValue());
                        break;
                    case 3:
                        overdueFine += rep.getAmount().doubleValue();
                        overdueFineB += rep.getBalanceAmount().doubleValue();
                        break;
                    case 4:
                        overdueFine += rep.getAmount().doubleValue();
                        overdueFineB += rep.getBalanceAmount().doubleValue();
                        break;
                    case 10:
                        orderPlande.setApproveFeeAmount(rep.getAmount().doubleValue());
                        orderPlande.setApproveFeeBalance(rep.getBalanceAmount().doubleValue());
                        break;
                    case 11:
                        orderPlande.setGuaranteeFeeAmount(rep.getAmount().doubleValue());
                        orderPlande.setGuaranteeFeeBalance(rep.getBalanceAmount().doubleValue());
                        break;

                }

            }
            orderPlande.setOverdueFineAmount(overdueFine);
            orderPlande.setOverdueFineBalance(overdueFineB);
            orderPlandes.add(orderPlande);
        }
        return orderPlandes;
    }

}
