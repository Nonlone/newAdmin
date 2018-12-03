package com.feitai.admin.mop.base;

import lombok.Data;

/**
 * @Author qiuyunlong
 */
@Data
public class RpcResult {

    private static final int SUCCESS_CODE = 0;

    private Integer code;

    private String message;

    private Object data;

    public boolean isSuccess() {
        return SUCCESS_CODE == code;
    }

}
