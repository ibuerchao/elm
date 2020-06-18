package com.buerc.permission.config;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan({"com.buerc.permission.mapper"})
@Slf4j
public class MyBatisConfig {

    @Bean
    public AutoFillInterceptor autoFillInterceptor() {
        return new AutoFillInterceptor();
    }
}
