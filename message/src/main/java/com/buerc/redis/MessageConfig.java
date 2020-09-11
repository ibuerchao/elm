package com.buerc.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

@Component
public class MessageConfig {

    @Value("${redis.message.topic}")
    private String topic;

    @Bean
    RedisMessageListenerContainer container(RedisConnectionFactory factory, ApplicationContext context,StringRedisTemplate template){
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(factory);
        container.addMessageListener(messageProcessor(context,template),new ChannelTopic(topic));
        return container;
    }

    @Bean
    MessageProcessor messageProcessor(ApplicationContext context,StringRedisTemplate template){
        return new MessageProcessor(context, template, topic);
    }
}
