package com.feitai.admin.backend.fund.vo;

import javax.validation.constraints.NotNull;

public class FundDetailRequest {
    @NotNull
    private Long fundId;
    private String type = "";
    private Integer size = 10;
    private Integer page = 0;

    public Long getFundId() {
        return fundId;
    }

    public void setFundId(Long fundId) {
        this.fundId = fundId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }
}

