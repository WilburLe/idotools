package com.toolbox.framework.utils;

import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

/**
* @author E-mail:86yc@sina.com
* 
*/
public class ImageUtility {
    public static void main(String[] args) {
        //1920*1080 960*540
        File f1 = new File("/home/hope/Desktop/upload_pic/img2.jpg");

        try {
            File f3 = zoomImage1(f1, 1000, 540);
            System.out.println(f3.getAbsolutePath());
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        //        try {
        //            File f2 = cutCenterImage(f1, 500, 500);
        //            System.out.println(f2.getAbsolutePath());
        //        } catch (IOException e) {
        //            // TODO Auto-generated catch block
        //            e.printStackTrace();
        //        }
        //        cropCenterSquare("/home/hope/Desktop/upload_pic/file[0]", "/home/hope/Desktop/upload_pic/file[0].jpg", 50, 50);
    }

    /**
     * 根据尺寸图片居中裁剪 
     * @param src
     * @param dest
     * @param w
     * @param h
     * @throws IOException
     *
     */
    public static File cutCenterImage(File file, int w, int h) throws IOException {
        String fileName = file.getName();
        String suffix = fileName.substring(fileName.indexOf(".") + 1); //jpg
        Iterator iterator = ImageIO.getImageReadersByFormatName(suffix);
        ImageReader reader = (ImageReader) iterator.next();
        InputStream in = new FileInputStream(file);
        ImageInputStream iis = ImageIO.createImageInputStream(in);
        reader.setInput(iis, true);
        ImageReadParam param = reader.getDefaultReadParam();
        int imageIndex = 0;
        int ws = (reader.getWidth(imageIndex) - w);
        ws = ws < 0 ? 0 : ws;
        int hs = (reader.getHeight(imageIndex) - h);
        hs = hs < 0 ? 0 : hs;
        Rectangle rect = new Rectangle(ws / 2, hs / 2, w, h);
        //        Rectangle rect = new Rectangle(reader.getWidth(imageIndex) / 2, reader.getHeight(imageIndex) / 2, w, h);
        param.setSourceRegion(rect);
        BufferedImage bi = reader.read(0, param);
        File tempFile = File.createTempFile(UUID.randomUUID().toString(), "." + suffix);
        ImageIO.write(bi, suffix, tempFile);
        return tempFile;
    }

    /**
     * 图片裁剪二分之一
     * @param src
     * @param dest
     * @throws IOException
     *
     */
    public static File cutHalfImage(File file) throws IOException {
        String fileName = file.getName();
        String suffix = fileName.substring(fileName.indexOf(".") + 1); //jpg
        Iterator iterator = ImageIO.getImageReadersByFormatName(suffix);
        ImageReader reader = (ImageReader) iterator.next();
        InputStream in = new FileInputStream(file);
        ImageInputStream iis = ImageIO.createImageInputStream(in);
        reader.setInput(iis, true);
        ImageReadParam param = reader.getDefaultReadParam();
        int imageIndex = 0;
        int width = reader.getWidth(imageIndex) / 2;
        int height = reader.getHeight(imageIndex) / 2;
        Rectangle rect = new Rectangle(width / 2, height / 2, width, height);
        param.setSourceRegion(rect);
        BufferedImage bi = reader.read(0, param);
        File tempFile = File.createTempFile(UUID.randomUUID().toString(), "." + suffix);
        ImageIO.write(bi, suffix, tempFile);
        return tempFile;
    }

    /**
     * 图片裁剪通用接口 
     * @param src
     * @param dest
     * @param x
     * @param y
     * @param w
     * @param h
     * @throws IOException
     *
     */
    public static File cutImage(File file, int x, int y, int w, int h) throws IOException {
        String fileName = file.getName();
        String suffix = fileName.substring(fileName.indexOf(".") + 1); //jpg
        Iterator iterator = ImageIO.getImageReadersByFormatName(suffix);
        ImageReader reader = (ImageReader) iterator.next();
        InputStream in = new FileInputStream(file);
        ImageInputStream iis = ImageIO.createImageInputStream(in);
        reader.setInput(iis, true);
        ImageReadParam param = reader.getDefaultReadParam();
        Rectangle rect = new Rectangle(x, y, w, h);
        param.setSourceRegion(rect);
        BufferedImage bi = reader.read(0, param);
        File tempFile = File.createTempFile(UUID.randomUUID().toString(), "." + suffix);
        ImageIO.write(bi, suffix, tempFile);
        return tempFile;
    }

    /**
    * 图片缩放 
    * @param src
    * @param dest
    * @param w
    * @param h
    * @throws Exception
    *
    */
    public static File zoomImage1(File file, int w, int h) throws Exception {
        String fileName = file.getName();
        String suffix = fileName.substring(fileName.indexOf(".") + 1); //jpg
        BufferedImage bufImg = ImageIO.read(file);
        Image Itemp = bufImg.getScaledInstance(w, h, bufImg.SCALE_SMOOTH);
        double wr = w * 1.0 / bufImg.getWidth();
        double hr = h * 1.0 / bufImg.getHeight();
        if (hr > 1) {
            hr = 1;
            //            return null;
        }
        if (wr > 1) {
            wr = 1;
        }
        double zm = 1;
        if (wr > hr) {
            zm = wr;
        } else {
            zm = hr;
        }
        AffineTransformOp ato = new AffineTransformOp(AffineTransform.getScaleInstance(zm, zm), null);
        Itemp = ato.filter(bufImg, null);
        File tempFile = File.createTempFile(UUID.randomUUID().toString(), "." + suffix);
        ImageIO.write((BufferedImage) Itemp, suffix, tempFile);
        //
        //        if (zm * bufImg.getWidth() >= w || zm * bufImg.getHeight() >= h) {
        //            File rfile = cutCenterImage(tempFile, w, h);
        //            tempFile.delete();
        //            return rfile;
        //        }

        File rfile = cutCenterImage(tempFile, w, h);
        tempFile.delete();
        return rfile;
    }

    //    public static File zoomImage(File file, int w, int h) throws Exception {
    //        String fileName = file.getName();
    //        String suffix = fileName.substring(fileName.indexOf(".") + 1); //jpg
    //        BufferedImage bufImg = ImageIO.read(file);
    //        Image Itemp = bufImg.getScaledInstance(w, h, bufImg.SCALE_SMOOTH);
    //        double wr = w * 1.0 / bufImg.getWidth();
    //        double hr = h * 1.0 / bufImg.getHeight();
    //        AffineTransformOp ato = new AffineTransformOp(AffineTransform.getScaleInstance(wr, hr), null);
    //        Itemp = ato.filter(bufImg, null);
    //        File tempFile = File.createTempFile(UUID.randomUUID().toString(), "." + suffix);
    //        ImageIO.write((BufferedImage) Itemp, suffix, tempFile);
    //        return tempFile;
    //    }

}
