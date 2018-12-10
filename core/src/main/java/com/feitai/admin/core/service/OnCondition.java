package com.feitai.admin.core.service;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import tk.mybatis.mapper.util.StringUtil;

/**
 * detail:
 * author:
 * date:2018/9/28
 */
@Getter
public class OnCondition {

    public OnCondition(SelectMultiTable.ConnectType connect, String firstParam, Operator operator, String secondParam) {
        this.connect = connect;
        this.firstParam = StringUtil.camelhumpToUnderline(firstParam);
        this.operator = operator;
        this.secondParam = StringUtil.camelhumpToUnderline(secondParam);
    }

    /**
     * 连接符，非第一个参数后增加拼接
     */
    @Getter
    private SelectMultiTable.ConnectType connect;

    /**
     * On条件第一个参数
     */
    private String firstParam;

    /**
     * 操作符
     */
    private Operator operator;

    /**
     * 第二个参数
     */
    private String secondParam;

    /**
     * 拼接参数Sql，不包括连接符connect
     *
     * @return
     */
    public String buildOnCondition(String tableAlias) {
        switch (operator) {
            case EQ:
                if (!StringUtils.isAnyBlank(firstParam, secondParam)) {
                    return "maintable." + firstParam + " = " + tableAlias + "." + secondParam;
                }
                return null;
            case OREQ:
                if (!StringUtils.isAnyBlank(firstParam, secondParam)) {
                    return "maintable." + firstParam + " = " + tableAlias + "." + secondParam;
                }
                return null;
            case LIKE:
                if (!StringUtils.isAnyBlank(firstParam, secondParam)) {
                    return "maintable." + firstParam + " like " + tableAlias + "." + secondParam;
                }
                return null;
            case ORLIKE:
                if (!StringUtils.isAnyBlank(firstParam, secondParam)) {
                    return "maintable." + firstParam + " like " + tableAlias + "." + secondParam;
                }
                return null;
            case ANDLIKE:
                if (!StringUtils.isAnyBlank(firstParam, secondParam)) {
                    return "maintable." + firstParam + " like " + tableAlias + "." + secondParam;
                }
                return null;
            case GT:
                if (!StringUtils.isAnyBlank(firstParam, secondParam)) {
                    return "maintable." + firstParam + " > " + tableAlias + "." + secondParam;
                }
                return null;
            case LT:
                if (!StringUtils.isAnyBlank(firstParam, secondParam)) {
                    return "maintable." + firstParam + " < " + tableAlias + "." + secondParam;
                }
                return null;
            case GTE:
                if (!StringUtils.isAnyBlank(firstParam, secondParam)) {
                    return "maintable." + firstParam + " >= " + tableAlias + "." + secondParam;
                }
                return null;
            case LTE:
                if (!StringUtils.isAnyBlank(firstParam, secondParam)) {
                    return "maintable." + firstParam + " <= " + tableAlias + "." + secondParam;
                }
                return null;
            case NOTLIKE:
                if (!StringUtils.isAnyBlank(firstParam, secondParam)) {
                    return "maintable." + firstParam + " not like " + tableAlias + "." + secondParam;
                }
                return null;
            case ANDNOTLIKE:
                if (!StringUtils.isAnyBlank(firstParam, secondParam)) {
                    return "maintable." + firstParam + " not like " + tableAlias + "." + secondParam;
                }
                return null;
            case NOTEQ:
                if (!StringUtils.isAnyBlank(firstParam, secondParam)) {
                    return "maintable." + firstParam + " <> " + tableAlias + "." + secondParam;
                }
                return null;
            case ANDNOTEQ:
                if (!StringUtils.isAnyBlank(firstParam, secondParam)) {
                    return "maintable." + firstParam + " <> " + tableAlias + "." + secondParam;
                }
                return null;
            case ISNULL:
                if (!StringUtils.isAnyBlank(firstParam)) {
                    return "maintable." + firstParam + " is null ";
                }
                return null;
            case ISNOTNULL:
                if (!StringUtils.isAnyBlank(firstParam)) {
                    return "maintable." + firstParam + " is not null ";
                }
                return null;
            case IN:
                if (!StringUtils.isAnyBlank(firstParam, secondParam)) {
                    return "maintable." + firstParam + " in ( " + tableAlias + "." + secondParam + " ) ";
                }
                return null;
            case NOTIN:
                if (!StringUtils.isAnyBlank(firstParam, secondParam)) {
                    return "maintable." + firstParam + " not in ( " + tableAlias + "." + secondParam + " ) ";
                }
                return null;
            default:
                return null;
        }
    }

}