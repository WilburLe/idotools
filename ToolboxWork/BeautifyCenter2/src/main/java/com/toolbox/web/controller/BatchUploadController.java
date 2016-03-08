package com.toolbox.web.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toolbox.common.AppEnum;
import com.toolbox.framework.utils.StringUtility;
import com.toolbox.utils.UploadUtility;
import com.toolbox.web.entity.LockscreenEntity;
import com.toolbox.web.entity.WallpaperEntity;
import com.toolbox.web.service.LockScreenService;
import com.toolbox.web.service.WallpaperService;

/**
* @author E-mail:86yc@sina.com
* 
*/
@Controller
public class BatchUploadController {
    @Autowired
    private WallpaperService  wallpaperService;
    @Autowired
    private LockScreenService lockScreenService;

    @RequestMapping(value = "upload", method = RequestMethod.POST)
    public @ResponseBody JSON upload(HttpServletRequest request, String tablename, String tags, String market) {
        tags = StringUtility.isEmpty(tags) ? "[]" : tags;
        market = StringUtility.isEmpty(market) ? "[]" : market;

        JSONObject result = new JSONObject();
        if (!ServletFileUpload.isMultipartContent(request)) {
            result.put("statu", false);
            result.put("msg", "It's not Multipart Content");
            return result;
        }
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Iterator<String> it = multipartRequest.getFileNames();
        List<MultipartFile> files = new ArrayList<MultipartFile>();
        while (it.hasNext()) {
            String filenindex = it.next();
            MultipartFile file = multipartRequest.getFile(filenindex);
            if (!file.isEmpty()) {
                files.add(file);
            }
        }
        int successNum = 0;
        JSONArray msg = new JSONArray();
        if (AppEnum.wallpaper.getCollection().equals(tablename)) {
            Map<String, Object> upload_result = UploadUtility.upload_wallpapers(files, tags);
            List<WallpaperEntity> wallpapers = (List<WallpaperEntity>) upload_result.get("wallpapers");
            msg = (JSONArray) upload_result.get("errors");
            successNum = wallpapers.size();
            wallpaperService.saveList(wallpapers);
        } else if (AppEnum.lockscreen.getCollection().equals(tablename)) {
            List<LockscreenEntity> upload_result = UploadUtility.upload_lockscenery(files, market);
            for (int i = 0; i < upload_result.size(); i++) {
                LockscreenEntity saveEntity = upload_result.get(i);
                LockscreenEntity check = lockScreenService.findByPackageName(saveEntity.getPackageName());
                if (check == null) {
                    lockScreenService.save(saveEntity);
                    successNum++;
                } else {
                    msg.add("【" + saveEntity.getPackageName() + "包名重复】");
                }
            }
        }
        result.put("total", files.size());
        result.put("successNum", successNum);
        result.put("failNum", files.size() - successNum);
        result.put("msg", msg);
        return result;
    }

}
