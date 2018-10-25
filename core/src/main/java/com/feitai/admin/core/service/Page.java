package com.feitai.admin.core.service;

import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Data
public class Page<T> {

    public Page() {
    }


    public Page(List<T> content) {
        this.content = content;
    }

    /**
     * 总页数
     */
    private int totalPages;

    /**
     * 总长度
     */
    private long totalElements;

    /**
     * 当前页
     */
    private int number;

    /**
     * 页长
     */
    private int size;

    private List<T> content;

    private List<Sort> sorts;

    private boolean first = false;

    private boolean last = false;

    private boolean next = false;

    private boolean previous = false;

    public boolean hasContent() {
        return !CollectionUtils.isEmpty(content);
    }


    public boolean hasNext() {
        return isNext();
    }

    public boolean hasPreivous() {
        return isPrevious();
    }

}
