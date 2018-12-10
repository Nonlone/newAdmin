package com.feitai.admin.backend.product.vo;

import javax.validation.Valid;
import java.util.List;

/**
 * 期数计划请求结构体
 */
public class FeePlan {

    /**
     * 期数
     */
    private Integer term;

    /**
     * 科目
     */
    @Valid
    private List<FeePlanDetail> subject;

    public Integer getTerm() {
        return term;
    }

    public void setTerm(Integer term) {
        this.term = term;
    }

    public List<FeePlanDetail> getSubject() {
        return subject;
    }

    public void setSubject(List<FeePlanDetail> subject) {
        this.subject = subject;
    }
}
