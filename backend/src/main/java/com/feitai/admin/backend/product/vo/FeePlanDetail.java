package com.feitai.admin.backend.product.vo;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

/**
 * 费率详细
 */
public class FeePlanDetail {

    /**
     * 科目Id
     */
    private Byte subjectId;

    /**
     * 计息方式
     */
    @NotNull
    @Max(127)
    private Byte paymentType;

    /**
     * 收费类型
     */
    @NotNull
    @Max(127)
    private Byte calculationMode;

    /**
     * 收费内容
     */
    @NotNull
    private Double fee;

    /**
     * 收费基数
     */
    @NotNull
    @Max(127)
    private Byte feeBaseType;

    /**
     * 收取时点
     */
    @NotNull
    @Max(127)
    private Byte paymentTimeType;

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

    @NotNull
    public Byte getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(@NotNull Byte paymentType) {
        this.paymentType = paymentType;
    }

    @NotNull
    public Byte getCalculationMode() {
        return calculationMode;
    }

    public void setCalculationMode(@NotNull Byte calculationMode) {
        this.calculationMode = calculationMode;
    }

    @NotNull
    public Double getFee() {
        return fee;
    }

    public void setFee(@NotNull Double fee) {
        this.fee = fee;
    }

    @NotNull
    public Byte getFeeBaseType() {
        return feeBaseType;
    }

    public void setFeeBaseType(@NotNull Byte feeBaseType) {
        this.feeBaseType = feeBaseType;
    }

    @NotNull
    public Byte getPaymentTimeType() {
        return paymentTimeType;
    }

    public void setPaymentTimeType(@NotNull Byte paymentTimeType) {
        this.paymentTimeType = paymentTimeType;
    }

    public String getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(String paymentTime) {
        this.paymentTime = paymentTime;
    }
}
