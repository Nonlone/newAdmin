package com.feitai.admin.mop.base;

import lombok.Data;

@Data
public abstract class BasePageReq {

    protected int pageIndex;

    protected int limit;

    //继承的子类自己补充查询条件
}
