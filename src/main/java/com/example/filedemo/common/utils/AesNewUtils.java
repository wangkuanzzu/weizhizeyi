package com.example.filedemo.common.utils;


import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class AesNewUtils {

    private static final String IV_STRING = "qwertyuioqwertyi";

    /**
     * AES加密
     */
    public static String encrypt(String aPlaintext, String aKey) throws Exception {
        byte[] byteContent = aPlaintext.getBytes("UTF-8");
        // 注意，为了能与 iOS 统一
        // 这里的 key 不可以使用 KeyGenerator、SecureRandom、SecretKey 生成
        byte[] enCodeFormat = aKey.getBytes();
        SecretKeySpec secretKeySpec = new SecretKeySpec(enCodeFormat, "AES");
        byte[] initParam = IV_STRING.getBytes();
        IvParameterSpec ivParameterSpec = new IvParameterSpec(initParam);
        // 指定加密的算法、工作模式和填充方式
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
        byte[] encryptedBytes = cipher.doFinal(byteContent);
        // 同样对加密后数据进行 base64 编码
        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(encryptedBytes);
    }

    /**
     * AES解密
     */
    public static String decrypt(String aCiphertext, String aKey) throws Exception {
        // base64 解码
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] encryptedBytes = decoder.decode(aCiphertext);
        byte[] enCodeFormat = aKey.getBytes();
        SecretKeySpec secretKey = new SecretKeySpec(enCodeFormat, "AES");
        byte[] initParam = IV_STRING.getBytes();
        IvParameterSpec ivParameterSpec = new IvParameterSpec(initParam);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);
        byte[] result = cipher.doFinal(encryptedBytes);
        return new String(result, "UTF-8");
    }


    public static void main(String[] args) throws Exception {

        //	"videoUrl":"https://video.weishi.qq.com/lHXJOqng",
        //	"api":"https://www.52api.cn/api/weishi",
        //	"timestemp":

        String key1 = "1234567.$12345qw";
        long l = System.currentTimeMillis();
        String text = "videoUrl=https://video.weishi.qq.com/lHXJOqng;api=https://www.52api.cn/api/weishi;timestamp="+l;
        String encrypt = encrypt(text, key1);
        System.out.println("text = " + text);
        System.out.println("encrypt = " + encrypt);

        String text2 = "r0nHcHfzvxJyn4mwkZ6guwUCqjH+MeylAyz2FuaaJsr1Z/bTXcM3mhoH/JOUGioe1pAqkiMhM8e8Lajs4FFD++RKdhQI8sW4bZr58AjB3yuK8EFp09JYQto98iLVu6/jC0bGqPk41SnzvolpfcS0OtPnUp/Xp3+DeqZX0gbr5drea/6j3RuI4VlcKTnFEjVvnYmp4jeNhRlHlTCUWege7LH1Jv8VabxntngmTckKO+KHAvhSkUGeTvGRX/VuZ1jmCrUDiMSJVUfKFqDbTTNMxwSSuFIL3vnfvG7PTN2WVixRVWeup+6I+Xki5J2pciVQzDyHJFooz65Utwp7fP/FXsDZUDdszhkIFY5id0VMyk3R7xq/6FB6NUCeE6Dnd7DWzSRpWwOe+auX95pQS/QcEj+aa3fyjuEDXN8d36gZ+x02+HVjUHJvHVqrNQ99wRnc+BDDvP9XxOjvbRAIhl3V4vhLXQf31i/R6Xr5mozk4vTLbK8aQsgKJ45PQc0FSw87HpsIWM/J1LHeGBViO4HHfHswJmf2NlgIEGYFIx4+pmM2yXu5LhCsPYJ1uoZhJn2N";
        String decrypt = decrypt(text2, key1);
        System.out.println("text2 = " + text2);
        System.out.println("decrypt = " + decrypt);
    }


}
