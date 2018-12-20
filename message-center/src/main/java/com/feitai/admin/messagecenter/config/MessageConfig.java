package com.feitai.admin.messagecenter.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class MessageConfig {
    @Value("${messagecenter.server.url}")
    private String messagecenterServerUrl;

}
