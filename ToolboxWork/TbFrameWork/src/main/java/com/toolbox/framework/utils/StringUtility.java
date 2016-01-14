package com.toolbox.framework.utils;

import org.apache.commons.lang.StringUtils;

public class StringUtility extends StringUtils {

    public static String toString(Object obj) {
        return obj == null ? null : obj.toString();
    }

    public static boolean isBoolean(String str) {
        if ("true".equalsIgnoreCase(str) || "false".equalsIgnoreCase(str)) {
            return true;
        }
        return false;
    }

    public static boolean isNotEmpty(String... array) {
        for (String str : array) {
            if (isEmpty(str)) {
                return false;
            }
        }
        return true;
    }

    public static String fillChar(String str, char add, int length, boolean asc) {
        if (str.length() >= length) {
            return str;
        }
        StringBuilder fill = new StringBuilder();
        if (!asc) {
            fill.append(str);
        }
        for (int i = 0; i < length - str.length(); i++) {
            fill.append(add);
        }
        return asc ? fill.toString() + str : fill.toString();
    }

    private static char[] hexChars = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

    /**
    * Convert a byte array to a hex-encoding string: "a33bff00..."
    */
    public static String bytesToHexString(final byte[] bytes) {
        return bytesToHexString(bytes, null);
    }

    /**
    * Convert a byte array to a hex-encoding string with the specified
    * delimiter: "a3&lt;delimiter&gt;3b&lt;delimiter&gt;ff..."
    */
    public static String bytesToHexString(final byte[] bytes, Character delimiter) {
        StringBuffer hex = new StringBuffer(bytes.length * (delimiter == null ? 2 : 3));
        int nibble1, nibble2;
        for (int i = 0; i < bytes.length; i++) {
            nibble1 = (bytes[i] >>> 4) & 0xf;
            nibble2 = bytes[i] & 0xf;
            if (i > 0 && delimiter != null)
                hex.append(delimiter.charValue());
            hex.append(hexChars[nibble1]);
            hex.append(hexChars[nibble2]);
        }
        return hex.toString();
    }

}
