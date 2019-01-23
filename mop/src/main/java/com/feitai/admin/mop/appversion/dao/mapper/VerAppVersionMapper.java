package com.feitai.admin.mop.appversion.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.feitai.admin.mop.appversion.dao.entity.VerAppVersion;

import tk.mybatis.mapper.common.Mapper;

public interface VerAppVersionMapper extends Mapper<VerAppVersion> {

	@Select({ "<script>", "SELECT distinct `app_version` FROM `t_ver_app_version` ORDER BY id DESC limit 1",
			"</script>" })
	List<String> queryAllVersions();
}