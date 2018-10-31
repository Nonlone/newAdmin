package com.feitai.admin.backend.product.vo;


import javax.validation.constraints.NotNull;

/**
 * 权重结构体
 */
public class Weight {

    private Long id;

    @NotNull
    private Short term;

    @NotNull
    private Byte weight;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Short getTerm() {
        return term;
    }

    public void setTerm(Short term) {
        this.term = term;
    }

    public Byte getWeight() {
        return weight;
    }

    public void setWeight(Byte weight) {
        this.weight = weight;
    }
}
