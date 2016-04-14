package com.toolbox.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.imageio.ImageIO;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toolbox.common.AppMarketEnum;
import com.toolbox.common.LanguageEnum;
import com.toolbox.framework.utils.ConfigUtility;
import com.toolbox.framework.utils.FileUtility;
import com.toolbox.framework.utils.ImageUtility;
import com.toolbox.framework.utils.MD5Utility;
import com.toolbox.framework.utils.UUIDUtility;
import com.toolbox.web.entity.InActionCount;
import com.toolbox.web.entity.LockscreenEntity;
import com.toolbox.web.entity.WallpaperEntity;

import net.dongliu.apk.parser.ApkParser;
import net.dongliu.apk.parser.bean.ApkMeta;

/**
* @author E-mail:86yc@sina.com
* 
*/
public class UploadUtility {
    public static String base_path = ConfigUtility.getInstance().getString("upload.base.path");

    private final static String wallpaper_path          = "wallpaper/";
    public final static String  lockscreen_apk_path     = "lockscreen/apk/";
    public final static String  lockscreen_icon_path    = "lockscreen/icon/";
    public final static String  lockscreen_preview_path = "lockscreen/preview/";

    public final static String banner_voer_path = "banner/cover/";

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

    public static String upload_file(MultipartFile file, String path, String fileName) {
        String src_path = null;
        try {
            //原始文件保存路径
            src_path = path + fileName;
            File newFile = new File(base_path + src_path);
            // 转存文件  
            file.transferTo(newFile);
        } catch (Exception e) {

        }
        return src_path;
    }

    public static String upload_file(byte[] bytes, String path) throws IOException {
        File file = new File(base_path + path);
        FileUtility.writeByteArrayToFile(file, bytes);
        return path;
    }

