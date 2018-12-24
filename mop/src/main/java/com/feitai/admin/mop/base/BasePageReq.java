package com.feitai.admin.mop.base;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Data
public abstract class BasePageReq {

    protected int pageIndex;

    protected int limit;

    protected Date startTime;

    protected Date endTime;
    //排序字段
    protected String field;
    //desc,asc
    protected String direction;

    public Date getStartTime() {
        if (startTime != null) {
            startTime = (Date.from(LocalDateTime
                    .ofInstant(startTime.toInstant(), ZoneId.systemDefault())
                    .toLocalDate().atStartOfDay()
                    .atZone(ZoneId.systemDefault()).toInstant()));

        }
        return startTime;
    }

    public Date getEndTime() {
        if (endTime != null) {
            endTime = (Date.from(LocalDateTime
                    .ofInstant(endTime.toInstant(), ZoneId.systemDefault())
                    .toLocalDate().atTime(23,59,59)
                    .atZone(ZoneId.systemDefault()).toInstant()));
        }
        return endTime;
    }

    //前端从0开始，pageHelper从1开始
    public int getPageIndex() {
        return pageIndex + 1;
    }


    //继承的子类自己补充查询条件
}
