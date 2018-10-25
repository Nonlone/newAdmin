package com.feitai.admin.core.vo;

public class AjaxResult {

    private boolean success;
    private String msg;
    //以下两个参数在store里用到
    private boolean hasError;
    private String error;

    public AjaxResult() {

    }


    public AjaxResult(boolean success, String msg) {
        super();
        this.success = success;
        this.msg = msg;
    }


    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isHasError() {
        return hasError;
    }

    public void setHasError(boolean hasError) {
        this.hasError = hasError;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }


}
