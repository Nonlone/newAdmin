package com.feitai.admin.core.web;

import com.feitai.admin.core.service.Sort;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;

import javax.servlet.ServletRequest;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

@Slf4j
public class PageBulider {


    /**
     * 页面索引，一般从0开始
     */
    private static String pageIndex = "pageIndex";

    /**
     * 页面大小
     */
    private static String pageSize = "limit";

    /**
     * 排序字段
     */
    private static String sortField = "field";

    /**
     * 排序方法 ASC OR DESC
     */
    private static String sortDirection = "direction";


    public static void setPageIndex(String pageIndex) {
        PageBulider.pageIndex = pageIndex;
    }

    public static void setPageSize(String pageSize) {
        PageBulider.pageSize = pageSize;
    }

    public static void setSortField(String sortField) {
        PageBulider.sortField = sortField;
    }

    public static void setSortDirection(String sortDirection) {
        PageBulider.sortDirection = sortDirection;
    }

    public static int getPageNo(ServletRequest request) {
        String value = getParamEndWith(request, pageIndex);
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException nfe) {
            log.error(String.format("getPage error value<%s>", value));
        }
        return 0;
    }


    public static int getPageSize(ServletRequest request) {
        String value = getParamEndWith(request, pageSize);
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException nfe) {
            log.error(String.format("getPageSize error value<%s>", value));
        }
        return 0;
    }

    public static List<Sort> getSort(ServletRequest request) {
        String[] sortFields = getParamsEndWith(request, sortField);
        String[] sortDirections = getParamsEndWith(request, sortDirection);
        if (ArrayUtils.isEmpty(sortFields) || ArrayUtils.isEmpty(sortDirections)) {
            return null;
        }
        int max = Math.min(sortFields.length, sortDirections.length);
        List<Sort> results = new ArrayList<>();
        for (int i = 0; i < max; i++) {
            results.add(new Sort(sortFields[i], sortDirections[i]));
        }
        return results;
    }


    private static String getParamEndWith(ServletRequest request, String endWith) {
        String[] values = getParamsEndWith(request, endWith);
        if (values != null) {
            return values[0];
        }
        return null;
    }

    private static String[] getParamsEndWith(ServletRequest request, String endWith) {
        Enumeration<String> paramNames = request.getParameterNames();
        while ((paramNames != null) && paramNames.hasMoreElements()) {
            String paramName = (String) paramNames.nextElement();
            if (paramName.endsWith(endWith)) {
                String[] values = request.getParameterValues(paramName);
                if (values != null && values.length > 0) {
                    return values;
                }
            }
        }
        return null;
    }
}
