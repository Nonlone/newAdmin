package com.feitai.admin.core.service;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import tk.mybatis.mapper.entity.Config;
import tk.mybatis.mapper.entity.EntityTable;
import tk.mybatis.mapper.mapperhelper.EntityHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 多表查询Sql构造器
 */
@Data
@Slf4j
public class SelectMultiTable {

    /***
     * 主表的别名
     */
    public static final String MAIN_ALAIS = "maintable";

    /***
     * 还款列表计数别名
     */
    public static final String RCOUNT_ALIAS = "RCOUNT";

    /**
     * 计数别名
     */
    public static final String COUNT_ALIAS = "count";

    /***
     *查询配置
     */
    private Config config;

    /**
     * 搜索主表
     */
    private Class<?> resultClass;

    /**
     * join连接表
     */
    private List<TableJoin> tableJoinList;


    private SelectMultiTable() {
    }


    /**
     * 静态构造器
     *
     * @param resultClass
     * @return
     */
    public static SelectMultiTable builder(Class<?> resultClass) {
        SelectMultiTable selectMultiTable = new SelectMultiTable();
        selectMultiTable.resultClass = resultClass;
        selectMultiTable.tableJoinList = new ArrayList<>();
        selectMultiTable.config = new Config();
        return selectMultiTable;
    }

    /**
     * 左连接
     *
     * @param joinClass
     * @param tableAlias
     * @param onConditions
     * @return
     */
    public SelectMultiTable leftJoin(Class<?> joinClass, String tableAlias, OnCondition... onConditions) {
        return leftJoin(joinClass, tableAlias, Arrays.asList(onConditions));
    }

    /**
     * 左连接
     *
     * @param joinClass
     * @param tableAlias
     * @param onConditionList
     * @return
     */
    public SelectMultiTable leftJoin(Class<?> joinClass, String tableAlias, List<OnCondition> onConditionList) {
        return join(JoinType.LEFT, joinClass, tableAlias, onConditionList);
    }

    /**
     * 右连接
     *
     * @param joinClass
     * @param tableAlias
     * @param onConditions
     * @return
     */
    public SelectMultiTable rightJoin(Class<?> joinClass, String tableAlias, OnCondition... onConditions) {
        return rightJoin(joinClass, tableAlias, Arrays.asList(onConditions));
    }

    /**
     * 右连接
     *
     * @param joinClass
     * @param tableAlias
     * @param onConditionList
     * @return
     */
    public SelectMultiTable rightJoin(Class<?> joinClass, String tableAlias, List<OnCondition> onConditionList) {
        return join(JoinType.RIGHT, joinClass, tableAlias, onConditionList);
    }

    /**
     * 内连接
     *
     * @param joinClass
     * @param tableAlias
     * @param onConditions
     * @return
     */
    public SelectMultiTable innerJoin(Class<?> joinClass, String tableAlias, OnCondition... onConditions) {
        return innerJoin(joinClass, tableAlias, Arrays.asList(onConditions));
    }

    /**
     * 内连接
     *
     * @param joinClass
     * @param tableAlias
     * @param onConditionList
     * @return
     */
    public SelectMultiTable innerJoin(Class<?> joinClass, String tableAlias, List<OnCondition> onConditionList) {
        return join(JoinType.INNER, joinClass, tableAlias, onConditionList);
    }

    /**
     * 连接
     *
     * @param joinType
     * @param joinClass
     * @param tableAlias
     * @param onConditions
     * @return
     */
    public SelectMultiTable join(JoinType joinType, Class<?> joinClass, String tableAlias, OnCondition... onConditions) {
        return join(joinType, joinClass, tableAlias, Arrays.asList(onConditions));
    }

    /**
     * 连接
     *
     * @param joinType
     * @param joinClass
     * @param tableAlias
     * @param onConditionList
     * @return
     */
    private SelectMultiTable join(JoinType joinType, Class<?> joinClass, String tableAlias, List<OnCondition> onConditionList) {
        this.getTableJoinList().add(new TableJoin() {{
            this.setJoinType(JoinType.LEFT);
            this.setJoinClass(joinClass);
            this.setTableAlias(tableAlias);
            this.setConditions(onConditionList);
        }});
        return this;
    }

    public String buildSqlString() {
        StringBuffer sqlSb = new StringBuffer();
        EntityHelper.initEntityNameMap(resultClass, config);
        EntityTable entityTable = EntityHelper.getEntityTable(resultClass);
        sqlSb.append("select " + MAIN_ALAIS + ".* from " + entityTable.getName() + " AS " + MAIN_ALAIS + " ");
        for (TableJoin tableJoin : tableJoinList) {
            sqlSb.append(tableJoin.buildJoinString());
        }
        return sqlSb.toString();
    }


    public String buildCountSqlString() {
        StringBuffer sqlSb = new StringBuffer();
        EntityHelper.initEntityNameMap(resultClass, config);
        EntityTable entityTable = EntityHelper.getEntityTable(resultClass);
        sqlSb.append("select  count(*) AS " + COUNT_ALIAS + " from " + entityTable.getName() + " AS " + MAIN_ALAIS + " ");
        for (TableJoin tableJoin : tableJoinList) {
            sqlSb.append(tableJoin.buildJoinString());
        }
        return sqlSb.toString();
    }


    /**
     * 连接表类，join的单位
     */
    @Data
    class TableJoin {

        /**
         * 连接类型
         */
        private JoinType joinType;

        /**
         * 连接表类
         */
        private Class<?> joinClass;

        /**
         * 表别名用，用于拼装
         */
        private String tableAlias;

        /**
         * On条件列表
         */
        private List<OnCondition> conditions;

        public String buildJoinString() {
            StringBuffer sb = new StringBuffer();
            EntityHelper.initEntityNameMap(joinClass, config);
            EntityTable entityTable = EntityHelper.getEntityTable(joinClass);
            String tableName = entityTable.getName();
            for (OnCondition onCondition : conditions) {
                if (sb.length() == 0) {
                    sb.append(" " + joinType + " JOIN " + tableName + " AS " + tableAlias + " ON (");
                    sb.append(onCondition.buildOnCondition(tableAlias) + " ");
                } else {
                    sb.append(onCondition.getConnect() + " " + onCondition.buildOnCondition(tableAlias) + " ");
                }
            }
            sb.append(") ");
            return sb.toString();


        }
    }


    /**
     * 连接On条件类型
     */
    public enum ConnectType {
        AND,
        OR,
    }

    /**
     * 连接表类型
     */
    public enum JoinType {
        INNER,
        LEFT,
        RIGHT,
    }

}
