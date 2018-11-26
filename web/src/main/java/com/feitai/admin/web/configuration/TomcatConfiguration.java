package com.feitai.admin.web.configuration;

import org.apache.catalina.Context;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TomcatConfiguration {


    @Value("${server.docBase:''}")
    private String docBase;

    @Value("${server.port:8080}")
    private int port;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Bean
    @ConditionalOnProperty("server.docBase")
    public ServletWebServerFactory webServerFactory() {
        TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory() {
            @Override
            protected void configureContext(Context context, ServletContextInitializer[] initializers) {
                context.setDocBase(docBase);
                super.configureContext(context, initializers);
            }
        };
        return factory;
    }

}
