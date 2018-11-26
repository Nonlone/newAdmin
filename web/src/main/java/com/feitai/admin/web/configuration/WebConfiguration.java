package com.feitai.admin.web.configuration;

import com.alibaba.druid.support.monitor.MonitorServlet;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.feitai.base.json.filter.KeyFilter;
import com.feitai.utils.Desensitization;
import com.google.code.kaptcha.servlet.KaptchaServlet;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {

        //转换消息对象
        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
        //fastjson配置信息
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        // 脱敏映射Map
        Map<String, KeyFilter.KeyValueHandler> hyposensitizeHandlerMap = new HashMap<>();
        // 身份证脱敏
        hyposensitizeHandlerMap.put("idCard", (v) -> {
            if (v instanceof String && StringUtils.isNotBlank((String) v)) {
                return Desensitization.idCard((String) v);
            }
            return v;
        });

        //手机脱敏
        hyposensitizeHandlerMap.put("phone", (v) -> {
            if (v instanceof String && StringUtils.isNotBlank((String) v)) {
                return Desensitization.phone((String) v);
            }
            return v;
        });

        //收款银行脱敏
        hyposensitizeHandlerMap.put("bankCardNo", (v) -> {
            if (v instanceof String && StringUtils.isNotBlank((String) v)) {
                return Desensitization.bankCardNo((String) v);
            }
            return v;
        });

        //序列化时把Long与BigInteger类型转String类型，防止精度丢失
        SerializeConfig serializeConfig = new SerializeConfig();
        serializeConfig.put(BigInteger.class, ToStringSerializer.instance);
        serializeConfig.put(Long.class, ToStringSerializer.instance);
        serializeConfig.put(Long.TYPE, ToStringSerializer.instance);
        //serializeConfig.put(Date.class, DateSerializer.instance);
        fastJsonConfig.setSerializeConfig(serializeConfig);

        fastJsonConfig.setSerializeFilters(new KeyFilter(hyposensitizeHandlerMap));
        fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat);
        fastConverter.setFastJsonConfig(fastJsonConfig);
        converters.add(fastConverter);
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
    @Override
    protected void addFormatters(FormatterRegistry registry) {
    	registry.addConverter(new Converter<String, Date>() {

			@Override
			public Date convert(String source) {
				if(com.feitai.utils.StringUtils.isEmpty(source)){
					return null;
				}
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				try {
					return sdf.parse(source);
				} catch (ParseException e) {
					return new Date(Long.valueOf(source));
				}
			}
		});
    	
    }
}


