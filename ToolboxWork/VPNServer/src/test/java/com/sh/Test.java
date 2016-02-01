package com.sh;

import org.apache.commons.codec.digest.DigestUtils;

/**
* @author E-mail:86yc@sina.com
* 
*/
public class Test {
    private final static String pass_append = "f8Udt9diChCe";
    public static void main(String[] args) {
        String res = DigestUtils.sha256Hex("102853673414862482536" + pass_append);
        System.out.println(res);
        //2aed93e9a1d923d2b308412a213fcab9aa9f4c77636d183886d385afda278192
        //2aed93e9a1d923d2b308412a213fcab9aa9f4c77636d183886d385afda278192

    }
}
