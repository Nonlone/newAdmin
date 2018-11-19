package com.feitai.admin.mop.superpartner.request;


import com.feitai.admin.mop.base.BasePageReq;
import lombok.Data;

/**
 * @Author qiuyunlong
 */
@Data
public class QueryRequest extends BasePageReq {

    private Long userId;

    private Byte type;

    private String phone;
}
