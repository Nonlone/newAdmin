package com.feitai.admin.web;

import com.feitai.admin.system.model.User;
import com.feitai.utils.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.mapper.entity.EntityTable;
import tk.mybatis.mapper.mapperhelper.EntityHelper;
import tk.mybatis.spring.annotation.MapperScan;

import java.lang.reflect.Field;
import java.util.Map;

@MapperScan(basePackages = {
        "com.feitai.jieya.**.mapper",
        "com.feitai.admin.**.mapper"})
@ComponentScan(basePackages = {
        "com.feitai.admin.backend",
//        "com.feitai.jieya",
        "com.feitai.admin.system",
        "com.feitai.admin.web",
        "com.feitai.admin.core"})
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class,})
@Slf4j
public class AdminApplication {

    public static void main(String[] args) throws Exception {
        //设置logback日志目录
        System.setProperty("APP_NAME", "admin");
        SpringApplication.run(AdminApplication.class, args);

    }

}