    /**
     * 壁纸上传
     * @param files
     * @param tags
     * @return
     *
     */
    public static Map<String, Object> upload_wallpapers(List<MultipartFile> files, String tags) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        JSONArray errors = new JSONArray();
        List<WallpaperEntity> wallpapers = new ArrayList<WallpaperEntity>();
        int errorNu = 0;
        for (int i = 0; i < files.size(); i++) {
            try {
                MultipartFile file = files.get(i);
                String fileName = file.getOriginalFilename();
                BufferedImage bufImg = ImageIO.read(file.getInputStream());
                double sh = bufImg.getHeight();
                double sw = bufImg.getWidth();
                if (sh > sw) {
                    errorNu++;
                    errors.add("【" + errorNu + " " + fileName + "：宽=" + sw + "," + "高=" + sh + ", 比例不对 高不得大于宽】\n");
                    continue;
                }
                if (sh * sw < 720 * 2 * 1280) {
                    errorNu++;
                    errors.add("【" + errorNu + " " + fileName + "：宽=" + sw + "," + "高=" + sh + ", 图片不得小于 1440*1280】\n");
                    continue;
                }

                bufImg.getAccelerationPriority();

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
                JSONArray ts = JSON.parseArray(tags);
                if (ts.size() == 0) {
                    ts.add("other");
                }
                String[] dus = new String[ts.size()];
                for (int k = 0; k < ts.size(); k++) {
                    dus[k] = ts.getString(k);
                }

                wallpaper.setTags(dus);
                wallpaper.setCreateDate(System.currentTimeMillis());
                String[] market = { AppMarketEnum.China.getCode(), AppMarketEnum.GooglePlay.getCode() };
                wallpaper.setMarket(market);
                //缩放/剪切
                File preview_temp = ImageUtility.zoomImage1(newFile, 330, 292);
                if (preview_temp != null) {
                    String preview_path = wallpaper_path + uuid + "_preview" + suffix;
                    FileUtility.copyFile(preview_temp, new File(base_path + preview_path), false);
                    wallpaper.setPreviewImageUrl(preview_path);
                }
                //854 1280 1920
                if (sh <= (1280 - 854) / 2 + 854) {
                    String path = wallpaper_path + uuid + "_ldpi" + suffix;
                    JSONObject result = zoomImage(newFile, path, 480 * 2, 854);
                    fileSize.put("ldpi", result.getIntValue("size"));
                    actionUrl.put("ldpi", result.getString("path"));
                } else if (sh <= (1920 - 1280) / 2 + 1280) {
                    String path = wallpaper_path + uuid + "_ldpi" + suffix;
                    JSONObject result = zoomImage(newFile, path, 480 * 2, 854);
                    fileSize.put("ldpi", result.getIntValue("size"));
                    actionUrl.put("ldpi", result.getString("path"));

                    String path2 = wallpaper_path + uuid + "_mdpi" + suffix;
                    JSONObject result2 = zoomImage(newFile, path2, 720 * 2, 1280);
                    fileSize.put("mdpi", result2.getIntValue("size"));
                    actionUrl.put("mdpi", result2.getString("path"));
                } else {
                    String path = wallpaper_path + uuid + "_ldpi" + suffix;
                    JSONObject result = zoomImage(newFile, path, 480 * 2, 854);
                    fileSize.put("ldpi", result.getIntValue("size"));
                    actionUrl.put("ldpi", result.getString("path"));

                    String path2 = wallpaper_path + uuid + "_mdpi" + suffix;
                    JSONObject result2 = zoomImage(newFile, path2, 720 * 2, 1280);
                    fileSize.put("mdpi", result2.getIntValue("size"));
                    actionUrl.put("mdpi", result2.getString("path"));

                    String path3 = wallpaper_path + uuid + "_hdpi" + suffix;
                    JSONObject result3 = zoomImage(newFile, path3, 1080 * 2, 1920);
                    fileSize.put("hdpi", result3.getIntValue("size"));
                    actionUrl.put("hdpi", result3.getString("path"));
                }

                wallpaper.setFileSize(fileSize);
                wallpaper.setActionUrl(actionUrl);
                wallpaper.setActionCount(new InActionCount());
                wallpapers.add(wallpaper);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        resultMap.put("wallpapers", wallpapers);
        resultMap.put("errors", errors);
        return resultMap;
    }

    private static JSONObject zoomImage(File file, String path, int w, int h) throws Exception {
        JSONObject result = new JSONObject();
        File temp = ImageUtility.zoomImage1(file, w, h);
        if (temp != null) {
            FileUtility.copyFile(temp, new File(base_path + path), false);
            result.put("size", temp.length());
            result.put("path", path);
            result.put("temp", temp);
        }
        temp.delete();
        return result;
    }

    private final static String packageName_ = "com.dotool.flashlockscreen.theme.";

    public static List<LockscreenEntity> upload_lockscenery(List<MultipartFile> files, String market) {
        List<LockscreenEntity> result = new ArrayList<LockscreenEntity>();
        for (int i = 0; i < files.size(); i++) {
            MultipartFile fileData = files.get(i);
            LockscreenEntity lock = parseAPK(fileData, market);
            result.add(lock);
        }
        return result;
    }

    public static LockscreenEntity parseAPK(MultipartFile fileData, String[] market) {
        JSONArray arr = new JSONArray();
        for (int i = 0; i < market.length; i++) {
            arr.add(market[i]);
        }
        return parseAPK(fileData, arr.toJSONString());
    }

    public static LockscreenEntity parseAPK(MultipartFile fileData, String market) {
        LockscreenEntity lock = new LockscreenEntity();
        try {
            String sha1 = FileUtility.SHA1(fileData.getBytes());
            String md5 = MD5Utility.md5Hex(fileData.getBytes());
            lock.setSha1(sha1);
            lock.setMd5(md5);

            String uuid = UUIDUtility.uuid32();
            String fileName = fileData.getOriginalFilename();
            String apkSuffix = fileName.substring(fileName.lastIndexOf('.'));
            String apkPath = upload_file(fileData, lockscreen_apk_path, uuid + apkSuffix);

            lock.setElementId(uuid);
            lock.setFileSize(fileData.getSize());
            lock.setActionCount(new InActionCount());
            JSONArray ts = JSON.parseArray(market);
            String[] markets = new String[ts.size()];
            for (int k = 0; k < ts.size(); k++) {
                markets[k] = ts.getString(k);
            }
            lock.setMarket(markets);
            lock.setCreateDate(System.currentTimeMillis());

            ApkParser apkParser = new ApkParser(base_path + apkPath);
            apkParser.setPreferredLocale(Locale.SIMPLIFIED_CHINESE);
            ApkMeta apkMeta = apkParser.getApkMeta();
            String packageName = apkMeta.getPackageName();
            if (packageName.indexOf("dotool") > -1) {
                packageName = packageName.substring(packageName.lastIndexOf(".") + 1);
            }
            packageName = packageName_ + packageName;
            //resultMap.put("name", URLEncoder.encode(apkMeta.getLabel(), "UTF-8"));
            //lock.setVersion(apkMeta.getVersionCode());
            lock.setPackageName(packageName);
            String googlePlayUrl = "https://play.google.com/store/apps/details?id=" + packageName;
            JSONObject actionUrl = new JSONObject();
            actionUrl.put(LanguageEnum.zh_CN.getCode(), apkPath);
            actionUrl.put(LanguageEnum.en_US.getCode(), googlePlayUrl);
            lock.setActionUrl(actionUrl);
            byte[] iconData = apkParser.getFileData(apkMeta.getIcon());
            String iconPath = upload_file(iconData, lockscreen_icon_path + uuid + ".png");
            lock.setIconUrl(iconPath);

            byte[] themeXML = apkParser.getFileData("assets/theme.xml");
            Document document = DocumentHelper.parseText(new String(themeXML, "utf-8"));
            Element rootEle = document.getRootElement();
            Element themeAppName = rootEle.element("themeAppName");
            if (themeAppName != null) {
                JSONObject name = new JSONObject();
                name.put(LanguageEnum.zh_CN.getCode(), themeAppName.element("cn").getText());
                name.put(LanguageEnum.en_US.getCode(), themeAppName.element("en").getText());
                lock.setName(name);
            }
            Element themeDescription = rootEle.element("themeDescription");
            if (themeDescription != null) {
                JSONObject description = new JSONObject();
                description.put(LanguageEnum.zh_CN.getCode(), themeDescription.element("cn").getText());
                description.put(LanguageEnum.en_US.getCode(), themeDescription.element("en").getText());
                lock.setDescription(description);
            }

            @SuppressWarnings("unchecked")
            List<Element> previewEle = rootEle.element("themePreview").elements("item");
            List<String> detatilUrl = new ArrayList<String>();
            for (Element item : previewEle) {
                String pName = item.getText();
                byte[] previewData1 = apkParser.getFileData("assets/" + pName);//assets
                if (previewData1 != null) {
                    String previewImage = upload_file(previewData1, lockscreen_preview_path + uuid + pName);
                    if (!detatilUrl.contains(previewImage)) {
                        detatilUrl.add(previewImage);
                    }
                }
                byte[] previewData2 = apkParser.getFileData("previews/" + pName);//previews
                if (previewData2 != null) {
                    String previewImage = upload_file(previewData2, lockscreen_preview_path + uuid + pName);
                    if (!detatilUrl.contains(previewImage)) {
                        detatilUrl.add(previewImage);
                    }
                }
            }
            String[] dus = new String[detatilUrl.size()];
            for (int m = 0; m < detatilUrl.size(); m++) {
                dus[m] = detatilUrl.get(m);
            }
            lock.setPreviewImageUrl(detatilUrl.get(0));
            lock.setDetailUrl(dus);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return lock;
    }

}
