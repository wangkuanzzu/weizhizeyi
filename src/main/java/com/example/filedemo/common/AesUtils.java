package com.example.filedemo.common;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class AesUtils {


    private static final String AES = "AES";
    private static final String Mode = "AES/CBC/PKCS5Padding";

    private static Cipher cipherEncrypt;
    private static Cipher cipherDecrypt;
    /**
     * AES加密
     */
    public static byte[] encrypt(String aPlaintext, String aKey) throws Exception{
        String originalString = "";
        byte[] encrypted = null;
        try {
            String tmpIv = aKey.substring(16, 32);
            SecretKeySpec keyspec = new SecretKeySpec(aKey.getBytes(), AES);
            IvParameterSpec ivspec = new IvParameterSpec(tmpIv.getBytes());
            cipherEncrypt = Cipher.getInstance(Mode);
            cipherEncrypt.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);


            int blockSize = cipherEncrypt.getBlockSize();
            byte[] dataBytes = aPlaintext.getBytes();
            int plaintextLength = dataBytes.length;
            if (plaintextLength % blockSize != 0) {
                plaintextLength = plaintextLength + (blockSize - (plaintextLength % blockSize));
            }
            byte[] plaintext = new byte[plaintextLength];
            System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);

            try {
                encrypted = cipherEncrypt.doFinal(plaintext);
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            } catch (BadPaddingException e) {
                e.printStackTrace();
            }
            if (encrypted == null) {
                return null;
            }
            originalString = Base64.encode(encrypted);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }

        return encrypted;
    }
    /**
     * AES解密
     */
    public static String decrypt(byte[] aCiphertext, String aKey) {
        String originalString = "";
        try {
            String tmpIv = aKey.substring(16, 32);
            SecretKeySpec keyspec = new SecretKeySpec(aKey.getBytes(), AES);
            IvParameterSpec ivspec = new IvParameterSpec(tmpIv.getBytes());
            cipherDecrypt = Cipher.getInstance(Mode);
            cipherDecrypt.init(Cipher.DECRYPT_MODE, keyspec, ivspec);
//            byte[] sourcebyte = Base64.decode(aCiphertext);
            byte[] original = null;
            original = cipherDecrypt.doFinal(aCiphertext);
            try {
                originalString = new String(original, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            System.out.println("设置算法错误!");
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            System.out.println("解密算法密钥设置错误!");
            System.out.println("解密算法密钥长度不够，过长!");
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            System.out.println("算法解密过程错误!");
            System.out.println("块大小设置错误!密文长度不够或太长!");
            e.printStackTrace();
        } catch (BadPaddingException e) {
            System.out.println("解密算法解密过程错误!");
            System.out.println("解密填充方式错误!");
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
        return originalString.trim();
    }


    public static void main(String[] args) throws Exception {
        String text = "woshishui";
        String key1 = "123456789qwertyuiopasdfghjklzxcv";
        byte[] encrypt = encrypt(text, key1);
        System.out.println("encrypt = " + Arrays.toString(encrypt));
        String decrypt = decrypt(encrypt, key1);
        System.out.println("decrypt = " + decrypt);
    }
}
