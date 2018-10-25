package com.feitai.admin.core.service;

import com.feitai.base.mybatis.ManyAnnotationFieldWalkProcessor;
import com.feitai.base.mybatis.OneAnnotationFieldWalkProcessor;
import com.feitai.base.mybatis.SqlMapper;
import com.feitai.utils.ObjectUtils;
import com.feitai.utils.StringUtils;
import com.github.pagehelper.PageHelper;
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

    @PostConstruct
    public void init() {
        this.sqlMapper = new SqlMapper(sqlSession);
    }

    /**
     * 默认数据源构建SqlMapper
     *
     * @return
     */
    protected SqlMapper doGetMapper() {
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
                    this.sqlMapper = doGetMapper();
                }
            }
        }
        return this.sqlMapper;
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        this.oneAnnotationFieldWalkProcessor = new OneAnnotationFieldWalkProcessor(applicationContext);
        this.manyAnnotationFieldWalkProcessor = new ManyAnnotationFieldWalkProcessor(applicationContext);
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
    public List<T> findPageBySqls(String sqls, int pageNo, int pageSize) {
        PageHelper.startPage(pageNo, pageSize);
        List<T> results = getSqlMapper().selectList(sqls, classOfT);
        return walkProcessCollection(results);
    }

    /***
     * 根据sql获取page
     *
     */
    public List<T> findBySelectMultiTable(SelectMultiTable selectMultiTable, List<SearchParams> searchParamsList, List<Sort> sortList, int pageNo, int pageSize) {
        String sql = selectMultiTable.buildSqlString() + buildSqlWhereCondition(searchParamsList) + " ORDER BY " + buildSqlSort(sortList);
        PageHelper.startPage(pageNo, pageSize);
        List<T> results = getSqlMapper().selectList(sql, classOfT);
        return walkProcessCollection(results);
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
        ObjectUtils.fieldWalkProcess(e, getOneAnnotationFieldWalkProcessor());
        ObjectUtils.fieldWalkProcess(e, getManyAnnotationFieldWalkProcessor());
        return e;
    }

    protected <E> List<E> walkProcessCollection(List<E> collection) {
        for (E e : collection) {
            walkProcess(e);
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
                sql.append(" where 1=1 ");
                break;
            }
        }
        if (!CollectionUtils.isEmpty(searchParamsList)) {
            for (SearchParams searchParams : searchParamsList) {
                switch (searchParams.getOperator()) {
                    case EQ:
                        if (!ArrayUtils.isEmpty(searchParams.getValues())) {
                            sql.append(" and " + prefix + searchParams.getName() + " = '" + searchParams.getValues()[0] + "'");
                        }
                        break;
                    case LIKE:
                        if (!ArrayUtils.isEmpty(searchParams.getValues())) {
                            sql.append(" and " + prefix + searchParams.getName() + " like '%" + searchParams.getValues()[0] + "%'");
                        }
                        break;
                    case GT:
                        if (!ArrayUtils.isEmpty(searchParams.getValues())) {
                            sql.append(" and " + prefix + searchParams.getName() + " > '" + searchParams.getValues()[0] + "'");
                        }
                        break;
                    case LT:
                        if (!ArrayUtils.isEmpty(searchParams.getValues())) {
                            sql.append(" and " + prefix + searchParams.getName() + " < '" + searchParams.getValues()[0] + "'");
                        }
                        break;
                    case GTE:
                        if (!ArrayUtils.isEmpty(searchParams.getValues())) {
                            sql.append(" and " + prefix + searchParams.getName() + " >= '" + searchParams.getValues()[0] + "'");
                        }
                        break;
                    case LTE:
                        if (!ArrayUtils.isEmpty(searchParams.getValues())) {
                            sql.append(" and " + prefix + searchParams.getName() + " <= '" + searchParams.getValues()[0] + "'");
                        }
                        break;
                    case ORLIKE:
                        if (!ArrayUtils.isEmpty(searchParams.getValues())) {
                            int i = 0;
                            for (String value : searchParams.getValues()) {
                                if (i == 0) {
                                    sql.append(" and (" + prefix + searchParams.getName() + " like '%" + value + "%'");
                                } else {
                                    sql.append(" or " + prefix + searchParams.getName() + " like '%" + value + "%'");
                                }
                                i++;
                            }
                            sql.append(")");
                        }
                        break;
                    case ANDLIKE:
                        if (!ArrayUtils.isEmpty(searchParams.getValues())) {
                            int i = 0;
                            for (String value : searchParams.getValues()) {
                                if (i == 0) {
                                    sql.append(" and " + prefix + searchParams.getName() + " like '%" + value + "%'");
                                } else {
                                    sql.append(" and " + prefix + searchParams.getName() + " like '%" + value + "%'");
                                }
                                i++;
                            }
                        }
                        break;
                    case ANDNOTLIKE:
                        if (!ArrayUtils.isEmpty(searchParams.getValues())) {
                            int i = 0;
                            for (String value : searchParams.getValues()) {
                                if (i == 0) {
                                    sql.append(" and " + prefix + searchParams.getName() + " not like '%" + value + "%'");
                                } else {
                                    sql.append(" and " + prefix + searchParams.getName() + " not like '%" + value + "%'");
                                }
                                i++;
                            }
                        }
                        break;
                    case OREQ:
                        if (!ArrayUtils.isEmpty(searchParams.getValues())) {
                            int i = 0;
                            for (String value : searchParams.getValues()) {
                                if (i == 0) {
                                    sql.append(" and (" + prefix + searchParams.getName() + " = '" + value + "'");
                                } else {
                                    sql.append(" or " + prefix + searchParams.getName() + " = '" + value + "'");
                                }
                                i++;
                            }
                            sql.append(")");
                        }
                        break;
                    case ANDNOTEQ:
                        if (!ArrayUtils.isEmpty(searchParams.getValues())) {
                            int i = 0;
                            for (String value : searchParams.getValues()) {
                                if (i == 0) {
                                    sql.append(" and " + prefix + searchParams.getName() + " != '" + value + "'");
                                } else {
                                    sql.append(" or " + prefix + searchParams.getName() + " != '" + value + "'");
                                }
                                i++;
                            }
                        }
                        break;
                    case NOTEQ:
                        if (!ArrayUtils.isEmpty(searchParams.getValues())) {
                            sql.append(" and " + prefix + searchParams.getName() + " != '" + searchParams.getValues()[0] + "'");
                        }
                        break;
                    case NOTLIKE:
                        if (!ArrayUtils.isEmpty(searchParams.getValues())) {
                            sql.append(" and " + prefix + searchParams.getName() + " not like '%" + searchParams.getValues()[0] + "%'");
                        }
                        break;
                    case ISNULL:
                        sql.append(" and " + prefix + searchParams.getName() + " is null");
                        break;
                    case ISNOTNULL:
                        sql.append(" and " + prefix + searchParams.getName() + " is not null");
                        break;
                    case IN:
                        if (!ArrayUtils.isEmpty(searchParams.getValues())) {
                            sql.append("and " + prefix + searchParams.getName() + " in '" + searchParams.getValues()[0] + "'");
                        }
                        break;
                    case NOTIN:
                        if (!ArrayUtils.isEmpty(searchParams.getValues())) {
                            sql.append("and " + prefix + searchParams.getName() + " not in '" + searchParams.getValues()[0] + "'");
                        }
                        break;
                }
            }
        }
        return sql.toString();
    }

}
