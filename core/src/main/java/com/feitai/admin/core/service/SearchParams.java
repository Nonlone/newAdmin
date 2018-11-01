package com.feitai.admin.core.service;

import lombok.Data;
import tk.mybatis.mapper.weekend.Fn;
import tk.mybatis.mapper.weekend.reflection.Reflections;

/**
 * 搜索参数
 */
@Data
public class SearchParams {

    private String name;

    private Operator operator;

    private String[] values;


    public SearchParams() {
    }

    public SearchParams(String name, Operator operator, String... values) {
        this.name = name;
        this.operator = operator;
        this.values = values;
    }


    public <A, B> SearchParams(Fn<A, B> fn, Operator operator, String... values) {
        this.name = Reflections.fnToFieldName(fn);
        this.operator = operator;
        this.values = values;
    }


}
