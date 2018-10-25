package com.feitai.admin.system.service;

import com.feitai.admin.core.service.DynamitSupportService;
import com.feitai.admin.system.model.Role;
import com.feitai.admin.system.model.RoleAuth;
import com.feitai.admin.system.mapper.RoleAuthMapper;
import com.feitai.admin.system.shiro.ShiroDbRealm;
import com.feitai.base.mybatis.ManyAnnotationFieldWalkProcessor;
import com.feitai.utils.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.RealmSecurityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Component
@Transactional
@Slf4j
public class RoleService extends DynamitSupportService<Role> {

	@Autowired
	private RoleAuthMapper roleAuthMapper;

	@Autowired
	private ApplicationContext applicationContext;

	/**
	 * 给角色授权
	 * @param role 从前台传进来的只有Role的ID和roleAuth，没有Role基本信息
	 * @return
	 */
	public boolean authRole(Role role){
		Role dbRole = findOne(role.getId());
		ManyAnnotationFieldWalkProcessor manyAnnotationFieldWalkProcessor = new ManyAnnotationFieldWalkProcessor(applicationContext);
		ObjectUtils.fieldWalkProcess(dbRole,manyAnnotationFieldWalkProcessor);
		List<RoleAuth> ras = dbRole.getRoleAuths();
		for (RoleAuth roleAuth:
		ras) {
			this.roleAuthMapper.delete(roleAuth);
		}
		dbRole.setRoleAuths(role.getRoleAuths());
		this.save(dbRole);
		RealmSecurityManager securityManager = (RealmSecurityManager) SecurityUtils
				.getSecurityManager();
		ShiroDbRealm shiroDbRealm = (ShiroDbRealm) securityManager.getRealms()
				.iterator().next();
		//清空所有的缓存
		shiroDbRealm.clearAllCachedAuthorizationInfo();
		
		return true;
	}
	
}
