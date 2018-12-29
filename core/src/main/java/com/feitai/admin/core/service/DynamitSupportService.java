package com.feitai.admin.core.service;

import com.feitai.base.mybatis.ManyAnnotationFieldWalkProcessor;
import com.feitai.base.mybatis.OneAnnotationFieldWalkProcessor;
import com.feitai.base.mybatis.SqlMapper;
import com.feitai.utils.ObjectUtils;
import com.feitai.utils.StringUtils;
import lombok.Getter;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 动态Sql服务类
 *
 * @param <T>
 */
public abstract class DynamitSupportService<T> extends BaseSupportService<T> implements ApplicationContextAware {


    @Getter
    private OneAnnotationFieldWalkProcessor oneAnnotationFieldWalkProcessor;

    @Getter
    private ManyAnnotationFieldWalkProcessor manyAnnotationFieldWalkProcessor;

    @Autowired
    protected SqlSession sqlSession;

    @Autowired
    protected ApplicationContext applicationContext;

    protected SqlMapper sqlMapper;

    public final static String WHERE_COMMON = " where 1=1 ";

    @PostConstruct
    public void init() {
        this.sqlMapper = doGetSqlMapper();
    }

    /**
     * 默认数据源构建SqlMapper
     *
     * @return
     */
    protected SqlMapper doGetSqlMapper() {
        return new SqlMapper(sqlSession);
    }

