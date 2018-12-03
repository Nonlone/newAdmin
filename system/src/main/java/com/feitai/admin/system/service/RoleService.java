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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class RoleService extends DynamitSupportService<Role> {

    @Autowired
    private RoleAuthMapper roleAuthMapper;

    /**
     * 给角色授权
     *
     * @param role 从前台传进来的只有Role的ID和roleAuth，没有Role基本信息
     * @return
     */
    public boolean authRole(Role role) {
        Role dbRole = findOne(role.getId());
        walkProcess(dbRole);
        List<RoleAuth> ras = dbRole.getRoleAuths();
        for (RoleAuth roleAuth :
                ras) {
            this.roleAuthMapper.delete(roleAuth);
        }
        dbRole.setRoleAuths(role.getRoleAuths());
        this.updateByPrimaryKey(dbRole);
        for (RoleAuth roleAuth : role.getRoleAuths()) {
            this.roleAuthMapper.insert(roleAuth);
        }
        RealmSecurityManager securityManager = (RealmSecurityManager) SecurityUtils
                .getSecurityManager();
        ShiroDbRealm shiroDbRealm = (ShiroDbRealm) securityManager.getRealms()
                .iterator().next();
        //清空所有的缓存
        shiroDbRealm.clearAllCachedAuthorizationInfo();
        return true;
    }

    @Override
    public Role findOne(Object id) {
        Role role = getMapper().selectByPrimaryKey(id);
        return walkProcess(role);
    }

}
