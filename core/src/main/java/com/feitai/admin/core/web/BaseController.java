package com.feitai.admin.core.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.feitai.admin.core.service.Page;
import com.feitai.admin.core.vo.AjaxResult;
import lombok.NonNull;

import java.util.List;

public abstract class BaseController {

    protected final static AjaxResult successResult = new AjaxResult(true, "操作成功！");

    protected final static AjaxResult failResult = new AjaxResult(false, "操作失败");

    private final String KEY_CONTENT = "content";


    protected JSONObject switchContent(@NonNull Page<?> page, @NonNull List<?> content) {
        JSONObject result = (JSONObject) JSON.toJSON(page);
        result.put(KEY_CONTENT, JSON.toJSON(content));
        return result;
    }
}
