package com.feitai.admin.backend.fund.vo;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Data
public class FundChargeRequest {
    @NotNull
    private String fundId;
    @NotNull
    private String amount;
    @Length(max = 255)
    private String remark;
    @Length(max = 10)
    private String operator;

}
