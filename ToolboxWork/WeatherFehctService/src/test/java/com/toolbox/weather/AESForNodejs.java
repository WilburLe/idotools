
package com.toolbox.weather;

import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AESForNodejs {
    private static final String PASSWORD = "5uCItjTAohgFxo9a";

    public static String decrypt(String encrypted, String seed) throws Exception { // TODO 异常处理
        byte[] keyb = seed.getBytes("UTF-8");
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] thedigest = md.digest(keyb);
        SecretKeySpec skey = new SecretKeySpec(thedigest, "AES");
        Cipher dcipher = Cipher.getInstance("AES");
        dcipher.init(Cipher.DECRYPT_MODE, skey);
        byte[] clearbyte = dcipher.doFinal(toByte(encrypted)); //new BASE64Decoder().decodeBuffer(encrypted)
        return new String(clearbyte);
    }

    private static byte[] toByte(String hexString) {
        int len = hexString.length() / 2;
        byte[] result = new byte[len];
        for (int i = 0; i < len; i++) {
            result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2), 16).byteValue();
        }
        return result;
    }

    public static void main(String[] args) throws Exception {
        String enc = "d08826d4050a7d1cefff3e2398f2d662bc7e169ec69dfa896c9147dbd6153415";
        System.out.println(decrypt(enc, PASSWORD));
    }
}