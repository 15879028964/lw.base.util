package com.yy.application.util;

import com.google.common.base.Charsets;
import lombok.extern.slf4j.Slf4j;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Objects;

import static com.yy.application.constant.CommonConstant.*;


/**
 * @Description: 自定义加密类
 * @Author: David.liu
 * @Date: 2020/2/27 15:09
 */
@Slf4j
public class CustomEncryptionUtil {

    /**
     * 加密次数
     */
    private static final int ENC_REPEAT = 5;        //加密3次


    /**
     * Base64 加密处理 ：盐值+5次重复加密
     *
     * @param String 要加密的字符串，需要与盐值整合
     * @return 加密后的数据
     */
    public static String encryptBase64(String String, String salt) {
        // 😊 下面的编码代表笑脸
        String temp = String + "\uD83D\uDE0A" + salt + "\uD83D\uDE0A";
        //将字符串变为字节数组
        byte[] data = temp.getBytes(Charsets.UTF_8);
        for (int x = 0; x < ENC_REPEAT; x++) {
            //重复加密：加强安全性
            data = Base64.getEncoder().encode(data);
        }
        return new String(data, Charsets.UTF_8);
    }


    /**
     * 生成32位md5码
     *
     * @param inStr
     * @param salt
     * @return
     */
    public static String encryptMD5(String inStr, String salt) throws Exception {
        // 😊 下面的编码代表笑脸
        String key = "\uD83D\uDE0A" + salt + "\uD83D\uDE0A";
        String keyStr = inStr + key;
        MessageDigest md5 = null;
        try {
            //获取信息摘要对象
            md5 = MessageDigest.getInstance(KEY_MD5);
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
            return "";
        }
        char[] charArray = keyStr.toCharArray();
        //获取字符串的字节数组
        byte[] byteArray = new byte[charArray.length];
        for (int i = 0; i < charArray.length; i++) {
            byteArray[i] = (byte) charArray[i];
        }
        //摘要对象对字节数组进行摘要,得到摘要字节数组:
        byte[] md5Bytes = md5.digest(byteArray);

        //把摘要字节数组中的每一个字节转换成16进制,并拼接
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();

    }

    /**
     * SHA加密
     *
     * @param inStr
     * @param salt
     * @return
     * @throws Exception
     */
    public static String encryptSHA(String inStr, String salt) throws Exception {
        char[] charArray = (inStr + salt).toCharArray();
        //获取字符串的字节数组
        byte[] byteArray = new byte[charArray.length];
        for (int i = 0; i < charArray.length; i++) {
            byteArray[i] = (byte) charArray[i];
        }
        MessageDigest sha = MessageDigest.getInstance(KEY_SHA);
        sha.update(byteArray);
        byte[] SHABytes = sha.digest();
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < SHABytes.length; i++) {
            int val = ((int) SHABytes[i]) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();

    }

    /**
     * @param key
     * @param saltStr
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static String encryptPBKDF2(String key, String saltStr) throws NoSuchAlgorithmException {
        //定义迭代次数
        int iterations = 1000;

        //转成字符串字节数组
        char[] keyChars = key.toCharArray();
        //这里的salt为参数带入，外面自定义生成，暂时不使用SecureRandom类提供加密的强随机数生成器 (RNG)
        byte[] salt = saltStr.getBytes(Charsets.UTF_8);

        //带有生成可变密钥大小的 PBE 密码的 PBEKey 时使用的一个密码、salt、迭代计数以及导出密钥长度的构造方法。
        PBEKeySpec spec = new PBEKeySpec(keyChars, salt, iterations, 64 * 8);
        //密钥工厂--返回一个SecretKeyFactory对象，该对象将转换指定算法的秘密密钥。
        // 这里选用PBKDF2WithHmacSHA1 算法：原因--》比MD5算法更安全。它可以同样密码在不同时间生成不同加密Hash
        SecretKeyFactory skf = SecretKeyFactory.getInstance(KEY_PBK);
        byte[] hash;
        try {
            //SecretKey根据提供的密钥规范（密钥材料）生成对象。
            hash = skf.generateSecret(spec).getEncoded();
            return encryptMD5(iterations + "df" + toHex(salt) + "df" + toHex(hash), saltStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 把字符串字节转成十六进制
     *
     * @param array
     * @return
     */
    private static String toHex(byte[] array) {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int paddingLength = (array.length * 2) - hex.length();
        if (paddingLength > 0) {
            return String.format("%0" + paddingLength + "d", 0) + hex;
        } else {
            return hex;
        }
    }

    /**
     * SecureRandom类提供加密的强随机数生成器 (RNG)（暂时不用）
     *
     * @return
     * @throws NoSuchAlgorithmException
     */
    private static String getSalt() throws NoSuchAlgorithmException {
        SecureRandom sr = SecureRandom.getInstance(KEY_SHA1PRNG);
        byte[] salt = new byte[128];
        sr.nextBytes(salt);
        return new String(salt, Charsets.UTF_8);
    }


    public static void main(String[] args) {
        String key = "123456";
        String salt = "STWfAX";
        try {
            //base64加密
            if (encryptBase64(key, salt).equals(encryptBase64(key, salt))) {
                System.out.println("base64加密:" + encryptBase64(key, salt));
            }
            //MD5加密
            if (encryptMD5(key, salt).equals(encryptMD5(key, salt))) {
                System.out.println("MD5加密:" + encryptMD5(key, salt));
            }
            //SHA加密
            if (encryptSHA(key, salt).equals(encryptSHA(key, salt))) {
                System.out.println("SHA加密:" + encryptSHA(key, salt));
            }
            //PBKDF2加密
            if (Objects.equals(encryptPBKDF2(key, salt), encryptPBKDF2(key, salt))) {
                System.out.println("PBKDF2加密:" + encryptPBKDF2(key, salt));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
