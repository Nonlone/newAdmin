
package com.feitai.admin.system.shiro;

import com.feitai.admin.core.vo.ShiroUser;
import com.feitai.admin.system.model.Role;
import com.feitai.admin.system.model.User;
import com.feitai.admin.system.service.RoleService;
import com.feitai.admin.system.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.List;

@Slf4j
public class ShiroDbRealm extends AuthorizingRealm {


    @Autowired
    protected UserService userService;

    @Autowired
    protected RoleService roleService;

    /**
     * 认证回调函数,登录时调用.
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
        String username = token.getUsername();
        User user = userService.findByLoginName(username);
        if (user != null) {
            if ("disabled".equals(user.getStatus())) {
                throw new DisabledAccountException();
            }
            try {
                byte[] salt = Hex.decodeHex(user.getSalt());
                return new SimpleAuthenticationInfo(new ShiroUser(
                        user.getLoginName(), user.getName(), user.getId()),
                        user.getPassword(), ByteSource.Util.bytes(salt), getName());
            } catch (DecoderException e) {
                log.error(String.format("doGetAuthenticationInfo decodeHex error %s ", e.getMessage()), e);
                throw new AuthenticationException(e);
            }
        } else {
            return null;
        }
    }

    /**
     * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用.
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        ShiroUser shiroUser = (ShiroUser) principals.getPrimaryPrincipal();
        List<Role> roles = this.userService.findRolesByUserId(shiroUser.getId());
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        for (Role role : roles) {
            // 基于Role的权限信息
            info.addRole(role.getCode());
        }
        List<String> userPermissions = this.userService.findUserPermissions(shiroUser.getId());
        info.addStringPermissions(userPermissions);
        return info;
    }

    /**
     * 更新用户授权信息缓存.
     */
    public void clearCachedAuthorizationInfo(String principal) {
        SimplePrincipalCollection principals = new SimplePrincipalCollection(
                principal, getName());
        clearCachedAuthorizationInfo(principals);
    }

    /**
     * 清除所有用户授权信息缓存.
     */
    public void clearAllCachedAuthorizationInfo() {
        Cache<Object, AuthorizationInfo> cache = getAuthorizationCache();
        if (cache != null) {
            for (Object key : cache.keys()) {
                cache.remove(key);
            }
        }
    }

    /**
     * 设定Password校验的Hash算法与迭代次数.
     */
    @PostConstruct
    public void initCredentialsMatcher() {
        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher(UserService.HASH_ALGORITHM);
        matcher.setHashIterations(UserService.HASH_INTERATIONS);
        setCredentialsMatcher(matcher);
    }


}
