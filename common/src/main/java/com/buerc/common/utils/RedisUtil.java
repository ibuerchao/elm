package com.buerc.common.utils;

import com.buerc.common.constants.RedisConstant;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.TimeUnit;

public class RedisUtil {

    private static StringRedisTemplate get() {
        return ApplicationContextUtil.getApplicationContext().getBean(StringRedisTemplate.class);
    }

    public static String getKeyForToken(String key){
        return RedisConstant.USER_TOKEN+key;
    }

    public static void set(String key, String value) {
        get().opsForValue().set(key, value);
    }

    public static String get(String key){
        return get().opsForValue().get(key);
    }

    public static void set(String key, String value, long l, TimeUnit timeUnit) {
        get().opsForValue().set(key, value, l, timeUnit);
    }
}
