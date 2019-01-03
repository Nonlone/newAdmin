package com.feitai.admin.backend.creditdata.model;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Table;
import java.util.Date;

/**
 * detail:
 * author:longhaoteng
 * date:2018/12/28
 */
@ToString
@Data
@Table(name = "t_authdata_record")
public class AuthdataRecord {

    private Long dataId;

    private byte productCode;

    private Long userId;

    private Long cardId;

    private String source;

    private String code;

    private String callDataPara;

    private String callResultFlag;

    private Date authTime;

    private Date expiredTime;

    private Date createdTime;

    private Date updateTime;

}
