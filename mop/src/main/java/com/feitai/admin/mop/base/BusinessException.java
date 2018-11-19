package com.feitai.admin.mop.base;

import lombok.Data;

/**
 * 自定义业务异常
 */
@Data
public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }
}
