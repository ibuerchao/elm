package com.buerc.common.utils;

import com.buerc.common.constants.ResultCode;
import com.buerc.common.exception.BizException;
import org.apache.commons.lang3.tuple.Pair;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Base64;

public class PasswordUtil {
    private static final String ALGORITHM_MD5 = "MD5";
    private static final String ALGORITHM_HMAC = "HmacSHA512";
    private static final String SPLIT = "$";

    private static String getSalt(String str) {
        try {
            MessageDigest md5 = MessageDigest.getInstance(ALGORITHM_MD5);
            str = str.concat(String.valueOf(Instant.now().toEpochMilli()));
            md5.update(str.getBytes(StandardCharsets.UTF_8));
            return new String(Base64.getEncoder().encode(md5.digest()));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new BizException(ResultCode.INTERNAL_ERROR_CODE, e.getMessage());
        }
    }

    public static String encrypt(String str) {
        String salt = getSalt(str);
        String encrypt = encrypt(salt, str);
        return SPLIT + salt + SPLIT +encrypt;
    }

    private static String encrypt(String salt, String str) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(salt.getBytes(StandardCharsets.UTF_8), ALGORITHM_HMAC);
            Mac mac = Mac.getInstance(ALGORITHM_HMAC);
            mac.init(keySpec);
            mac.update(str.getBytes(StandardCharsets.UTF_8));
            return new String(Base64.getEncoder().encode(mac.doFinal()));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BizException(ResultCode.INTERNAL_ERROR_CODE, e.getMessage());
        }
    }

    public static boolean check(String text, String encrypt) {
        Pair<String, String> pair = split(encrypt);
        String password = encrypt(pair.getLeft(), text);
        return password.equals(pair.getRight());
    }

    private static Pair<String,String> split(String encrypt){
        int start = encrypt.indexOf(SPLIT);
        int end = encrypt.lastIndexOf(SPLIT);
        if (start!=0){
            throw new BizException(ResultCode.PARAM_ERROR_CODE,ResultCode.PASSWORD_INVALID_MSG);
        }
        if (start == end){
            throw new BizException(ResultCode.PARAM_ERROR_CODE,ResultCode.PASSWORD_INVALID_MSG);
        }
        String salt = encrypt.substring(start+SPLIT.length(), end);
        if (salt.contains(SPLIT)){
            throw new BizException(ResultCode.PARAM_ERROR_CODE,ResultCode.PASSWORD_INVALID_MSG);
        }
        String password = encrypt.substring(end + SPLIT.length());
        return Pair.of(salt,password);
    }
}
