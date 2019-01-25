/**
 * @author 
 * @version 1.0
 * @since  2018-07-12 10:44:26
 * @desc AppVersion
 */

package com.feitai.admin.backend.config.web;
import com.alibaba.fastjson.JSON;
import com.feitai.admin.backend.config.service.AppVersionService;
import com.feitai.admin.core.service.DynamitSupportService;
import com.feitai.admin.core.web.BaseCrudController;
import com.feitai.jieya.server.dao.appconfig.model.AppVersion;
import lombok.extern.slf4j.Slf4j;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping(value = "/backend/appVersion")
@Slf4j
public class AppVersionController extends BaseCrudController<AppVersion> {

	@Autowired
	private AppVersionService appVersionService;
	
	@RequestMapping(value = "index")
	@RequiresPermissions("/backend/appVersion:list")
	public String index(Model model) {
		List<AppVersion> appVersions = appVersionService.findAll();
		Map<String,String> osTypeMap = new HashMap<>();
		Map<String,String> versionMap = new HashMap<>();
		Map<String,String> versionNumMap = new HashMap<>();
		for (AppVersion appVersion:appVersions){
			osTypeMap.put(appVersion.getOsType(),appVersion.getOsType());
			versionMap.put(appVersion.getVersion(),appVersion.getVersion());
			versionNumMap.put(appVersion.getVersionNum().toString(),appVersion.getVersionNum().toString());
		}
		String osTypes = JSON.toJSONString(osTypeMap);
		String versions = JSON.toJSONString(versionMap);
		String versionNums = JSON.toJSONString(versionNumMap);

		model.addAttribute("osTypes",osTypes);
		model.addAttribute("versions",versions);
		model.addAttribute("versionNums",versionNums);
		return "/backend/appVersion/index";
	}
	

	@Override
	protected DynamitSupportService<AppVersion> getService() {
		return this.appVersionService;
	}

	protected String getSingleSql(String typeCode){
		return null;
	}

}
