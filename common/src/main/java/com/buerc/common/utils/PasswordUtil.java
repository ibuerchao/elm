package com.buerc.common.utils;

import com.buerc.common.constants.ResultCode;
import com.buerc.common.exception.BizException;
import org.apache.commons.lang3.StringUtils;
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
    /**
     * 必须由大小写字母、数字、特殊符号的8位以上组成
     * 1.^匹配开头
     * 2.(?![A-Za-z0-9]+$)匹配后面不全是（大写字母或小写字母或数字）的位置，排除了（大写字母、小写字母、数字）的1种2种3种组合
     * 3.(?![a-z0-9\\W]+$)同理，排除了（小写字母、数字、特殊符号）的1种2种3种组合
     * 4.(?![A-Za-z\\W]+$)同理，排除了（大写字母、小写字母、特殊符号）的1种2种3种组合
     * 5.(?![A-Z0-9\\W]+$)同理，排除了（大写字母、数组、特殊符号）的1种2种3种组合
     * 6.[a-zA-Z0-9\\W]匹配（小写字母或大写字母或数字或特殊符号）因为排除了上面的组合，所以就只剩下了4种都包含的组合了
     * 7.{8,}8位以上
     * 8.$匹配字符串结尾
     */
    private static final String REGEX = "^(?![A-Za-z0-9]+$)(?![a-z0-9\\W]+$)(?![A-Za-z\\W]+$)(?![A-Z0-9\\W]+$)[a-zA-Z0-9\\W]{8,}$";

    /**
     * 获取盐
     * @param str 明文字符串
     * @return 盐
     */
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

    /**
     * 加密字符串
     * @param str 明文字符串
     * @return 加密后的字符串
     */
    public static String encrypt(String str) {
        String salt = getSalt(str);
        String encrypt = encrypt(salt, str);
        return SPLIT + salt + SPLIT +encrypt;
    }

    /**
     *
     * @param salt 盐
     * @param str 字符串
     * @return 加密后的密码
     */
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

    /**
     * 验证文本是否与加密后的字符串匹配
     * @param text 文本
     * @param encrypt 加密后的字符串
     * @return true匹配 false不匹配
     */
    public static boolean check(String text, String encrypt) {
        Pair<String, String> pair = split(encrypt);
        String password = encrypt(pair.getLeft(), text);
        return password.equals(pair.getRight());
    }

    /**
     * 分割出盐和加密后的字符串
     * @param encrypt 加密字符串
     * @return left:盐 right:加密字符串
     */
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

    /**
     * 验证密码是否符合规则
     * @param password 密码
     * @return
     */
    public static boolean match(String password){
        return StringUtils.isNotBlank(password) && password.matches(REGEX);
    }
}
