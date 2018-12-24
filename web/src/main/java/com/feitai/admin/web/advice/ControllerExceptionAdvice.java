package com.feitai.admin.web.advice;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.feitai.admin.core.vo.AjaxResult;
import com.feitai.admin.mop.base.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.ShiroException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author leishunyang
 */
@ControllerAdvice
@Slf4j
public class ControllerExceptionAdvice {

    private final static String KEY_MESSAGE = "message";

    private final static String KEY_DETAIL = "detail";

    private JSONObject buildJson(Throwable throwable) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(KEY_MESSAGE, throwable.getMessage());
        JSONArray array = new JSONArray();
        for (StackTraceElement stackTraceElement : throwable.getStackTrace()) {
            array.add(stackTraceElement.getClassName() + " : " + stackTraceElement.getLineNumber());
        }
        jsonObject.put(KEY_DETAIL, array);
        return jsonObject;
    }

    /**
     * 权限异常，抛出跳到登录页
     *
     * @param shiroException
     */
    @ExceptionHandler(ShiroException.class)
    public void shiroException(ShiroException shiroException) {
        log.error(String.format("ShiroException Error %s", shiroException.getMessage()));
        throw shiroException;
    }

    /**
     * 无法解析JSON异常处理
     *
     * @param exception
     * @return
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    public JSONObject httpMessageNotReadableException(HttpMessageNotReadableException exception) {
        log.error(String.format("httpMessageNotReadableException %s", exception.getMessage()), exception);
        return buildJson(exception);
    }

    /**
     * 参数校验失败异常处理
     *
     * @param exception
     * @return
     */
    @ExceptionHandler({
            MethodArgumentNotValidException.class,
            BindException.class
    })
    @ResponseBody
    public JSONObject validate(MethodArgumentNotValidException exception) {
        String errorMessage = getValidResult(exception.getBindingResult());
        log.warn(errorMessage);
        return buildJson(exception);
    }


    /**
     * API异常异常处理
     *
     * @param exception
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public JSONObject exceptionHandler(Exception exception) {
        log.error(String.format("Exception Error %s", exception.getMessage()), exception);
        return buildJson(exception);
    }

    /**
     * 业务异常
     *
     * @param exception
     * @return
     */
    @ExceptionHandler(BusinessException.class)
    @ResponseBody
    public AjaxResult businessExceptionHandler(Exception exception) {
        log.error(String.format("Exception Error %s", exception.getMessage()), exception);
        AjaxResult ajaxResult = new AjaxResult(false, exception.getMessage());
        ajaxResult.setHasError(true);
        return ajaxResult;
    }


    /**
     * 获取系统日志校验信息
     *
     * @param bindingResult
     * @return
     */
    private String getValidResult(BindingResult bindingResult) {
        StringBuilder sb = new StringBuilder();
        for (ObjectError error : bindingResult.getAllErrors()) {
            FieldError fError = (FieldError) error;
            sb.append(String.format(" param(%s) value(%s) validate error<%s>", fError.getField(), fError.getRejectedValue(), fError.getDefaultMessage()));
        }
        return sb.toString();
    }


}
