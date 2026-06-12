package com.gk.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * RSA加密工具类
 *
 * @author Flame
 * @date 2020-06-03 10:27
 **/
@Slf4j
public class RsaUtils {

    private RsaUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 加密算法RSA
     */
    private static final String CRYPTO_TYPE = "RSA";

    /**
     * 获取公钥的key
     */
    private static final String PUBLIC_KEY = "RSAPublicKey";

    /**
     * 获取私钥的key
     */
    private static final String PRIVATE_KEY = "RSAPrivateKey";

    /**
     * 钥匙对Map
     */
    private static final Map<String, Key> RSA_KEY_MAP = genKeyPair();

    /**
     * 生成公私钥
     */
    public static Map<String, Key> genKeyPair() {
        Map<String, Key> keyMap = new HashMap<>(2);
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(CRYPTO_TYPE);
            keyPairGenerator.initialize(2048);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            keyMap.put(PUBLIC_KEY, keyPair.getPublic());
            keyMap.put(PRIVATE_KEY, keyPair.getPrivate());
            return keyMap;
        } catch (Exception e) {
            log.error("RSA Gen Key Pair Error: {}", e.getMessage());
            return keyMap;
        }
    }

    /**
     * Base64字符串通过私钥解密
     *
     * @param base64String 被加密的Base64字符串
     * @param privateKey 私钥
     * @return 解密后的原文
     */
    public static String decryptBase64(String base64String, String privateKey) {
        byte[] sourceBytes = Base64.decodeBase64(base64String.getBytes(StandardCharsets.UTF_8));
        byte[] decryptBytes = decryptByPrivateKey(sourceBytes, privateKey);
        return new String(decryptBytes, StandardCharsets.UTF_8);
    }

    /**
     * 十六进制字符串通过私钥解密
     *
     * @param hexString 被加密的十六进制字符串
     * @param privateKey 私钥
     * @return 解密后的原文
     */
    public static String decryptHex(String hexString, String privateKey) {
        byte[] sourceBytes = StringExtUtils.toByteArray(hexString);
        byte[] decryptBytes = decryptByPrivateKey(sourceBytes, privateKey);
        return new String(decryptBytes, StandardCharsets.UTF_8);
    }

    /**
     * 通过私钥解密
     *
     * @param bytes 密文的byte数组
     * @param privateKey 私钥
     * @return 解密后原文的byte数组
     */
    public static byte[] decryptByPrivateKey(byte[] bytes, String privateKey) {
        try {
            byte[] encodedKey = Base64.decodeBase64(privateKey.getBytes(StandardCharsets.UTF_8));
            PKCS8EncodedKeySpec encodedKeySpec = new PKCS8EncodedKeySpec(encodedKey);
            RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) KeyFactory.getInstance(CRYPTO_TYPE).generatePrivate(encodedKeySpec);
            Cipher cipher = Cipher.getInstance(CRYPTO_TYPE);
            cipher.init(Cipher.DECRYPT_MODE, rsaPrivateKey);
            return cipher.doFinal(bytes);
        } catch (Exception e) {
            log.error("RSA Decrypt by privateKey({}) error: {}", privateKey, e.getMessage());
            return new byte[0];
        }
    }

    /**
     * 获取私钥
     */
    public static String getPrivateKey() {
        Key key = RSA_KEY_MAP.get(PRIVATE_KEY);
        return getKey(key);
    }

    /**
     * 获取公钥
     */
    public static String getPublicKey() {
        Key key = RSA_KEY_MAP.get(PUBLIC_KEY);
        return getKey(key);
    }

    private static String getKey(Key key) {
        return Base64.encodeBase64String(key.getEncoded());
    }
}
