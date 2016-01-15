package com.toolbox.framework.utils;

import java.security.MessageDigest;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
* @author E-mail:86yc@sina.com
* 
*/
public class SHAUtility {
    private final static Log log = LogFactory.getLog(SHAUtility.class);

    public static String encryp(String str) {
        String result = str;
        try {

            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(str.getBytes("UTF-8"));
            result = Hex.encodeHexString(hash);
        } catch (Exception e) {
            log.info("SHAUtility error, msg:" + e.getMessage());
        }
        return result;
    }
}
