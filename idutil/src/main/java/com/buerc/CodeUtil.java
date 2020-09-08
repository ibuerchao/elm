package com.buerc;

import com.buerc.common.constants.ResultCode;
import com.buerc.common.exception.BizException;
import com.buerc.common.utils.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.security.SecureRandom;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class CodeUtil {

    private static final SecureRandom random = new SecureRandom();

    private static final String INIT_KEY_SUFFIX = "-init";
    private static final long INIT_KEY_TTL = 60000L;

    //+的位置填充特征值
    private static final char EIGENVALUE_CHAR = '+';
    //_的位置使用随机数填充
    private static final char RANDOM_CHAR = '_';

    private static final Map<String, Rule> ruleCache = new ConcurrentHashMap<>();
    private static final Map<String, Config> configCache = new ConcurrentHashMap<>();

    public static String getCode(String key, String... eigenvalue) {
        if (StringUtils.isBlank(key)) {
            throw new BizException(ResultCode.PARAM_ERROR_CODE, "illegal char sequence");
        }
        Config config = configCache.get(key);
        if (config == null){
            throw new BizException(ResultCode.PARAM_ERROR_CODE, "configuration is not initialized");
        }
        Rule rule = getRuleFromCache(config.getPattern(), eigenvalue);
        long seq = getNextSeq(key,config,rule);
        String str = StringUtils.leftPad(String.valueOf(seq), rule.getCount(), '0');
        StringBuilder sb = new StringBuilder();
        int[] r = rule.getRule();

        for (int i = 0, j = 0; i < r.length; i++) {
            if (r[i] == -1) {
                sb.append(random.nextInt(10));
            } else if (r[i] == -2) {
                sb.append(eigenvalue[j]);
                j++;
            } else {
                sb.append(str.charAt(r[i]));
            }
        }
        return sb.toString();
    }

    private static synchronized long getNextSeq(String key,Config config,Rule rule) {
        long seq = getSeq(key);
        return doCycle(seq,config,rule);
    }

    private static long getSeq(String key){
        Config config = configCache.get(key);
        if (config == null) {
            throw new BizException(ResultCode.PARAM_ERROR_CODE, "configuration is not initialized");
        }
        long left = config.getLeft();
        long currentValue = config.getCurrentValue();
        if (left > config.getLeftSize()) {
            config.setLeft(left-1);
            config.setCurrentValue(currentValue+1);
            return currentValue+1;
        }
        if (config.getNextValue() == -1) {
            config.setNextValue(RedisUtil.increment(key,config.getCacheSize()));
        }

        if (left > 0) {
            config.setLeft(left-1);
            config.setCurrentValue(currentValue+1);
            return currentValue+1;
        }
        config.setLeft(config.getCacheSize() - 1);
        long nextValue = config.getNextValue()+1;
        config.setCurrentValue(nextValue);
        config.setNextValue(-1);
        return nextValue;
    }

    private static long doCycle(long seq,Config config,Rule rule){
        if (seq <= rule.getMax()){
            return seq;
        }else{
            if (config.isCycle()){
                String key = config.getKey();
                String value = UUID.randomUUID().toString();
                String initKey = key + INIT_KEY_SUFFIX;
                Boolean lock = Boolean.FALSE;
                boolean reset = false;
                while (!lock){
                    lock = RedisUtil.getLock(initKey, value, INIT_KEY_TTL);
                    if (lock) {
                        String s = RedisUtil.get(config.getKey());
                        if (NumberUtils.toLong(s)>rule.getMax()){
                            RedisUtil.set(config.getKey(),"0");
                            reset = true;
                        }
                        RedisUtil.delLock(initKey, value);
                    }
                }
                if (reset){
                    config.setCurrentValue(1L);
                    config.setLeft(config.getCacheSize()-1);
                    config.setNextValue(-1L);
                    return 1L;
                }else{
                    Long l = RedisUtil.increment(config.getKey(), config.getCacheSize());
                    config.setCurrentValue(l+1);
                    config.setLeft(config.getCacheSize()-1);
                    config.setNextValue(-1L);
                    return l+1;
                }
            }else{
                throw new BizException(ResultCode.PARAM_ERROR_CODE, "exceeds the maximum allowable value");
            }
        }
    }

    private static Rule getRuleFromCache(String pattern, String... eigenvalue) {
        Rule rule = ruleCache.get(pattern);
        if (rule == null) {
            LegalCharSetUtil.check(pattern);
            rule = resolvePattern(pattern);
            if (rule.getNum() != eigenvalue.length) {
                throw new BizException(ResultCode.PARAM_ERROR_CODE, "illegal char sequence");
            }
            ruleCache.put(pattern, rule);
        }
        return rule;
    }

    private static Rule resolvePattern(String pattern) {
        int[] rule = new int[pattern.length()];
        int count = 0;
        int num = 0;
        for (int i = 0; i < pattern.length(); i++) {
            char c = pattern.charAt(i);
            if (c == RANDOM_CHAR) {
                rule[i] = -1;
            } else if (c == EIGENVALUE_CHAR) {
                rule[i] = -2;
                num++;
            } else {
                count++;
                if (c >= '1' && c <= '9') {
                    rule[i] = c - '1';
                } else {
                    rule[i] = c - '1' - 7;
                }
            }
        }
        return new Rule(rule, count, num);
    }

    public static long decode(String value, String pattern, String... eigenvalue) {
        if (StringUtils.isBlank(pattern)) {
            throw new BizException(ResultCode.PARAM_ERROR_CODE, "illegal char sequence");
        }
        Rule rule = getRuleFromCache(pattern, eigenvalue);

        int[] result = new int[rule.getCount()];
        int[] r = rule.getRule();
        for (int i = 0, j = 0; i < r.length; i++) {
            if (r[i] == -2) {
                if (i == 0) {
                    value = "_" + value.substring(eigenvalue[j].length());
                } else if (i == r.length - 1) {
                    value = value.substring(0, i) + "_";
                } else {
                    String s1 = value.substring(0, i);
                    String s2 = eigenvalue[j].replaceAll(eigenvalue[j], "_");
                    String s3 = value.substring(i + eigenvalue[j].length());
                    value = s1 + s2 + s3;
                }
                j++;
            }
        }
        for (int i = 0; i < r.length; i++) {
            if (r[i] != -1 && r[i] != -2) {
                result[r[i]] = value.charAt(i) - '0';
            }
        }

        return getSeq(result);
    }

    private static long getSeq(int[] array) {
        StringBuilder sb = new StringBuilder();
        for (int i : array) {
            sb.append(i);
        }
        return NumberUtils.toLong(sb.toString());
    }

    public static void initConfig(Config config) {
        String key = config.getKey();
        String value = UUID.randomUUID().toString();
        String initKey = key + INIT_KEY_SUFFIX;
        Boolean lock = Boolean.FALSE;
        while (!lock) {
            lock = RedisUtil.getLock(initKey, value, INIT_KEY_TTL);
            if (lock) {
                String s = RedisUtil.get(key);
                long currentStepValue;
                if (StringUtils.isBlank(s)) {
                    currentStepValue = 0;
                } else {
                    currentStepValue = NumberUtils.toLong(s) + config.getCacheSize();
                }
                RedisUtil.set(key, String.valueOf(currentStepValue));
                config.setCurrentValue(currentStepValue);
                configCache.put(key, config);
                RedisUtil.delLock(initKey, value);
            }
        }
    }
}
