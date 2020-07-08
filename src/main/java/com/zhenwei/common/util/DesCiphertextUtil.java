package com.zhenwei.common.util;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.SecureRandom;

public class DesCiphertextUtil {

    //前端对称加密算法
    private static final String DES_ALGORITHM = "DES";
    private static final String DEFAULT_DES_KEY = "8*nlu4Hz6S!jU79";

    /**
     * DES加密操作
     * @param source 要加密的源
     * @param key    约定的密钥
     * @return
     */
    public static String encryptDES(String source,String key){
        //强加密随机数生成器
        SecureRandom random = new SecureRandom();
        try {
            //创建密钥规则
            DESKeySpec keySpec = new DESKeySpec(key.getBytes());
            //创建密钥工厂
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            //按照密钥规则生成密钥
            SecretKey secretKey =  keyFactory.generateSecret(keySpec);
            //加密对象
            Cipher cipher = Cipher.getInstance(DES_ALGORITHM);
            //初始化加密对象需要的属性
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, random);
            //开始加密
            byte[] result = cipher.doFinal(source.getBytes());
            //Base64加密
            return  new BASE64Encoder().encode(result) ;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 解密
     * @param cryptograph 密文
     * @param key         约定的密钥
     * @return
     */
    public static String decryptDES(String cryptograph,String key){
        //强加密随机生成器
        SecureRandom random  =  new SecureRandom();
        try {
            //定义私钥规则
            DESKeySpec keySpec = new DESKeySpec(key.getBytes());
            //定义密钥工厂
            SecretKeyFactory factory = SecretKeyFactory.getInstance("DES");
            //按照密钥规则生成密钥
            SecretKey secretkey = factory.generateSecret(keySpec);
            //创建加密对象
            Cipher cipher = Cipher.getInstance(DES_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretkey, random);
            //Base64对
            byte[] result = new BASE64Decoder().decodeBuffer(cryptograph);
            return new String(cipher.doFinal(result));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args)throws Exception {
        //前端对称加密解码
        String pwd="+3qk0If1aeATKQnsdvZeJdYSrtxnXT4j7xvwaG/nWd312oraCXScQTBYJAeWsZ9JxXWbenbOTC3Z\n" +
                "WVO/mZKskljsIQl9f/KCRFUA6aPoDnKbGewZgSSJO4ZWLFjkP58SV1L+ZDLyaHJFRRmzSS+3IbiP\n" +
                "5QxDf7mMMGkKaytsG15y55tfvNkbcC1BgMXNbYfI2W8JYL94iQvmUEAk/a7bFgcblWVKxTmIY3kk\n" +
                "3Q7BAT7BdIEYhsWoHD1FLoTDWdkhoIzj38cGNKtoKUg8h11ILYp2kUstQBx9eV6FavwW2tzeN7/I\n" +
                "8ZOOXhyS0fAcWH6dDwXBjmpx0KnqlzSdAfbHvHLnm1+82RtwuHXJmg5c4HtD9BJ9EuE4+x7/VPsF\n" +
                "iMmwQ/QSfRLhOPvoprlVPQii2gZ+DDks0uTyRjsYdZTP2F0=";
        System.out.println("明文: " + decryptDES(pwd,DEFAULT_DES_KEY));


        //System.out.println("密文: " + encryptDES("schoolCode=schoolCode_test&studentNum=1993&idCardNo=3308241994&name=name_test","health20200307code!"));
    }

}