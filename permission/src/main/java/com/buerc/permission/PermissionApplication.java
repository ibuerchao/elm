package com.buerc.permission;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.concurrent.ThreadPoolExecutor;

@ComponentScan("com.buerc")
@SpringBootApplication
@EnableAsync
@EnableTransactionManagement
public class PermissionApplication {

    @Bean("mailThreadPool")
    public ThreadPoolTaskExecutor mailThreadPool(){
        ThreadPoolTaskExecutor mailThreadPool = new ThreadPoolTaskExecutor();
        mailThreadPool.setCorePoolSize(5);
        mailThreadPool.setMaxPoolSize(10);
        mailThreadPool.setQueueCapacity(25);
        mailThreadPool.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        mailThreadPool.setThreadNamePrefix("mail-thread-");
        mailThreadPool.initialize();
        return mailThreadPool;
    }

    @Bean("logThreadPool")
    public ThreadPoolTaskExecutor logThreadPool(){
        ThreadPoolTaskExecutor logThreadPool = new ThreadPoolTaskExecutor();
        logThreadPool.setCorePoolSize(5);
        logThreadPool.setMaxPoolSize(10);
        logThreadPool.setQueueCapacity(25);
        logThreadPool.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        logThreadPool.setThreadNamePrefix("log-thread-");
        logThreadPool.initialize();
        return logThreadPool;
    }

    public static void main(String[] args) {
        SpringApplication.run(PermissionApplication.class, args);
    }

}
