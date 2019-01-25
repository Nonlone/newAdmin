package com.feitai.admin.mop.advert.request;


import com.feitai.admin.mop.base.BasePageReq;
import lombok.Data;

/**
 * @Author qiuyunlong
 */
@Data
public class QueryRequest extends BasePageReq {

    private Integer status;

    private Long blockId;

}
