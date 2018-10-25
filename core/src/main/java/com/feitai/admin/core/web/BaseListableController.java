package com.feitai.admin.core.web;

import com.feitai.admin.core.service.*;
import com.feitai.utils.ObjectUtils;
import com.feitai.utils.StringUtils;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Joiner;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.web.util.WebUtils;

import javax.servlet.ServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class BaseListableController<T> extends BaseController {

    protected static String searchPrefix = "search_";

    protected abstract DynamitSupportService<T> getService();

    private Class<T> entityClass = ObjectUtils.getGenericClass(getClass());

    /***
     * 复杂查询时返回select语句的主体，单表查询时返回null
     * @return
     */
    protected abstract String getSql();


    protected Page<T> list(ServletRequest request) {
        int pageNo = PageBulider.getPageNo(request);
        int pageSize = PageBulider.getPageSize(request);
        String sql = getSql();
        if (StringUtils.isNotBlank(sql)) {
            return buildPage(getService().findAll(sql), pageNo, pageSize);
        } else {
            List<Sort> sortList = PageBulider.getSort(request);
            Map<String, Object> searchMap = WebUtils.getParametersStartingWith(request, searchPrefix);
            List<SearchParams> searchParamsList = buildSearchParams(searchMap);
            return buildPage(getService().findBySelectMultiTable(SelectMultiTable.builder(entityClass), searchParamsList, sortList, pageNo, pageSize), pageNo, pageSize);
        }
    }

    /***
     * 多表搜索，需提供select语句主体
     * @param request
     * @param sqlHead
     * @return
     */
    protected Page<T> list(ServletRequest request, String sqlHead) {
        return list(request, sqlHead, SelectMultiTable.MAIN_ALAIS, "id");
    }

    /***
     * 多表搜索，需提供select语句主体
     * @param request
     * @param sqlHead
     * @return
     */
    protected Page<T> list(ServletRequest request, String sqlHead, String sqlMainTableAlias, String sqlHeadId) {
        int pageNo = PageBulider.getPageNo(request);
        int pageSize = PageBulider.getPageSize(request);
        Map<String, Object> searchMap = WebUtils.getParametersStartingWith(request, searchPrefix);
        List<SearchParams> searchParamsList = buildSearchParams(searchMap);
        String groupBy = " GROUP BY " + sqlMainTableAlias + "." + sqlHeadId;
        if (sqlHeadId.startsWith(sqlMainTableAlias) || sqlHeadId.indexOf(".") > 0) {
            groupBy = " GROUP BY " + sqlHeadId;
        }
        String sql = sqlHead + getService().buildSqlWhereCondition(searchParamsList, sqlMainTableAlias) + groupBy;
        return buildPage(getService().findPageBySqls(sql, pageNo, pageSize), pageNo, pageSize);
    }


    /**
     * 构建搜索参数
     *
     * @param searchMap
     * @return
     */
    protected List<SearchParams> buildSearchParams(Map<String, Object> searchMap) {
        List<SearchParams> searchParamsList = new ArrayList<>();
        for (Map.Entry<String, Object> entry : searchMap.entrySet()) {
            String[] sources = entry.getKey().replace(searchPrefix, "").split("_");
            if (sources.length >= 2) {
                Operator operator = Operator.valueOf(sources[0]);
                // 切分第一个作为分割符
                sources = ArrayUtils.remove(sources, 0);
                // 余后重新组合，转换驼峰，和 . 号
                String name = StringUtils.humpToLine(Joiner.on("_").join(sources)).replace(".", "_");
                if (entry.getValue().getClass().isArray()
                        && entry.getValue().getClass().getComponentType() == String.class) {
                    // String数组
                    searchParamsList.add(new SearchParams() {{
                        this.setName(name);
                        this.setOperator(operator);
                        if (!entry.getValue().equals("")) {
                            this.setValues((String[]) entry.getValue());
                        }
                    }});
                } else if (String.class.isInstance(entry.getValue())) {
                    // String类型
                    searchParamsList.add(new SearchParams() {{
                        this.setName(name);
                        this.setOperator(operator);
                        if (!entry.getValue().equals("")) {
                            this.setValues(new String[]{(String) entry.getValue()});
                        }
                    }});
                }
            }
        }
        return searchParamsList;
    }


    protected <K> Page<K> buildPage(List<K> results, int pageNo, int pageSize) {
        PageInfo<K> pageInfo = new PageInfo(results, pageSize);
        Page<K> page = new Page(pageInfo.getList());
        page.setTotalPages(pageInfo.getPages());
        page.setTotalElements(results.size());
        page.setNumber(pageNo);
        page.setSize(pageSize);
        page.setFirst(pageInfo.isIsFirstPage());
        page.setLast(pageInfo.isIsLastPage());
        page.setNext(pageInfo.isHasNextPage());
        page.setPrevious(pageInfo.isHasPreviousPage());
        return page;
    }

}