    /**
     * 获取SqlMapper执行器
     *
     * @return
     */
    public SqlMapper getSqlMapper() {
        if (Objects.isNull(sqlMapper)) {
            synchronized (DynamitSupportService.class) {
                if (Objects.isNull(this.sqlMapper)) {
                    this.sqlMapper = doGetSqlMapper();
                }
            }
        }
        sqlMapper.clearCache();
        return this.sqlMapper;
    }



    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        this.oneAnnotationFieldWalkProcessor = new OneAnnotationFieldWalkProcessor(applicationContext);
        this.manyAnnotationFieldWalkProcessor = new ManyAnnotationFieldWalkProcessor(applicationContext);
    }


    /***
     * 单表查询所有
     * @return
     */
    @Override
    public List<T> findAll() {
        return walkProcessCollection(super.findAll());
    }


    /***
     * 根据id获取单个实体
     * @param id
     * @return
     */
    @Override
    public T findOne(Object id) {
        return walkProcess(super.findOne(id));
    }

    /***
     * 根据条件查询数组
     */
    public List<T> findAll(String sql) {
        List<T> results = getSqlMapper().selectList(sql, classOfT);
        return walkProcessCollection(results);
    }

    /**
     * 根据条件查询单个
     *
     * @param sql
     * @return
     */
    public T findOneBySql(String sql) {
        T t = getSqlMapper().selectOne(sql, classOfT);
        return walkProcess(t);
    }


    /***
     * 根据sql获取page
     *
     */
    public List<T> findAllBySqls(String sqls, int pageNo, int pageSize) {
        if (sqls.toLowerCase().indexOf("limit") < 0 && pageNo >= 0 && pageSize > 0) {
            sqls = sqls + " LIMIT  " + (pageNo * pageSize) + " , " + pageSize;
        }
        List<T> results = getSqlMapper().selectList(sqls, classOfT);
        return walkProcessCollection(results);
    }

    /***
     * 计算sql获取page
     *
     */
    public Integer countBySqls(String sqls, String countAlias) {
        Map<Object, Object> resultMap = getSqlMapper().selectOne(sqls, Map.class);
        Object result = resultMap.get(countAlias);
        if (result instanceof Long) {
            return ((Long) result).intValue();
        }
        return (Integer) result;
    }


    /***
     * 根据sql获取结果数组
     *
     */
    public List<T> findBySelectMultiTable(SelectMultiTable selectMultiTable, List<SearchParams> searchParamsList, List<Sort> sortList, int pageNo, int pageSize) {
        String sql = selectMultiTable.buildSqlString() + buildSqlWhereCondition(searchParamsList)
                + " ORDER BY " + buildSqlSort(sortList)
                + " LIMIT " + pageNo * pageSize + " , " + pageSize;
        List<T> results = getSqlMapper().selectList(sql, classOfT);
        return walkProcessCollection(results);
    }

    /***
     * 根据sql获取count
     *
     */
    public Integer countBySelectMultiTable(SelectMultiTable selectMultiTable, List<SearchParams> searchParamsList) {
        String sql = selectMultiTable.buildCountSqlString() + buildSqlWhereCondition(searchParamsList);
        Map<Object, Object> resultMap = getSqlMapper().selectOne(sql, Map.class);
        Object result = resultMap.get(SelectMultiTable.COUNT_ALIAS);
        if (result instanceof Long) {
            return ((Long) result).intValue();
        }
        return (Integer) result;
    }


    private String buildSqlSort(List<Sort> sortList) {
        StringBuilder sb = new StringBuilder();
        for (Sort sort : sortList) {
            if (sb.length() != 0) {
                sb.append(",");
            }
            sb.append(StringUtils.humpToLine(sort.getProperty()));
            sb.append(" ");
            sb.append(sort.getDirection().toString().toLowerCase());
            sb.append(" ");
        }
        return sb.toString();
    }


    protected <E> E walkProcess(E e) {
        if (!Objects.isNull(e)) {
            ObjectUtils.fieldWalkProcess(e, getOneAnnotationFieldWalkProcessor());
            ObjectUtils.fieldWalkProcess(e, getManyAnnotationFieldWalkProcessor());
        }
        return e;
    }

    protected <E> List<E> walkProcessCollection(List<E> collection) {
        if (!CollectionUtils.isEmpty(collection)) {
            for (E e : collection) {
                walkProcess(e);
            }
        }
        return collection;
    }

    /***
     * 构建搜索条件语句
     *
     * @param searchParamsList
     * @return
     */
    public String buildSqlWhereCondition(List<SearchParams> searchParamsList) {
        return buildSqlWhereCondition(searchParamsList, null);
    }


    /***
     * 构建搜索条件语句
     *
     * @param searchParamsList
     * @return
     */
    public String buildSqlWhereCondition(List<SearchParams> searchParamsList, String alias) {
        StringBuffer sql = new StringBuffer();
        String prefix = "";
        if (StringUtils.isNotBlank(alias)) {
            prefix = alias + ".";
        }
        for (SearchParams searchParams : searchParamsList) {
            if (!ArrayUtils.isEmpty(searchParams.getValues())) {
                long code = System.currentTimeMillis();
                //sql.append(" where "+code+"="+code+" ");
                sql.append(WHERE_COMMON);
                break;
            }
        }
        if (!CollectionUtils.isEmpty(searchParamsList)) {
            for (SearchParams searchParams : searchParamsList) {
                String leftParam = searchParams.getName();
                if (!leftParam.contains(".")) {
                    leftParam = prefix + leftParam;
                }
                String value = null;
                switch (searchParams.getOperator()) {
                    case EQ:
                        value = getValue(searchParams.getValues()[0]);
                        if (!ArrayUtils.isEmpty(searchParams.getValues()) && StringUtils.isNotBlank(value)) {
                            sql.append(" and " + leftParam + " = " + value);
                        }
                        break;
                    case LIKE:
                        value = searchParams.getValues()[0].toString();
                        if (!ArrayUtils.isEmpty(searchParams.getValues()) && StringUtils.isNotBlank(value)) {
                            sql.append(" and " + leftParam + " like '%" + value + "%'");
                        }
                        break;
                    case GT:
                        value = getValue(searchParams.getValues()[0]);
                        if (!ArrayUtils.isEmpty(searchParams.getValues()) && StringUtils.isNotBlank(value)) {
                            sql.append(" and " + leftParam + " > " + value);
                        }
                        break;
                    case LT:
                        value = getValue(searchParams.getValues()[0]);
                        if (!ArrayUtils.isEmpty(searchParams.getValues()) && StringUtils.isNotBlank(value)) {
                            sql.append(" and " + leftParam + " < " + value);
                        }
                        break;
                    case GTE:
                        value = getValue(searchParams.getValues()[0]);
                        if (!ArrayUtils.isEmpty(searchParams.getValues()) && StringUtils.isNotBlank(value)) {
                            sql.append(" and " + leftParam + " >= " + value);
                        }
                        break;
                    case LTE:
                        value = getValue(searchParams.getValues()[0]);
                        if (!ArrayUtils.isEmpty(searchParams.getValues()) && StringUtils.isNotBlank(value)) {
                            sql.append(" and " + leftParam + " <= " + value);
                        }
                        break;
                    case ORLIKE:
                        if (!ArrayUtils.isEmpty(searchParams.getValues())) {
                            int i = 0;
                            for (Object val : searchParams.getValues()) {
                                if (i == 0) {
                                    sql.append(" and (" + leftParam + " like '%" + val.toString() + "%'");
                                } else {
                                    sql.append(" or " + leftParam + " like '%" + val.toString() + "%'");
                                }
                                i++;
                            }
                            sql.append(")");
                        }
                        break;
                    case ANDLIKE:
                        if (!ArrayUtils.isEmpty(searchParams.getValues())) {
                            for (Object val : searchParams.getValues()) {
                                sql.append(" and " + leftParam + " like '%" + val.toString() + "%'");
                            }
                        }
                        break;
                    case ANDNOTLIKE:
                        if (!ArrayUtils.isEmpty(searchParams.getValues())) {
                            for (Object val : searchParams.getValues()) {
                                sql.append(" and " + leftParam + " not like '%" + val.toString() + "%'");
                            }
                        }
                        break;
                    case OREQ:
                        if (!ArrayUtils.isEmpty(searchParams.getValues())) {
                            int i = 0;
                            String[] values = (searchParams.getValues()[0].toString()).split(",");
                            for (Object val : values) {
                                if(StringUtils.isBlank(val.toString())){
                                    break;
                                }
                                if (i == 0) {
                                    sql.append(" and (" + leftParam + " = " + val.toString());
                                } else {
                                    sql.append(" or " + leftParam + " = " + val.toString());
                                }
                                i++;
                                if(i==values.length){
                                    sql.append(")");
                                }
                            }
                        }
                        break;
                    case ANDNOTEQ:
                        if (!ArrayUtils.isEmpty(searchParams.getValues())) {
                            int i = 0;
                            for (Object val : searchParams.getValues()) {
                                if (i == 0) {
                                    sql.append(" and " + leftParam + " != " + getValue(val));
                                } else {
                                    sql.append(" or " + leftParam + " != " + getValue(val));
                                }
                                i++;
                            }
                        }
                        break;
                    case NOTEQ:
                        value = getValue(searchParams.getValues()[0]);
                        if (!ArrayUtils.isEmpty(searchParams.getValues())) {
                            sql.append(" and " + leftParam + " != " + value);
                        }
                        break;
                    case NOTLIKE:
                        value = getValue(searchParams.getValues()[0]);
                        if (!ArrayUtils.isEmpty(searchParams.getValues())) {
                            sql.append(" and " + leftParam + " not like '%" + value + "%'");
                        }
                        break;
                    case ISNULL:
                        sql.append(" and " + leftParam + " is null");
                        break;
                    case ISNOTNULL:
                        sql.append(" and " + leftParam + " is not null");
                        break;
                    case IN:
                        if (!ArrayUtils.isEmpty(searchParams.getValues())) {
                            StringBuffer sbSqlPart = new StringBuffer();
                            for (Object val : searchParams.getValues()) {
                                if (sbSqlPart.length() != 0) {
                                    sbSqlPart.append(",");
                                }
                                sbSqlPart.append(getValue(val));
                            }
                            sql.append("and " + leftParam + " in (" + sbSqlPart.toString() + ")");
                        }
                        break;
                    case NOTIN:
                        if (!ArrayUtils.isEmpty(searchParams.getValues())) {
                            StringBuffer sbSqlPart = new StringBuffer();
                            for (Object val : searchParams.getValues()) {
                                if (sbSqlPart.length() != 0) {
                                    sbSqlPart.append(",");
                                }
                                sbSqlPart.append(getValue(val));
                            }
                            sql.append("and " + leftParam + " not in (" + sbSqlPart + "'");
                        }
                        break;
                }
            }
        }
        return sql.toString();
    }

    private String getValue(Object object) {
        if (object instanceof String && StringUtils.isNotBlank((String) object)) {
            return "'" + (String) object + "'";
        } else if (!Objects.isNull(object)) {
            return object.toString();
        }
        return null;
    }

}
