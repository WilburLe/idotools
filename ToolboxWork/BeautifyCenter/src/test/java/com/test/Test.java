package com.test;

import java.io.UnsupportedEncodingException;

/**
* @author E-mail:86yc@sina.com
* 
*/
public class Test {

    public static void main(String[] args) {

        String s1 = "http://120.0.0.1/wallpaper/0a03bc685a454035b2059c1c7ccfd94e_preview.jpg";
        String s = "http://120.0.0.1/wallpaper/0a03bc685a454035b2059c1c7ccfd94e_preview.jpg";
        String s2 = "http://127.0.0.1/wallpaper/0a03bc685a454035b2059c1c7ccfd94e_preview.jpg";
        System.out.println(s1 + "\n" + s2);
        System.out.println(s1.length() + " & " + s2.length());

        for (int i = 0; i < s1.length(); i++) {
            String s1_c = s1.substring(i, i + 1);
            String s2_c = s2.substring(i, i + 1);
            try {
                s1_c = new String(s1_c.getBytes(), "utf8");
                //                s2_c = new String(s2_c.getBytes(), "utf8");
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            System.out.println(s1_c + ":" + s2_c + "=" + (s1_c == s2_c));
        }
    }
}
