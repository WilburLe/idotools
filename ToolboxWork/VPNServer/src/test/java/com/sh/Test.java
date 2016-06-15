package com.sh;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.UnsupportedEncodingException;

/**
 * @author E-mail:86yc@sina.com
 */
public class Test {
    private final static String pass_append = "f8Udt9diChCe";

    public static void main(String[] args) {
        String res = DigestUtils.sha256Hex("102853673414862482536" + pass_append);
        System.out.println(res);
        //2aed93e9a1d923d2b308412a213fcab9aa9f4c77636d183886d385afda278192
        //2aed93e9a1d923d2b308412a213fcab9aa9f4c77636d183886d385afda278192
        String pass_append_1 = "f8Udt9diChCe";
        String pass_append_2 = "Jdsd8fdLfh7O";
        String username = "username";
        String pass = DigestUtils.sha256Hex(username + pass_append_1);
        String sha256Hex = DigestUtils.sha256Hex(pass + pass_append_2);
        System.out.println("pass > " + pass);
        System.out.println("sha256Hex > " + sha256Hex);
        try {
            System.out.println(">>> "+pass_append_2.getBytes("UTF-8"));
            String saltHex = Hex.encodeHexString(pass_append_2.getBytes("UTF-8"));
            System.out.println("saltHex > " + saltHex);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
