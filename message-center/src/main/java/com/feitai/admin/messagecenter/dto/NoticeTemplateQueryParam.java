package com.feitai.admin.messagecenter.dto;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class NoticeTemplateQueryParam extends BasePageReq implements Serializable {
    private static final long serialVersionUID = -1737607617808345081L;
    /**
     * 模板code
     */
    private String code;
    /**
     * 模板名称
     */
    private String name;
}
