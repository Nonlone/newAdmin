package com.feitai.admin.backend.vo;

import lombok.Data;

/**
 * detail:服务端通用ResponseVO
 * author:
 * date:2018/11/27
 */
@Data
public class BackendResponse {

    private int code;

    private String message;

    public BackendResponse(int code,String message){
        this.code = code;
        this.message = message;
    }
}
