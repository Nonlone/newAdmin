package com.feitai.admin.backend.config.entity;

import com.feitai.jieya.server.dao.base.model.BaseModel;
import lombok.Data;
import lombok.ToString;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * detail:
 * author:longhaoteng
 * date:2019/1/22
 */
@Table(name = "t_app_manage")
@Data
@ToString
public class AppManage extends BaseModel {

    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;

    private String code;

    private String name;

    private String appId;

    private String bundleId;

}
