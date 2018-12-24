package com.feitai.admin.mop.superpartner.request;


import com.feitai.admin.mop.base.BasePageReq;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * @Author qiuyunlong
 */
@Data
@ToString(callSuper = true)
public class WithdrawOrderQueryRequest extends BasePageReq {

    private Long userId;

    private Integer status;

    private Long orderId;

    private Date startTime;

    private Date endTime;

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


}
