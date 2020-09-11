package com.buerc.permission.controller;

import com.buerc.CodeUtil;
import com.buerc.common.web.Result;
import com.buerc.redis.Event;
import com.buerc.redis.MessageProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/test")
public class TestController {

    @Resource
    private MessageProcessor messageProcessor;

    @Bean
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(100);
        executor.initialize();
        return executor;
    }


    @RequestMapping("/code")
    public Result code(){
        String test = CodeUtil.getCode("test");
        log.info(test);
        return Result.success(test);
    }

    @RequestMapping("/redis")
    public Result redis(){
        Event<Map<String,Object>> event = new Event<>();
        event.setTopic(messageProcessor.getTopic());
        event.setModule(1);
        event.setType(2);
        Map<String,Object> map = new HashMap<>();
        map.put("key1","value1");
        map.put("key2","value3");
        map.put("key3","value3");

        event.setData(map);
        messageProcessor.publish(event);
        return Result.success();
    }
}
