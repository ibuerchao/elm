package com.buerc.common.utils;

import com.buerc.common.constants.ResultCode;
import com.buerc.common.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Slf4j
public class RsaUtil {

    //定义加密方式
    private static final String KEY_RSA = "RSA";
    //定义签名算法
    private final static String KEY_RSA_SIGNATURE = "MD5withRSA";
    //RSA最大加密大小
    private final static int MAX_ENCRYPT_BLOCK = 117;
    //RSA最大解密大小
    private final static int MAX_DECRYPT_BLOCK = 128;
    //加密标识
    private final static Boolean ENCRYPT = Boolean.TRUE;
    //解密标识
    private final static Boolean DECRYPT = Boolean.FALSE;

    @Value("${rsa.publicKey}")
    private String publicKey;
    @Value("${rsa.privateKey}")
    private String privateKey;


//    public static void main(String[] args) throws NoSuchAlgorithmException {
//        KeyPairGenerator generator = KeyPairGenerator.getInstance(KEY_RSA);
//        //设置密钥对的bit数，越大越安全，但速度减慢，一般使用512或1024
//        generator.initialize(1024);
//        KeyPair keyPair = generator.generateKeyPair();
//        PublicKey aPublic = keyPair.getPublic();
//
//        System.out.println(new String(Base64.getEncoder().encode(aPublic.getEncoded())));
//        System.out.println(new String(Base64.getEncoder().encode(keyPair.getPrivate().getEncoded())));
//    }



    /**
     * BASE64 解码
     */
    private byte[] decryptBase64(String str) {
        return Base64.getDecoder().decode(str);
    }

    /**
     * BASE64 编码
     */
    private String encryptBase64(byte[] bytes) {
        return new String(Base64.getEncoder().encode(bytes));
    }

    /**
     * 将公钥由字符串转为UTF-8格式的字节数组
     */
    private byte[] getPublicKeyBytes() {
        return decryptBase64(publicKey);
    }

    /**
     * 将私钥由字符串转为UTF-8格式的字节数组
     */
    private byte[] getPrivateKeyBytes() {
        return decryptBase64(privateKey);
    }

