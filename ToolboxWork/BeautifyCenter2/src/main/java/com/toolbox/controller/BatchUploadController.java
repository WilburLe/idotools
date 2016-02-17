package com.toolbox.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
import com.alibaba.fastjson.JSONObject;
import com.toolbox.entity.WallpaperEntity;
import com.toolbox.framework.utils.StringUtility;
import com.toolbox.service.WallpaperService;
import com.toolbox.utils.UploadUtility;

/**
* @author E-mail:86yc@sina.com
* 
*/
@Controller
public class BatchUploadController {
    @Autowired
    private WallpaperService wallpaperService;

    @RequestMapping(value = "upload", method = RequestMethod.POST)
    public @ResponseBody JSON upload(HttpServletRequest request, String tablename, String tags) {
        tags = StringUtility.isEmpty(tags) ? "[]" : tags;

        JSONObject result = new JSONObject();
        if (!ServletFileUpload.isMultipartContent(request)) {
            result.put("statu", false);
            result.put("msg", "It's not Multipart Content");
            return result;
        }
        try {
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            Iterator<String> it = multipartRequest.getFileNames();
            List<MultipartFile> files = new ArrayList<MultipartFile>();
            int count = 0;

            while (it.hasNext()) {
                String filenindex = it.next();
                MultipartFile file = multipartRequest.getFile(filenindex);
                if (!file.isEmpty()) {
                    files.add(file);
                    count++;
                }
            }
            List<WallpaperEntity> upload_result = UploadUtility.upload_wallpapers(files, tags);
            switch (tablename) {
                case "wallpaper":
                    wallpaperService.saveList(upload_result);
                    break;

                default:
                    break;
            }

            result.put("count", count);
            result.put("statu", true);
        } catch (Exception e) {
            result.put("statu", false);
            result.put("msg", e.getMessage());
        }

        return result;
    }

}
