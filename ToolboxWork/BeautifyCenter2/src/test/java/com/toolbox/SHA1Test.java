package com.toolbox;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.toolbox.framework.utils.FileUtility;
import com.toolbox.framework.utils.MD5Utility;

/**
* @author E-mail:86yc@sina.com
* 
*/
public class SHA1Test {

    public static byte[] getBytes(String filePath) {
        byte[] buffer = null;
        try {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }

    public static void main(String[] args) {
        String m = FileUtility.SHA1(getBytes("/home/hope/Desktop/95d46303a83040dcbe35daa586849718.apk"));
        System.out.println("sha1=" + m);
        String md5 = MD5Utility.md5Hex(getBytes("/home/hope/Desktop/95d46303a83040dcbe35daa586849718.apk"));
        System.out.println("md5=" + md5);

    }
}
