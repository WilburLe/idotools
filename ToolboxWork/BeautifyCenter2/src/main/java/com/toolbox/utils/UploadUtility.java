package com.toolbox.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.toolbox.framework.utils.ConfigUtility;
import com.toolbox.framework.utils.FileUtility;
import com.toolbox.framework.utils.ImageUtility;
import com.toolbox.framework.utils.UUIDUtility;
import com.toolbox.web.entity.InActionCount;
import com.toolbox.web.entity.WallpaperEntity;

/**
* @author E-mail:86yc@sina.com
* 
*/
public class UploadUtility {
    private static String base_path = ConfigUtility.getInstance().getString("upload.base.path");

    private final static String wallpaper_path   = "wallpaper/";
    private final static String theme_path       = "theme/";
    private final static String weather_path     = "weather/";
    public final static String  banner_voer_path = "banner/cover/";

    /**
    * 单张上传
    * @param file
    * @return   文件相对地址
    *
    */
    public static String upload_file(MultipartFile file, String path) {
        String src_path = null;
        try {
            String fileName = file.getOriginalFilename();
            String suffix = fileName.substring(fileName.lastIndexOf('.'));
            String uuid = UUIDUtility.uuid32();
            //原始文件保存路径
            src_path = path + uuid + suffix;
            File newFile = new File(base_path + src_path);
            // 转存文件  
            file.transferTo(newFile);
        } catch (Exception e) {

        }
        return src_path;
    }

    /**
     * 壁纸上传
     * @param files
     * @param tags
     * @return
     *
     */
    public static List<WallpaperEntity> upload_wallpapers(List<MultipartFile> files, String tags) {
        List<WallpaperEntity> wallpapers = new ArrayList<WallpaperEntity>();
        for (int i = 0; i < files.size(); i++) {
            try {
                MultipartFile file = files.get(i);
                String fileName = file.getOriginalFilename();
                String suffix = fileName.substring(fileName.lastIndexOf('.'));
                String uuid = UUIDUtility.uuid32();
                //原始文件保存路径
                String src_path = base_path + wallpaper_path + uuid + suffix;
                File newFile = new File(src_path);
                // 转存文件  
                file.transferTo(newFile);

                WallpaperEntity wallpaper = new WallpaperEntity();
                JSONObject fileSize = new JSONObject();
                JSONObject actionUrl = new JSONObject();
                wallpaper.setElementId(uuid);
                wallpaper.setSrc(src_path);
                wallpaper.setTags(JSON.parseArray(tags));
                wallpaper.setCreateDate(System.currentTimeMillis());
                //缩放/剪切
                File preview_temp = ImageUtility.zoomImage1(newFile, 330, 292);
                if (preview_temp != null) {
                    String preview_path = wallpaper_path + uuid + "_preview" + suffix;
                    FileUtility.copyFile(preview_temp, new File(base_path + preview_path), false);
                    wallpaper.setPreviewImageUrl(preview_path);
                }

                File hdpi_temp = ImageUtility.zoomImage1(newFile, 1080 * 2, 1920);
                if (hdpi_temp != null) {
                    String hdpi_path = wallpaper_path + uuid + "_hdpi" + suffix;
                    FileUtility.copyFile(hdpi_temp, new File(base_path + hdpi_path), false);
                    fileSize.put("hdpi", hdpi_temp.length());
                    actionUrl.put("hdpi", hdpi_path);
                }

                File mdpi_temp = ImageUtility.zoomImage1(newFile, 720 * 2, 1280);
                if (mdpi_temp != null) {
                    String mdpi_path = wallpaper_path + uuid + "_mdpi" + suffix;
                    FileUtility.copyFile(mdpi_temp, new File(base_path + mdpi_path), false);
                    fileSize.put("mdpi", mdpi_temp.length());
                    actionUrl.put("mdpi", mdpi_path);
                }

                File ldpi_temp = ImageUtility.zoomImage1(newFile, 480 * 2, 854);
                if (ldpi_temp != null) {
                    String ldpi_path = wallpaper_path + uuid + "_ldpi" + suffix;
                    FileUtility.copyFile(ldpi_temp, new File(base_path + ldpi_path), false);
                    fileSize.put("ldpi", ldpi_temp.length());
                    actionUrl.put("ldpi", ldpi_path);
                }
                wallpaper.setFileSize(fileSize);
                wallpaper.setActionUrl(actionUrl);
                wallpaper.setActionCount(new InActionCount());
                wallpapers.add(wallpaper);
                try {
                    preview_temp.delete();
                    hdpi_temp.delete();
                    mdpi_temp.delete();
                    ldpi_temp.delete();
                } catch (Exception e) {
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return wallpapers;
    }

    public static JSON upload_theme(List<MultipartFile> files, String path) {

        return null;
    }
}
