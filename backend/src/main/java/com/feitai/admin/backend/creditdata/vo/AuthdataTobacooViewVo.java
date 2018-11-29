package com.feitai.admin.backend.creditdata.vo;

import com.feitai.jieya.server.dao.authdata.model.AuthdataTobacco;
import lombok.Data;
import lombok.ToString;

/**
 * detail:烟草贷返回页面字段
 * author:longhaoteng
 * date:2018/11/29
 */
@Data
@ToString
public class AuthdataTobacooViewVo extends AuthdataTobacco {

    private String spouseIdTypeName;

    private String socialTypeName;

}
