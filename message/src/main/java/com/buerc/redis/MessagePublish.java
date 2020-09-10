package com.buerc.redis;

import com.buerc.common.utils.JSONUtil;
import org.springframework.data.redis.core.StringRedisTemplate;

public class MessagePublish {
    private StringRedisTemplate stringRedisTemplate;
    private String channel;

    public void publish(Event event){
        stringRedisTemplate.convertAndSend(channel, JSONUtil.toStr(event));
    }

    public MessagePublish(StringRedisTemplate stringRedisTemplate, String channel) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.channel = channel;
    }
}
