package com.feitai.admin.core.service;

/**
 * 搜索操作枚举类
 */
public enum Operator {
    EQ, NOTEQ, LIKE, NOTLIKE, GT, LT, GTE, LTE, ISNULL, ISNOTNULL,
    OREQ, ORLIKE, ANDLIKE, ANDNOTEQ, ANDNOTLIKE,
    IN, NOTIN
}