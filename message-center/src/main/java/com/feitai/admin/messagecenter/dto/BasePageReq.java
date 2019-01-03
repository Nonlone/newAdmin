package com.feitai.admin.messagecenter.dto;

import lombok.Data;
import lombok.ToString;

/**
 * 分页查询请求抽象类
 */
@Data
@ToString
public abstract class BasePageReq {

    /**
     * 查询的页码
     */
    protected int pageNum = 1;

    /**
     *每页的数量
     */
    protected int pageSize = 10;

}
