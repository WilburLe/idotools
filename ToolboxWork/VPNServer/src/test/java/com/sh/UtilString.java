package com.sh;

/**
* @author E-mail:86yc@sina.com
* 
*/
public class UtilString {  
    private static final char[] bcdLookup = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

    /** 
     * 将字节数组转换为16进制字符串的形式. 
     */
    public static final String bytesToHexStr(byte[] bcd) {
        StringBuffer s = new StringBuffer(bcd.length * 2);
        for (int i = 0; i < bcd.length; i++) {
            s.append(bcdLookup[(bcd[i] >>> 4) & 0x0f]);
            s.append(bcdLookup[bcd[i] & 0x0f]);
        }
        return s.toString();
    }

    /** 
     * 将16进制字符串还原为字节数组. 
     */
    public static final byte[] hexStrToBytes(String s) {
        byte[] bytes;
        bytes = new byte[s.length() / 2];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) Integer.parseInt(s.substring(2 * i, 2 * i + 2), 16);
        }
        return bytes;
    }

    public static void main(String[] args) {
        String str = "aa人民";
        //      str = URLEncoder.encode(str);  
        //      byte[] aa = UtilString.hexStrToBytes(str);  
        //      String nstr =new String(aa);  
        //      System.out.println(nstr);  
        //      System.out.println(UtilString.HexToString(nstr));  

        //字符串先byte 再十六进制，再字符串  
        str = bytesToHexStr(str.getBytes());
        System.out.println(str);

        //将16进制字符串还原为字节数组.  
        byte[] bb = hexStrToBytes(str);
        System.out.println(new String(bb));

    }
}
