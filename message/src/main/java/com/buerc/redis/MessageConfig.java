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
    RedisMessageListenerContainer container(RedisConnectionFactory factory, ApplicationContext applicationContext){
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        MessageProcessor messageProcessor = new MessageProcessor(applicationContext);
        container.setConnectionFactory(factory);
        container.addMessageListener(messageProcessor,new ChannelTopic(topic));
        return container;
    }

    @Bean
    MessagePublish messagePublish(StringRedisTemplate stringRedisTemplate){
        return new MessagePublish(stringRedisTemplate,topic);
    }
}
