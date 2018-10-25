package com.feitai.admin.web.configuration;

import com.alibaba.druid.support.monitor.MonitorServlet;
import com.google.code.kaptcha.servlet.KaptchaServlet;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.util.Properties;

/**
 * web 配置类
 */
@Configuration
public class WebConfiguration extends WebMvcConfigurationSupport {

    /**
     * 设置默认页
     *
     * @param registry
     */
    @Override
    protected void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("/login");
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
        super.addViewControllers(registry);
    }


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        super.addResourceHandlers(registry);
    }

    /**
     * 验证码输出
     *
     * @return
     */
    @Bean
    public ServletRegistrationBean kaptchaServlet() {
        ServletRegistrationBean registrationBean = new ServletRegistrationBean(new KaptchaServlet(), "/kaptcha");
        registrationBean.setLoadOnStartup(1);
        return registrationBean;
    }

    /**
     * Druid 监控输出
     *
     * @return
     */
    @Bean
    public ServletRegistrationBean druidMonitorServlet() {
        ServletRegistrationBean registrationBean = new ServletRegistrationBean(new MonitorServlet(), "/monitor/druid");
        registrationBean.setLoadOnStartup(1);
        return registrationBean;
    }


    /**
     * 配置JSP视图解析器
     *
     * @return
     */
    @Bean
    public ViewResolver viewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/template/");
        resolver.setSuffix(".jsp");
        resolver.setExposeContextBeansAsAttributes(true);
        return resolver;
    }


    /***
     * 加载配置文件
     * @return
     */
    @Bean(name = "webConf")
    public PropertiesFactoryBean webConf() {
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        Resource resource = new ClassPathResource("webConf.properties");
        propertiesFactoryBean.setLocation(resource);
        propertiesFactoryBean.setFileEncoding("utf-8");
        return propertiesFactoryBean;
    }


    /**
     * 将Controller抛出的异常转到特定View, 保持SiteMesh的装饰效果
     */
    @Bean
    public SimpleMappingExceptionResolver getSimpleMappingExceptionResolver() {
        SimpleMappingExceptionResolver resolver = new SimpleMappingExceptionResolver();
        Properties exceptionMappings = new Properties();
        exceptionMappings.setProperty("org.apache.shiro.authz.UnauthorizedException", "login");
        exceptionMappings.setProperty("org.apache.shiro.authz.UnauthenticatedException", "login");
        exceptionMappings.setProperty("java.lang.Throwable", "error/500");
        resolver.setExceptionMappings(exceptionMappings);
        return resolver;
    }
}
