package com.toolbox.utils;

import java.io.File;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toolbox.framework.utils.ConfigUtility;
import com.toolbox.framework.utils.FileUtility;
import com.toolbox.framework.utils.ImageUtility;
import com.toolbox.framework.utils.UUIDUtility;

/**
* @author E-mail:86yc@sina.com
* 
*/
public class UploadUtility {
    private static String base_path = ConfigUtility.getInstance().getString("upload.base.path");

    private final static String wallpaper_path = "wallpaper/";
    private final static String theme_path     = "theme/";

    public static JSON upload(List<MultipartFile> files, String tablename, String tags) {
        switch (tablename) {
            case "wallpaper":
                return upload_wallpapers(files, wallpaper_path, tags);
            case "theme":
                return upload_zhuti(files, theme_path);
            case "tianqi":
                return null;
            default:
                return null;
        }
    }

    private static JSON upload_wallpapers(List<MultipartFile> files, String path, String tags) {
        JSONArray arr = new JSONArray();
        for (int i = 0; i < files.size(); i++) {
            try {
                JSONObject json = new JSONObject();
                MultipartFile file = files.get(i);
                String fileName = file.getOriginalFilename();
                String suffix = fileName.substring(fileName.lastIndexOf('.'));
                String uuid = UUIDUtility.uuid32();
                //原始文件保存路径
                String src_path = base_path + path + uuid + suffix;
                File newFile = new File(src_path);
                // 转存文件  
                file.transferTo(newFile);

                JSONObject fileSize = new JSONObject();
                JSONObject actionUrl = new JSONObject();
                json.put("elementId", uuid);
                json.put("src", src_path);
                json.put("tags", JSON.parseArray(tags));
                json.put("createDate", System.currentTimeMillis());
                //缩放/剪切
                File preview_temp = ImageUtility.zoomImage1(newFile, 330, 292);
                if (preview_temp != null) {
                    String preview_path = path + uuid + "_preview" + suffix;
                    FileUtility.copyFile(preview_temp, new File(base_path + preview_path), false);
                    json.put("previewImageUrl", preview_path);
                }

                File hdpi_temp = ImageUtility.zoomImage1(newFile, 1080 * 2, 1920);
                if (hdpi_temp != null) {
                    String hdpi_path = path + uuid + "_hdpi" + suffix;
                    FileUtility.copyFile(hdpi_temp, new File(base_path + hdpi_path), false);
                    fileSize.put("hdpi", hdpi_temp.length());
                    actionUrl.put("hdpi", hdpi_path);
                }

                File mdpi_temp = ImageUtility.zoomImage1(newFile, 720 * 2, 1280);
                if (mdpi_temp != null) {
                    String mdpi_path = path + uuid + "_mdpi" + suffix;
                    FileUtility.copyFile(mdpi_temp, new File(base_path + mdpi_path), false);
                    fileSize.put("mdpi", mdpi_temp.length());
                    actionUrl.put("mdpi", mdpi_path);
                }

                File ldpi_temp = ImageUtility.zoomImage1(newFile, 480 * 2, 854);
                if (ldpi_temp != null) {
                    String ldpi_path = path + uuid + "_ldpi" + suffix;
                    FileUtility.copyFile(ldpi_temp, new File(base_path + ldpi_path), false);
                    fileSize.put("ldpi", ldpi_temp.length());
                    actionUrl.put("ldpi", ldpi_path);
                }
                json.put("fileSize", fileSize);
                json.put("actionUrl", actionUrl);
                arr.add(json);
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
        return arr;
    }

    private static JSON upload_zhuti(List<MultipartFile> files, String path) {

        return null;
    }
}
