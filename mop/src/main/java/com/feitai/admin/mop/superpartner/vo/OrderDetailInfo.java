package com.feitai.admin.mop.superpartner.vo;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.feitai.base.annotion.NoKeyFilter;
import lombok.Data;

/**
 * @Author qiuyunlong
 */
@Data
@NoKeyFilter
public class OrderDetailInfo {
    private Long partnerUserId;
    private String name;
    private String bankCardNo;
    private String bankName;
    private String bankFullName;
    private String idCardNo;
    private String idCardFront;
    private String idCardReverse;

    @JSONField(serialize = false)
    private JSONObject idCardDetail;

    public String getPicFront() {
        return (String)idCardDetail.get("front");
    }

    public String getPicReverse() {
        return (String)idCardDetail.get("reverse");
    }
}
