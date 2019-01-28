package com.feitai.admin.mop.superpartner.vo;

import com.alibaba.fastjson.JSONObject;
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

    private JSONObject idCardDetail;
}
