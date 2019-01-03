package com.feitai.admin.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.DefaultBeanNameGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

@MapperScan(basePackages = {
        "com.feitai.jieya.**.mapper",
        "com.feitai.admin.**.mapper"})
@ComponentScan(basePackages = {
        "com.feitai.admin.backend",
        "com.feitai.admin.channel",
        "com.feitai.admin.wisdomTooth",
        "com.feitai.admin.system",
        "com.feitai.admin.web",
        "com.feitai.admin.messagecenter",
        "com.feitai.admin.core",
        "com.feitai.admin.mop"})
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class,})
@Slf4j
public class AdminApplication {

    public static void main(String[] args) throws Exception {
        //设置logback日志目录
        System.setProperty("APP_NAME", "admin");
        SpringApplication app = new SpringApplication(AdminApplication.class);
        app.setBeanNameGenerator(new DefaultBeanNameGenerator());
        app.run(args);

    }
}