package com.whaleal.mars.util;

import com.whaleal.mars.core.internal.diagnostics.logging.LogFactory;
import com.whaleal.mars.core.internal.diagnostics.logging.Logger;


import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * 
 * @author  wh
 *
 *
 */
public class AESUtil {


    private  static Logger LOGGER = LogFactory.getLogger(AESUtil.class);


    // 加密
    public static String encrypt(String sSrc, String sKey) throws Exception {
        if (sKey == null) {
            LOGGER.error("Key is null");
            return null;
        }
        // 判断Key是否为16位
        if (sKey.length() != 16) {
            LOGGER.error("Key's Length is not match 16");
            return null;
        }
        byte[] raw = sKey.getBytes("utf-8");
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");//"算法/模式/补码方式"
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(sSrc.getBytes("utf-8"));
        return Base64Encoder.encode(encrypted);//此处使用BASE64做转码功能，同时能起到2次加密的作用。
    }

    // 解密
    public static String decrypt(String sSrc, String sKey) throws Exception {
        try {
            // 判断Key是否正确
            if (sKey == null) {
                LOGGER.error("Key is null");
                return null;
            }
            // 判断Key是否为16位
            if (sKey.length() != 16) {
                LOGGER.error("Key's Length is not match 16");
                return null;
            }
            byte[] raw = sKey.getBytes("utf-8");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            byte[] encrypted1 = Base64Decoder.decode(sSrc);//先用base64解密
            try {
                byte[] original = cipher.doFinal(encrypted1);
                String originalString = new String(original,"utf-8");
                return originalString;
            } catch (Exception e) {
                LOGGER.error(e.toString());
                return null;
            }
        } catch (Exception ex) {
            LOGGER.error(ex.toString());
            return null;
        }
    }




    public static void main(String[] args) throws Exception {
        String plaintext = "hello world";
        String key = "0123456789abcdef";

        String ciphertext = AESUtil.encrypt(plaintext, key);
        System.out.println("加密后的字符串：" + ciphertext);

        String decryptedText = AESUtil.decrypt(ciphertext, key);
        System.out.println("解密后的字符串：" + decryptedText);
    }


}
