/**
 * @author
 * @version 1.0
 * @desc AppConfig
 * @since 2018-07-11 17:04:59
 */

package com.feitai.admin.backend.config.service;

import com.feitai.admin.core.service.ClassPrefixDynamicSupportService;
import com.feitai.admin.core.service.DynamitSupportService;
import com.feitai.jieya.server.dao.appconfig.mapper.AppConfigMapper;
import com.feitai.jieya.server.dao.appconfig.model.AppConfig;
import com.feitai.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

@Service
@Slf4j
public class AppConfigService extends ClassPrefixDynamicSupportService<AppConfig> {

    @Autowired
    private AppConfigMapper appConfigMapper;
    /***
     * 获取相应字段值
     * @param typeCode
     * @param code
     * @return
     */
    public String findByTypeCodeAndCode(String typeCode, String code) {
        if(StringUtils.isNotBlank(typeCode)&&StringUtils.isNotBlank(code)){
            Example example = Example.builder(AppConfig.class).andWhere(Sqls.custom().andEqualTo("code",code).andEqualTo("typeCode",typeCode)).build();
            AppConfig appConfig = appConfigMapper.selectOneByExample(example);
            if (appConfig != null) {
                return appConfig.getName();
            }
            return String.format("typeCode[%s],code[%s]不存在", typeCode, code);
        }else{
            return "";
        }

    }
}
