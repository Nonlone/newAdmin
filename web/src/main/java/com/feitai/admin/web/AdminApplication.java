package com.feitai.admin.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

@MapperScan(basePackages = {
        "com.feitai.jieya.server.dao.**.mapper",
        "com.feitai.admin.**.mapper"})
@ComponentScan(basePackages = {
        "com.feitai.admin.system",
        "com.feitai.admin.web",
        "com.feitai.admin.core"})
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class,})
public class AdminApplication {

    public static void main(String[] args) {
        //设置logback日志目录
        System.setProperty("APP_NAME", "admin");
        SpringApplication.run(AdminApplication.class, args);
    }

}



