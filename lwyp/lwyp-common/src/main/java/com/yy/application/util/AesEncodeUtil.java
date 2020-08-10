package com.yy.application.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;


import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.time.LocalDateTime;

/**
 * @Description
 * @authors David.liu
 * @CreateDate: 2020/7/6 19:31
 */
@Slf4j
public class AesEncodeUtil {

    //偏移量
    public static final String VIPARA = "1234567876543210";   //AES 为16bytes. DES 为8bytes

    //编码方式
    public static final String CODE_TYPE = "UTF-8";
    //public static final String CODE_TYPE = "GBK";

    //填充类型
    public static final String AES_TYPE = "AES/CBC/PKCS5Padding";
    //public static final String AES_TYPE = "AES/ECB/PKCS7Padding";
    //此类型 加密内容,密钥必须为16字节的倍数位,否则抛异常,需要字节补全再进行加密
    //public static final String AES_TYPE = "AES/ECB/NoPadding";
    //java 不支持ZeroPadding
    //public static final String AES_TYPE = "AES/CBC/ZeroPadding";


    //字符补全
    private static final String[] consult = new String[]{"0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F","G"};


    /**
     * 加密
     *
     * @param cleartext
     * @return
     */
    public static String encrypt(String cleartext, String secret) {
        //加密方式： AES128(CBC/PKCS5Padding) + Base64, 私钥：1111222233334444
        try {

            IvParameterSpec zeroIv = new IvParameterSpec(VIPARA.getBytes());
            //两个参数，第一个为私钥字节数组， 第二个为加密方式 AES或者DES
            SecretKeySpec key = new SecretKeySpec(secret.getBytes(), "AES");
            //实例化加密类，参数为加密方式，要写全
            Cipher cipher = Cipher.getInstance(AES_TYPE); //PKCS5Padding比PKCS7Padding效率高，PKCS7Padding可支持IOS加解密
            //初始化，此方法可以采用三种方式，按加密算法要求来添加。（1）无第三个参数（2）第三个参数为SecureRandom random = new SecureRandom();中random对象，随机数。(AES不可采用这种方法)（3）采用此代码中的IVParameterSpec
            //加密时使用:ENCRYPT_MODE;  解密时使用:DECRYPT_MODE;
            cipher.init(Cipher.ENCRYPT_MODE, key,zeroIv); //CBC类型的可以在第三个参数传递偏移量zeroIv,ECB没有偏移量
            //加密操作,返回加密后的字节数组，然后需要编码。主要编解码方式有Base64, HEX, UUE,7bit等等。此处看服务器需要什么编码方式
            byte[] encryptedData = cipher.doFinal(cleartext.getBytes(CODE_TYPE));
            String decryptStr = new BASE64Encoder().encode(encryptedData).replaceAll(System.getProperty("line.separator"), "");
           return decryptStr;
        } catch (Exception e) {
            log.error("加密失败",e);
            return "";
        }
    }

    /**
     * 解密
     *
     * @param encrypted
     * @return
     */
    public static String decrypt(String encrypted, String secret) {
        try {
            byte[] byteMi = new BASE64Decoder().decodeBuffer(encrypted);
            IvParameterSpec zeroIv = new IvParameterSpec(VIPARA.getBytes());
            SecretKeySpec key = new SecretKeySpec(secret.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance(AES_TYPE);
            //与加密时不同MODE:Cipher.DECRYPT_MODE
            cipher.init(Cipher.DECRYPT_MODE, key,zeroIv);  //CBC类型的可以在第三个参数传递偏移量zeroIv,ECB没有偏移量
            byte[] decryptedData = cipher.doFinal(byteMi);
            String decryptStr = new String(decryptedData, CODE_TYPE).replaceAll(System.getProperty("line.separator"), "");
            return decryptStr;
        } catch (Exception e) {
            log.error("解密失败",e);
            return "";
        }
    }


    @Data
   static class TestCapital {
        /**
         * 工会id
         */
        private Long companyId;
        /**
         * 工会名称
         */
        private String companyName;
        /**
         * 当前账号
         */
        private String account;

        /**
         * ut失效时间
         */
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime expireTime;


    }

    public static void main(String[] args) {
        JSONObject jsonObject = JSONObject.parseObject("{\"companyId\":146,\"expireTime\":\"2021-07-23T20:12:38\",\"account\":\"liuwei2\",\"companyName\":\"测试工会\"}");
        log.info("jsonObject is: [{}]", JSON.toJSONString(jsonObject));
//        //得到加密后的信息
        AesEncodeUtil.encrypt(JSON.toJSONString(jsonObject), "$#E3SW@!Q|Z?_`CX");
//
        System.out.println("解密："+decrypt("Sd16SRs2lQLne94oFQCLSo8Y/dI5U15LDXdfS/d/0CGdLJONxRsfu7Bk8P/aSLYOvFZm6RNG4My5H4DQCkzl%2BWsPX6JJ303ib/Et9gH492TP2Hdp3SU5hRdPq059gkLbrx2sfOc7REGp3KQAeNZ8rw==","$#E3SW@!Q|Z?_`CX"));

    }


}
