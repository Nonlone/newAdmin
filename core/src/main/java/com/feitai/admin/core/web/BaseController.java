package com.feitai.admin.core.web;

import com.feitai.admin.core.vo.AjaxResult;

public abstract class BaseController {

    protected final static AjaxResult successResult = new AjaxResult(true, "操作成功！");

    protected final static AjaxResult failResult = new AjaxResult(false, "操作失败");
}
