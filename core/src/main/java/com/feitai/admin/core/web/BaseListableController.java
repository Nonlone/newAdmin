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


    protected Page<T> list(ServletRequest request) {
        int pageNo = PageBulider.getPageNo(request);
        int pageSize = PageBulider.getPageSize(request);
        List<Sort> sortList = PageBulider.getSort(request);
        Map<String, Object> searchMap = WebUtils.getParametersStartingWith(request, searchPrefix);
        List<SearchParams> searchParamsList = buildSearchParams(searchMap);
        Integer totalSize = getService().countBySelectMultiTable(SelectMultiTable.builder(entityClass), searchParamsList);
        List<T> result = getService().findBySelectMultiTable(SelectMultiTable.builder(entityClass), searchParamsList, sortList, pageNo, pageSize);
        return buildPage(result, totalSize, pageNo, pageSize);
    }


    protected List<SearchParams> bulidSearchParamsList(ServletRequest request) {
        return buildSearchParams(WebUtils.getParametersStartingWith(request, searchPrefix));
    }

    protected List<SearchParams> bulidSearchParamsList(ServletRequest request, String prefix) {
        return buildSearchParams(WebUtils.getParametersStartingWith(request, prefix));
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

    /**
     * Sql 搜索并分页
     *
     * @param sqls
     * @param pageNo
     * @param pageSize
     * @param countSqls
     * @param countAlias
     * @return
     */
    protected Page<T> list(String sqls, int pageNo, int pageSize, String countSqls, String countAlias) {
        Integer totalSize = getService().countBySqls(countSqls, countAlias);
        List<T> resultList = getService().findAllBySqls(sqls, pageNo, pageSize);
        return buildPage(resultList, totalSize, pageNo, pageSize);
    }


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
        return buildPage(getService().findAllBySqls(sql, pageNo, pageSize), pageNo, pageSize);
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
                String name = StringUtils.humpToLine(Joiner.on("_").join(sources));
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


    /**
     * 构建分页器
     *
     * @param results
     * @param totalSize
     * @param pageNo
     * @param pageSize
     * @param <K>
     * @return
     */
    protected <K> Page<K> buildPage(List<K> results, int totalSize, int pageNo, int pageSize) {
        Page<K> page = new Page(results);
        if (pageNo >= 0 && pageSize > 0) {
            page.setTotalPages(totalSize / pageSize + totalSize % pageSize > 0 ? 1 : 0);
        } else {
            page.setTotalPages(0);
        }
        page.setTotalElements(totalSize);
        page.setNumber(pageNo);
        page.setSize(pageSize);
        page.setFirst(pageNo == 1);
        page.setLast(pageNo == page.getTotalPages());
        page.setNext(!page.isFirst());
        page.setPrevious(!page.isLast());
        return page;
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