    /**
     * 获取公钥加密Cipher
     */
    private Cipher getPublicEncryptCipher() throws NoSuchAlgorithmException, InvalidKeySpecException,
            NoSuchPaddingException, InvalidKeyException {
        byte[] bytes = getPublicKeyBytes();
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(bytes);
        KeyFactory factory = KeyFactory.getInstance(KEY_RSA);
        PublicKey publicKey = factory.generatePublic(keySpec);
        Cipher cipher = Cipher.getInstance(factory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher;
    }

    /**
     * 获取私钥加密Cipher
     */
    private Cipher getPrivateEncryptCipher() throws NoSuchAlgorithmException, InvalidKeySpecException,
            NoSuchPaddingException, InvalidKeyException {
        byte[] bytes = getPrivateKeyBytes();
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(bytes);
        KeyFactory factory = KeyFactory.getInstance(KEY_RSA);
        PrivateKey privateKey = factory.generatePrivate(keySpec);
        Cipher cipher = Cipher.getInstance(factory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        return cipher;
    }

    /**
     * 获取公钥解密Cipher
     */
    private Cipher getPublicDecryptCipher() throws NoSuchAlgorithmException, InvalidKeySpecException,
            NoSuchPaddingException, InvalidKeyException {
        byte[] bytes = getPublicKeyBytes();
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(bytes);
        KeyFactory factory = KeyFactory.getInstance(KEY_RSA);
        PublicKey publicKey = factory.generatePublic(keySpec);
        Cipher cipher = Cipher.getInstance(factory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        return cipher;
    }

    /**
     * 获取私钥解密Cipher
     */
    private Cipher getPrivateDecryptCipher() throws NoSuchAlgorithmException, InvalidKeySpecException,
            NoSuchPaddingException, InvalidKeyException {
        byte[] bytes = getPrivateKeyBytes();
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(bytes);
        KeyFactory factory = KeyFactory.getInstance(KEY_RSA);
        PrivateKey privateKey = factory.generatePrivate(keySpec);
        Cipher cipher = Cipher.getInstance(factory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher;
    }

    /**
     * 获取私钥Signature
     */
    private Signature getPrivateSignature() throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException {
        byte[] bytes = getPrivateKeyBytes();
        PKCS8EncodedKeySpec pkcs = new PKCS8EncodedKeySpec(bytes);
        KeyFactory factory = KeyFactory.getInstance(KEY_RSA);
        PrivateKey key = factory.generatePrivate(pkcs);
        Signature signature = Signature.getInstance(KEY_RSA_SIGNATURE);
        signature.initSign(key);
        return signature;
    }

    /**
     * 获取公钥Signature
     */
    private Signature getPublicSignature() throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException {
        byte[] bytes = getPublicKeyBytes();
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(bytes);
        KeyFactory factory = KeyFactory.getInstance(KEY_RSA);
        PublicKey key = factory.generatePublic(keySpec);
        Signature signature = Signature.getInstance(KEY_RSA_SIGNATURE);
        signature.initVerify(key);
        return signature;
    }

    private byte[] doFinal(Cipher cipher, byte[] data, boolean flag) throws IOException, BadPaddingException, IllegalBlockSizeException {
        int length = data.length;
        int block = flag ? MAX_ENCRYPT_BLOCK : MAX_DECRYPT_BLOCK;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] result;
        try {
            int offset = 0;
            byte[] cache;
            int i = 0;
            // 分段处理
            while (length - offset > 0) {
                if (length - offset > block) {
                    cache = cipher.doFinal(data, offset, block);
                } else {
                    cache = cipher.doFinal(data, offset, length - offset);
                }
                out.write(cache, 0, cache.length);
                i++;
                offset = i * block;
            }
            result = out.toByteArray();
        } finally {
            out.close();
        }
        return result;
    }

    /**
     * 公钥加密
     */
    public String encryptByPublicKey(String str) {
        try {
            byte[] data = str.getBytes(StandardCharsets.UTF_8);
            Cipher cipher = getPublicEncryptCipher();
            byte[] bytes = doFinal(cipher, data, ENCRYPT);
            return encryptBase64(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 私钥解密
     */
    public String decryptByPrivateKey(String str) {
        try {
            byte[] data = decryptBase64(str);
            Cipher cipher = getPrivateDecryptCipher();
            byte[] bytes = doFinal(cipher, data, DECRYPT);
            return new String(bytes, StandardCharsets.UTF_8);
        } catch (Exception e){
            log.error("私钥解密错误，{}",str,e);
            throw new BizException(ResultCode.INVALID_ENCRYPT_STR_CODE,ResultCode.INVALID_ENCRYPT_STR_MSG);
        }
    }

    /**
     * 私钥加密
     */
    public String encryptByPrivateKey(String str) {
        try {
            byte[] data = str.getBytes(StandardCharsets.UTF_8);
            Cipher cipher = getPrivateEncryptCipher();
            byte[] bytes = doFinal(cipher, data, ENCRYPT);
            return encryptBase64(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 公钥解密
     */
    public String decryptByPublicKey(String encryptedStr) {
        try {
            byte[] data = decryptBase64(encryptedStr);
            Cipher cipher = getPublicDecryptCipher();
            byte[] bytes = doFinal(cipher, data, DECRYPT);
            return new String(bytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 用私钥对数据进行签名
     */
    public String sign(String str) {
        String result = "";
        try {
            Signature signature = getPrivateSignature();
            signature.update(str.getBytes());
            result = encryptBase64(signature.sign());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 校验数字签名
     * @return 校验成功返回true，失败返回false
     */
    public boolean verify(String str, String sign) {
        boolean flag = false;
        try {
            Signature signature = getPublicSignature();
            signature.update(str.getBytes());
            flag = signature.verify(decryptBase64(sign));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }
}