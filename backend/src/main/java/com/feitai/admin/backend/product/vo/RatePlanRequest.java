package com.feitai.admin.backend.product.vo;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 费率方案请求结构体
 */
public class RatePlanRequest  {

    private Long id;

    @NotBlank
    @Length(max = 20)
    private String name;

    @NotNull
    private Long fundId;

    @NotNull
    private Long productId;

    @NotNull
    private Boolean enable;

    /**
     * 权重结构
     */
    @Valid
    @NotNull
    private List<Weight> weight;

    /**
     * 科目明细
     */
    @Valid
    @NotNull
    private List<FeePlan> feePlan;

    @NotNull
    public List<Weight> getWeight() {
        return weight;
    }

    public void setWeight(@NotNull List<Weight> weight) {
        this.weight = weight;
    }

    @NotNull
    public List<FeePlan> getFeePlan() {
        return feePlan;
    }

    public void setFeePlan(@NotNull List<FeePlan> feePlan) {
        this.feePlan = feePlan;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NotNull
    public Long getFundId() {
        return fundId;
    }

    public void setFundId(@NotNull Long fundId) {
        this.fundId = fundId;
    }

    @NotNull
    public Long getProductId() {
        return productId;
    }

    public void setProductId(@NotNull Long productId) {
        this.productId = productId;
    }

    @NotNull
    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(@NotNull Boolean enable) {
        this.enable = enable;
    }
}
