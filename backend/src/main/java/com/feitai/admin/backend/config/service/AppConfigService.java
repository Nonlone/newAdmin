/**
 * @author
 * @version 1.0
 * @desc AppConfig
 * @since 2018-07-11 17:04:59
 */

package com.feitai.admin.backend.config.service;

import com.feitai.admin.core.service.DynamitSupportService;
import com.feitai.jieya.server.dao.appconfig.model.AppConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AppConfigService extends DynamitSupportService<AppConfig> {

    /***
     * 获取相应字段值
     * @param typeCode
     * @param code
     * @return
     */
    public String findByTypeCodeAndCode(String typeCode, String code) {
        AppConfig appConfigsearch = new AppConfig();
        appConfigsearch.setCode(code);
        appConfigsearch.setTypeCode(typeCode);
        AppConfig appConfig = mapper.selectOne(appConfigsearch);
        if (appConfig != null) {
            return appConfig.getName();
        }
        return String.format("typeCode[%s],code[%s]不存在", typeCode, code);
    }
}
