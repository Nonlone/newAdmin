package com.feitai.admin.core.service;

import lombok.Data;

/**
 * 搜索参数
 */
@Data
public class SearchParams {

    private String name;

    private Operator operator;

    private String[] values;


}
