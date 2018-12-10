package com.feitai.admin.web.configuration;

import com.feitai.admin.system.shiro.FormAuthenticationCaptchaFilter;
import com.feitai.admin.system.shiro.ShiroDbRealm;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfiguration {

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Bean(name = "shiroRealmImpl")
    public ShiroDbRealm getShiroRealm(EhCacheManager shiroEhcacheManager) {
        ShiroDbRealm shiroRealmImpl = new ShiroDbRealm();
        shiroRealmImpl.setCacheManager(shiroEhcacheManager);
        return shiroRealmImpl;
    }

    /**
     * 用户授权信息Cache, 采用EhCache
     */
    @Bean(name = "shiroEhcacheManager")
    public EhCacheManager getEhCacheManager() {
        EhCacheManager em = new EhCacheManager();
        em.setCacheManagerConfigFile("classpath:ehcache-shiro.xml");
        return em;
    }

    @Bean(name = "lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    /**
     * AOP式方法级权限检查
     */
    @Bean
    public DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator daap = new DefaultAdvisorAutoProxyCreator();
        daap.setProxyTargetClass(true);
        return daap;
    }

    @Bean
    public SecurityManager getDefaultWebSecurityManager(ShiroDbRealm shiroRealmImpl) {
        DefaultWebSecurityManager dwsm = new DefaultWebSecurityManager();
        dwsm.setRealm(shiroRealmImpl);
        dwsm.setCacheManager(getEhCacheManager());
        return dwsm;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor getAuthorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor aasa = new AuthorizationAttributeSourceAdvisor();
        aasa.setSecurityManager(securityManager);
        return new AuthorizationAttributeSourceAdvisor();
    }

    @Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        shiroFilterFactoryBean.setLoginUrl(contextPath + "/login");
        shiroFilterFactoryBean.setSuccessUrl(contextPath + "/login/success");
        shiroFilterFactoryBean.setUnauthorizedUrl(contextPath + "/login/unlogin");
        //shiroFilterFactoryBean.setUnauthorizedUrl("/403");
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
        filterChainDefinitionMap.put(contextPath + "/static/**", "anon");
        filterChainDefinitionMap.put(contextPath + "/login", "authc");
        filterChainDefinitionMap.put(contextPath + "/logout", "logout");
        filterChainDefinitionMap.put(contextPath + "/monitor/druid/**", "authc,perms[\"/admin/monitor/druid:list\"]");
        ///admin/monitor/druid/** = authc,perms["/admin/monitor/druid:list"]

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        Map<String, Filter> filters = new HashMap<String, Filter>();
        filters.put("authc", getFormAuthenticationCaptchaFilter());
        shiroFilterFactoryBean.setFilters(filters);
        return shiroFilterFactoryBean;
    }

    /**
     * Extension FormAuthenticationCaptchaFilter
     */
    @Bean(name = "captchaFilter")
    public FormAuthenticationCaptchaFilter getFormAuthenticationCaptchaFilter() {
        FormAuthenticationCaptchaFilter filter = new FormAuthenticationCaptchaFilter(contextPath);
        return filter;
    }


}
