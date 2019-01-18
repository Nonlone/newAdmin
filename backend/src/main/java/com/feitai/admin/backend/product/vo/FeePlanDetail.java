package com.feitai.admin.backend.product.vo;

import com.feitai.jieya.server.dao.base.constant.CalculationMode;
import com.feitai.jieya.server.dao.base.constant.FeeBaseType;
import com.feitai.jieya.server.dao.base.constant.PaymentTimeType;
import com.feitai.jieya.server.dao.base.constant.PaymentType;
import lombok.Data;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

/**
 * 费率详细
 */
@Data
public class FeePlanDetail {

    /**
     * 科目Id
     */
    private Byte subjectId;

    /**
     * 计息方式
     */
    @NotNull
    private PaymentType paymentType;

    /**
     * 收费类型
     */
    @NotNull
    private CalculationMode calculationMode;

    /**
     * 收费内容
     */
    @NotNull
    private BigDecimal fee;

    /**
     * 收费基数
     */
    private FeeBaseType feeBaseType;

    /**
     * 收取时点
     */
    @NotNull
    private PaymentTimeType paymentTimeType;

    /**
     * 收取时间点
     */
    private String paymentTime;

    public Byte getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Byte subjectId) {
        this.subjectId = subjectId;
    }

}
