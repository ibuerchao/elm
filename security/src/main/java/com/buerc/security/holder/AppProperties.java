package com.buerc.security.holder;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppProperties {
    @Value("${app.url.permission}")
    private String permissionServiceUrl;

    @Bean("restTemplate")
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

    public String getPermissionServiceUrl() {
        return permissionServiceUrl;
    }
}
