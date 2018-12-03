package com.feitai.admin.backend.loan.mapper;

import com.feitai.admin.backend.loan.vo.RepayPlanVo;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.base.BaseSelectMapper;

import java.util.List;

public interface RepayPlanVoMapper extends BaseSelectMapper<RepayPlanVo> {


    @Select(
            "SELECT" +
                    " trl.*," +
                    " pincipal.amount pincipal_amount," +
                    " pincipal.balance_amount pincipal_balance_amount," +
                    " interest.amount interest_amount," +
                    " interest.balance_amount interest_balance_amount," +
                    " overdueFine.amount overdueFine_amount," +
                    " overdueFine.balance_amount overdueFine_balance_amount," +
                    " overdueFineCompound.amount overdueFineCompound_amount," +
                    " overdueFineCompound.balance_amount overdueFineCompound_balance_amount," +
                    " innerInte.amount innerInte_amount," +
                    " innerInte.balance_amount innerInte_balance_amount," +
                    " occupyFee.amount occupyFee_amount," +
                    " occupyFee.balance_amount occupyFee_balance_amount," +
                    " poundageAmt.amount poundageAmt_amount," +
                    " poundageAmt.balance_amount poundageAmt_balance_amount," +
                    " approveFee.amount approveFee_amount," +
                    " approveFee.balance_amount approveFee_balance_amount," +
                    " guaranteeFee.amount guaranteeFee_amount," +
                    " guaranteeFee.balance_amount guaranteeFee_balance_amount," +
                    " brokerCommission.amount brokerCommission_amount," +
                    " brokerCommission.balance_amount brokerCommission_balance_amount " +
                    " FROM " +
                    " t_repay_plan trl" +
                    " LEFT JOIN (SELECT * FROM t_repay_plan_component WHERE component_type=1) pincipal ON trl.loan_order_id = pincipal.loan_order_id AND trl.term = pincipal.term" +
                    " LEFT JOIN (SELECT * FROM t_repay_plan_component WHERE component_type=2) interest ON trl.loan_order_id = interest.loan_order_id AND trl.term = interest.term" +
                    " LEFT JOIN (SELECT * FROM t_repay_plan_component WHERE component_type=3) overdueFine ON trl.loan_order_id = overdueFine.loan_order_id AND trl.term = overdueFine.term" +
                    " LEFT JOIN (SELECT * FROM t_repay_plan_component WHERE component_type=4) overdueFineCompound ON trl.loan_order_id = overdueFineCompound.loan_order_id AND trl.term = overdueFineCompound.term" +
                    " LEFT JOIN (SELECT * FROM t_repay_plan_component WHERE component_type=5) innerInte ON trl.loan_order_id = innerInte.loan_order_id AND trl.term = innerInte.term" +
                    " LEFT JOIN (SELECT * FROM t_repay_plan_component WHERE component_type=6) occupyFee ON trl.loan_order_id = occupyFee.loan_order_id AND trl.term = occupyFee.term" +
                    " LEFT JOIN (SELECT * FROM t_repay_plan_component WHERE component_type=7) poundageAmt ON trl.loan_order_id = poundageAmt.loan_order_id AND trl.term = poundageAmt.term" +
                    " LEFT JOIN (SELECT * FROM t_repay_plan_component WHERE component_type=10) approveFee ON trl.loan_order_id = approveFee.loan_order_id AND trl.term = approveFee.term" +
                    " LEFT JOIN (SELECT * FROM t_repay_plan_component WHERE component_type=11) guaranteeFee ON trl.loan_order_id = guaranteeFee.loan_order_id AND trl.term = guaranteeFee.term" +
                    " LEFT JOIN (SELECT * FROM t_repay_plan_component WHERE component_type=12) brokerCommission ON trl.loan_order_id = brokerCommission.loan_order_id AND trl.term = brokerCommission.term" +
                    " WHERE trl.loan_order_id=#{loanOrderId}"
    )
    List<RepayPlanVo> findRepayPlanDetailByLoanOrderVo(Long loanOrderId);

}
