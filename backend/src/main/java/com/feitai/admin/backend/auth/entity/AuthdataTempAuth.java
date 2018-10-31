
package com.feitai.admin.backend.auth.entity;

import com.feitai.jieya.server.dao.authdata.model.BaseAuthData;
import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;


@Table(name = "t_authdata_temp_auth")
@Data
public class AuthdataTempAuth extends BaseAuthData {

    private Integer singleFailCount;

    private Integer singleCount;

    private Date expiredTime;

}

