package com.example.filedemo.common;


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

        long l = System.currentTimeMillis();
        String text = "videoUrl=https://video.weishi.qq.com/lHXJOqng;api=https://www.52api.cn/api/weishi;timestamp="+l;
        String key1 = "1234567.$12345qw";
        String encrypt = encrypt(text, key1);
        System.out.println("text = " + text);
        System.out.println("encrypt = " + encrypt);


    }
}
