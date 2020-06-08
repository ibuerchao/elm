package com.buerc.common.utils;

import com.buerc.common.exception.BizException;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class JSONUtil {

    private static ObjectMapper mapper = new ObjectMapper();

    static {
        //default config
        //忽略空值
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        //忽略未知的字段
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        //允许非标准的JSON, 就是KEY VALUE不带引号的JSON串
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        //时区设置为中国
        mapper.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        //小数类型反序列化成BIGDECIMAL
        mapper.configure(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS, true);
        //反序列化遇到匹配不到的枚举类型时使用空值
        mapper.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL,true);
        //整型类型统一反序列化成Long
        mapper.configure(DeserializationFeature.USE_LONG_FOR_INTS, true);
    }

    public static <T> T toObject(String content, Class<T> valueType) {
        try {
            return mapper.readValue(content, valueType);
        } catch (Exception e) {
            throw new BizException("transform json string into object failed", e);
        }
    }

    public static <T> T toObject(byte[] content, Class<T> valueType) {
        try {
            return mapper.readValue(content, valueType);
        } catch (Exception e) {
            throw new BizException("transform bytes into object failed", e);
        }
    }

    public static <T> T toObject(InputStream content, Class<T> valueType) {
        try {
            return mapper.readValue(content, valueType);
        } catch (Exception e) {
            throw new BizException("read object from json input stream failed", e);
        }
    }

    public static <T> T toObject(String content, TypeReference<T> valueType) {
        try {
            return mapper.readValue(content, valueType);
        } catch (Exception e) {
            throw new BizException("transform json string into object failed", e);
        }
    }

    public static <T> T toObject(byte[] content, TypeReference<T> valueType) {
        try {
            return mapper.readValue(content, valueType);
        } catch (Exception e) {
            throw new BizException("transform bytes into object failed", e);
        }
    }

    public static <T> T toObject(InputStream content, TypeReference<T> valueType) {
        try {
            return mapper.readValue(content, valueType);
        } catch (Exception e) {
            throw new BizException("read object from json input stream failed", e);
        }
    }

    public static String toStr(Object object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (Exception e) {
            throw new BizException("transform object into json string failed", e);
        }
    }

    public static byte[] toBytes(Object object) {
        try {
            return mapper.writeValueAsBytes(object);
        } catch (Exception e) {
            throw new BizException("transform object into bytes failed", e);
        }
    }

    public static void toOutputStream(Object object, OutputStream os) {
        try {
            mapper.writeValue(os, object);
        } catch (Exception e) {
            throw new BizException("write object to json output stream failed", e);
        }
    }
}
