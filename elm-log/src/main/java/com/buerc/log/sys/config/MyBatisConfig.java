package com.buerc.log.sys.config;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan({"com.buerc.log.mapper"})
@Slf4j
public class MyBatisConfig {
}
