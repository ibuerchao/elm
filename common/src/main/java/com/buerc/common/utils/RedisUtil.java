package com.buerc.common.utils;

import com.buerc.common.constants.RedisConstant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeUnit;

public class RedisUtil {
    private static final String UTF8 = "UTF-8";

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

    public static Boolean del(String key){
        return get().delete(key);
    }

    public static void set(String key, String value, long l, TimeUnit timeUnit) {
        get().opsForValue().set(key, value, l, timeUnit);
    }

    public static Long increment(String key,long value){
        return get().opsForValue().increment(key,value);
    }

    public static Boolean getLock(String key,String value,long timeout){
        return get().execute((RedisCallback<Boolean>) connection -> {
            Object r = connection.execute("set", getBytes(key),
                    getBytes(value), getBytes("NX"),
                    getBytes("PX"), getBytes(String.valueOf(timeout)));
            return r != null;
        });
    }

    public static void delLock(String key,String value){
        String s = get(key);
        if (StringUtils.isNotBlank(s) && s.equals(value)){
            get().delete(key);
        }
    }

    private static byte[] getBytes(String str){
        try {
            return StringUtils.getBytes(str,UTF8);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("get bytes of string failed");
        }
    }
}
