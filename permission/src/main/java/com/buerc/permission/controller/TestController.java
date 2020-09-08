package com.buerc.permission.controller;

import com.buerc.CodeUtil;
import com.buerc.common.web.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/test")
public class TestController {

    @Bean
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(100);
        executor.initialize();
        return executor;
    }


    @RequestMapping()
    public Result test(){
        String test = CodeUtil.getCode("test");
        log.info(test);
        return Result.success(test);
    }

}
