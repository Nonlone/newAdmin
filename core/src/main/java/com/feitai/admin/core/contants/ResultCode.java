package com.feitai.admin.core.contants;

import lombok.Getter;

@Getter
public class ResultCode {

    private Integer code;

    private String message;

    protected ResultCode(Integer code,String message) {
        this.code = code;
        this.message = message;
    }

    public static final ResultCode SUCCESS = new ResultCode(0,"成功") ;

    public static final ResultCode FAIL = new ResultCode(1,"失败") ;

}
