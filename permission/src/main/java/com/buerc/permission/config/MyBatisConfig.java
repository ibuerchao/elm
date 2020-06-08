package com.buerc.permission.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan({"com.buerc.permission.mapper"})
public class MyBatisConfig {
}
