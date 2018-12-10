package com.feitai.admin.core.vo;

import com.feitai.admin.core.contants.ResultCode;
import lombok.Data;
import lombok.NonNull;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public class ResponseBean<T> {

    private Integer code;

    private String message;

    @NonNull
    private T data;

    public ResponseBean(ResultCode resultCode, T data) {
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
        this.data = data;
    }

    public ResponseBean(ResultCode status) {
        this.code = status.getCode();
        this.message = status.getMessage();
    }

    public void setResultCode(ResultCode status) {
        this.code = status.getCode();
        this.message = status.getMessage();
    }

}