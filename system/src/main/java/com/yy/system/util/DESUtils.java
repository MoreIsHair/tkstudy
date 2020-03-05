package com.yy.system.util;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

/**
 * Created by Lin on 2019/8/8.
 */
@Slf4j
public class DESUtils {
    /**
     * DES是加密方式 CBC是工作模式 PKCS7Padding是填充模式
     */
    private final static String TRANSFORMATION = "DES/CBC/PKCS7Padding";
    /**
     * 默认为 DESede/ECB/PKCS5Padding
     */
    private static final String CIPHER_TRANSFORMAT7 = "DESede/ECB/PKCS7Padding";

    /**
     * DES是加密方式
     */
    private final static String ALGORITHM = "DES";

    private static final String ALGORITHM2 = "DESede";

    private static final String ENCODING = "UTF-8";

    /**
     * 3DESECB加密,key必须是长度大于等于 3*8 = 24 位
     * @param src
     * @param key
     * @return
     */
    public static String encryptThreeDESECB(final String src, final String key) {
        try {
            // 设置密钥参数
            final DESedeKeySpec dks = new DESedeKeySpec(key.getBytes(ENCODING));
            final SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM2);
            // 得到密钥对象
            final SecretKey secretKey = keyFactory.generateSecret(dks);
            final Cipher cipher = Cipher.getInstance(CIPHER_TRANSFORMAT7);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            final byte[] b = cipher.doFinal(src.getBytes());
            return Base64.getEncoder().encodeToString(b).replaceAll("\r", "").replaceAll("\n", "");
        } catch (Exception e) {
            log.error(e.getMessage());
            return "错误";
        }
    }

    /**
     * DESECB解密,key必须是长度大于等于 3*8 = 24 位
     *
     * @param src
     * @param key
     * @return
     */
    public static String decryptThreeDESECB(final String src, final String key) {
        try {
            final byte[] bytesrc = Base64.getDecoder().decode(src);
            // --解密的key
            final DESedeKeySpec dks = new DESedeKeySpec(key.getBytes(ENCODING));
            final SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM2);
            final SecretKey secretKey = keyFactory.generateSecret(dks);

            // --Chipher对象解密
            final Cipher cipher = Cipher.getInstance(CIPHER_TRANSFORMAT7);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            final byte[] retByte = cipher.doFinal(bytesrc);
            return new String(retByte);
        } catch (Exception e) {
            log.error(e.getMessage());
            return "错误";
        }

    }


    public static String encryptToBase64(String plainText, String key) {
        try {
            SecretKey deskey = new SecretKeySpec(key.getBytes(), ALGORITHM2);
            Cipher c1 = Cipher.getInstance(CIPHER_TRANSFORMAT7);
            c1.init(Cipher.ENCRYPT_MODE, deskey);
            byte[] result = c1.doFinal(plainText.getBytes(ENCODING));
            return Base64.getEncoder().encodeToString(result);
        } catch (Exception e) {
            log.error(e.getMessage());
            return "错误";
        }
    }
    public static String decryptFromBase64(String base64, String key) {
        try {
            SecretKey deskey = new SecretKeySpec(key.getBytes(), ALGORITHM2);
            Cipher c1 = Cipher.getInstance(CIPHER_TRANSFORMAT7);
            c1.init(Cipher.DECRYPT_MODE, deskey);
            byte[] result = c1.doFinal(Base64.getDecoder().decode(base64));
            return new String(result, ENCODING);
        } catch (Exception e) {
            log.error(e.getMessage());
            return "错误";
        }
    }

}
