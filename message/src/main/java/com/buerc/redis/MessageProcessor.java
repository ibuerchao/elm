package com.buerc.redis;

import com.buerc.common.utils.JSONUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import javax.annotation.Resource;

public class MessageProcessor implements MessageListener {
    private StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();

    @Resource
    private EventProcessorProvider provider;

    private ApplicationContext applicationContext;
    private StringRedisTemplate stringRedisTemplate;
    private String topic;

    @Override
    public void onMessage(Message message, byte[] bytes) {
        String s = stringRedisSerializer.deserialize(message.getBody());
        Event event = JSONUtil.toObject(s, new TypeReference<Event>() {});
        applicationContext.publishEvent(event);
    }

    public void publish(Event event){
        stringRedisTemplate.convertAndSend(topic, JSONUtil.toStr(event));
    }

    @EventListener
    public void onEvent(Event event) {
        if (topic.equals(event.getTopic())){
            provider.process(event);
        }
    }

    MessageProcessor(ApplicationContext applicationContext,StringRedisTemplate stringRedisTemplate,String topic) {
        this.applicationContext = applicationContext;
        this.stringRedisTemplate = stringRedisTemplate;
        this.topic = topic;
    }

    public String getTopic() {
        return topic;
    }
}
