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
        String text = "videoUrl=https://v.douyin.com/iRTta2vB/;api=https://www.52api.cn/api/weishi;timestamp="+l;
        String encrypt = encrypt(text, key1);
        System.out.println("text = " + text);
        System.out.println("encrypt = " + encrypt);

        String text2 = "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";
        String decrypt = decrypt(text2, key1);
        System.out.println("text2 = " + text2);
        System.out.println("decrypt = " + decrypt);
    }


}
