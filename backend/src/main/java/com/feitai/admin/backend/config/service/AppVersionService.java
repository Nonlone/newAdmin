/**
 * @author 
 * @version 1.0
 * @since  2018-07-12 10:44:26
 * @desc AppVersion
 */

package com.feitai.admin.backend.config.service;

import com.feitai.admin.core.service.ClassPrefixDynamicSupportService;
import com.feitai.jieya.server.dao.appconfig.model.AppVersion;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AppVersionService extends ClassPrefixDynamicSupportService<AppVersion> {

}
