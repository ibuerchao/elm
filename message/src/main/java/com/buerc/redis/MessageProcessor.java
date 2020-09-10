package com.buerc.redis;

import com.buerc.common.utils.JSONUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.serializer.StringRedisSerializer;

public class MessageProcessor implements MessageListener {
    private StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();

    private ApplicationContext applicationContext;

    @Override
    public void onMessage(Message message, byte[] bytes) {
        String s = stringRedisSerializer.deserialize(message.getBody());
        Event event = JSONUtil.toObject(s, new TypeReference<Event>() {});
        applicationContext.publishEvent(event);
    }

    MessageProcessor(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
}
