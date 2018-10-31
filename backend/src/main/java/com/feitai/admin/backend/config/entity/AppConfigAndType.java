package com.feitai.admin.backend.config.entity;

import com.feitai.base.mybatis.Many;
import com.feitai.jieya.server.dao.appconfig.mapper.AppConfigMapper;
import com.feitai.jieya.server.dao.appconfig.model.AppConfig;
import lombok.Data;

import javax.persistence.Transient;
import java.util.List;

/**
 * detail:
 * author:
 * date:2018/10/12
 */
@Data
public class AppConfigAndType extends AppConfigType {

    @Transient
    @Many(classOfEntity = AppConfig.class, classOfMapper = AppConfigMapper.class, sourceField = "typeCode", targetField = "typeCode")
    private List<AppConfig> appConfigs;
}
