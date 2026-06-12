package com.gk.common.utils;

import cn.hutool.core.util.StrUtil;
import com.gk.common.enums.SysStatus;
import com.gk.common.model.exception.SysException;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * AES加密工具类
 *
 * @author GuoYu
 * @date 2020-03-10 10:27
 */

@Slf4j
public class AesUtils {

    private AesUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 加密算法AES
     */
    private static final String CRYPTO_TYPE = "AES";
    /**
     * 加密方式 CBC模式
     */
    private static final String CBC_CIPHER_TYPE = "AES/CBC/PKCS5Padding";
    /**
     * 加密方式 EBC模式
     */
    private static final String ECB_CIPHER_TYPE = "AES/ECB/PKCS5Padding";
    /**
     * 密钥及iv的长度
     */
    private static final int    KEY_LENGTH  = 16;

    /**
     * 加密
     *
     * @param sourceBytes 原文的byte数组
     * @param aesKey 密钥
     * @param aesIv iv
     * @return 密文的byte数组
     */
    public static byte[] encryptBytes(byte[] sourceBytes, String aesKey, String aesIv) {
        return aesCrypto(sourceBytes, aesKey, aesIv, Cipher.ENCRYPT_MODE);
    }

    /**
     * 加密
     *
     * @param sourceText 原文字符串
     * @param aesKey 密钥
     * @param aesIv iv
     * @return 密文的byte数组
     */
    public static byte[] encryptBytes(String sourceText, String aesKey, String aesIv) {
        byte[] sourceBytes = sourceText.getBytes(StandardCharsets.UTF_8);
        return encryptBytes(sourceBytes, aesKey, aesIv);
    }

    /**
     * 加密
     *
     * @param sourceBytes 原文的byte数组
     * @param aesKey 密钥
     * @param aesIv iv
     * @return 密文Hex字符串
     */
    public static String encrypt(byte[] sourceBytes, String aesKey, String aesIv) {
        return StringExtUtils.toHexString(encryptBytes(sourceBytes, aesKey, aesIv));
    }

    /**
     * 加密
     *
     * @param sourceText 原文字符串
     * @param aesKey 密钥
     * @param aesIv iv
     * @return 密文Hex字符串
     */
    public static String encrypt(String sourceText, String aesKey, String aesIv) {
        byte[] sourceBytes = sourceText.getBytes(StandardCharsets.UTF_8);
        return StringExtUtils.toHexString(encryptBytes(sourceBytes, aesKey, aesIv));
    }

    /**
     * 解密
     *
     * @param sourceBytes 密文的byte数组
     * @param aesKey 密钥
     * @param aesIv iv
     * @return 原文的byte数组
     */
    public static byte[] decryptBytes(byte[] sourceBytes, String aesKey, String aesIv) {
        return aesCrypto(sourceBytes, aesKey, aesIv, Cipher.DECRYPT_MODE);
    }

    /**
     * 解密
     *
     * @param hexString 密文Hex字符串
     * @param aesKey 密钥
     * @param aesIv iv
     * @return 原文的byte数组
     */
    public static byte[] decryptBytes(String hexString, String aesKey, String aesIv) {
        byte[] sourceBytes = StringExtUtils.toByteArray(hexString);
        return decryptBytes(sourceBytes, aesKey, aesIv);
    }

    /**
     * 解密
     *
     * @param sourceBytes 密文的byte数组
     * @param aesKey 密钥
     * @param aesIv iv
     * @return 原文字符串
     */
    public static String decrypt(byte[] sourceBytes, String aesKey, String aesIv) {
        return new String(decryptBytes(sourceBytes, aesKey, aesIv), StandardCharsets.UTF_8);
    }

    /**
     * 解密
     *
     * @param hexString 密文Hex字符串
     * @param aesKey 密钥
     * @param aesIv iv
     * @return 原文字符串
     */
    public static String decrypt(String hexString, String aesKey, String aesIv) {
        byte[] sourceBytes = StringExtUtils.toByteArray(hexString);
        return new String(decryptBytes(sourceBytes, aesKey, aesIv), StandardCharsets.UTF_8);
    }

    /**
     * 加解密通用方法
     *
     * @param sourceBytes byte数组
     * @param aesKey 密钥
     * @param aesIv iv
     * @param cipherMode 模式
     */
    private static byte[] aesCrypto(byte[] sourceBytes, String aesKey, String aesIv, int cipherMode) {
        try {
            // 判断Key是否正确
            if (aesKey == null || aesIv == null) {
                log.error("AES key or iv is null");
                throw new SysException(SysStatus.AES_KEY_IV_ERROR);
            }
            // 判断Key是否为16位
            if (aesKey.length() != KEY_LENGTH || aesIv.length() != KEY_LENGTH) {
                log.error("AES key or iv length is not 16");
                throw new SysException(SysStatus.AES_KEY_IV_ERROR);
            }
            SecretKeySpec secretKeySpec = new SecretKeySpec(aesKey.getBytes(), CRYPTO_TYPE);
            Cipher cipher = Cipher.getInstance(CBC_CIPHER_TYPE);
            if (StrUtil.isEmpty(aesIv)) {
                cipher.init(cipherMode, secretKeySpec);
            } else {
                IvParameterSpec iv = new IvParameterSpec(aesIv.getBytes(StandardCharsets.UTF_8));
                cipher.init(cipherMode, secretKeySpec, iv);
            }

            return cipher.doFinal(sourceBytes);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException
                | InvalidKeyException | InvalidAlgorithmParameterException
                | IllegalBlockSizeException | BadPaddingException e) {
            log.error("AES Crypto Error: {}", e.getMessage());
            if (Cipher.ENCRYPT_MODE == cipherMode) {
                throw new SysException(SysStatus.DATA_ENCRYPT_ERROR);
            } else {
                throw new SysException(SysStatus.DATA_DECRYPT_ERROR);
            }
        }
    }

}
