package com.toolbox.web.controller;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toolbox.common.BannerEnum;
import com.toolbox.framework.utils.StringUtility;
import com.toolbox.framework.utils.UUIDUtility;
import com.toolbox.utils.UploadUtility;
import com.toolbox.web.entity.BannerEntity;
import com.toolbox.web.service.BannerService;
import com.toolbox.web.service.CommonJSONService;

/**
* @author E-mail:86yc@sina.com
* 
*/
@Controller
public class BannerController {
    @Autowired
    private BannerService     bannerService;
    @Autowired
    private CommonJSONService commonJSONService;

    @RequestMapping(value = "banner/view/{bannerType}", method = RequestMethod.GET)
    public ModelAndView bannerview(@PathVariable("bannerType") String bannerType) {
        List<BannerEntity> banners = bannerService.findByBannerType(bannerType);
        return new ModelAndView("banner/view").addObject("banners", banners).addObject("bannerType", bannerType);
    }

    @RequestMapping(value = "banner/add", method = RequestMethod.POST)
    public ModelAndView banneradd(HttpServletRequest request, String bannerType, String title, String intro, String h5url) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Iterator<String> it = multipartRequest.getFileNames();
        MultipartFile cover = null;
        while (it.hasNext()) {
            String filenindex = it.next();
            MultipartFile file = multipartRequest.getFile(filenindex);
            if (!file.isEmpty()) {
                cover = file;
                break;
            }
        }
        String coverPath = null;
        if (cover != null) {
            coverPath = UploadUtility.upload_file(cover, UploadUtility.banner_voer_path);
        }

        BannerEntity banner = new BannerEntity();
        banner.setBannerType(bannerType);
        banner.setCreateDate(System.currentTimeMillis());
        banner.setElementId(UUIDUtility.uuid32());
        banner.setIntro(intro);
        banner.setPreviewImageUrl(coverPath);
        banner.setTitle(title);
        if (bannerType.equals(BannerEnum.H5.getType())) {
            JSONObject content = new JSONObject();
            content.put("url", h5url);
        }
        bannerService.save(banner);
        return new ModelAndView("redirect:view/" + bannerType);
    }

    @RequestMapping(value = "banner/edit/{elementId}", method = RequestMethod.GET)
    public ModelAndView banneredit(@PathVariable("elementId") String elementId) {
        BannerEntity banner = bannerService.findByElementId(elementId);
        return new ModelAndView("banner/edit").addObject("banner", banner);
    }

    @RequestMapping(value = "banner/del/{elementId}", method = RequestMethod.GET)
    public @ResponseBody JSON bannerdel(@PathVariable("elementId") String elementId) {
        bannerService.delBanner(elementId);
        return null;
    }

    @RequestMapping(value = "banner/content", method = RequestMethod.POST)
    public @ResponseBody String bannercontent(String appType, String elementId) {
        if (StringUtility.isEmpty(appType)) {
            return null;
        }
        Query query = new Query();
        query.addCriteria(Criteria.where("elementId").is(elementId));
        //注意 如果是查找专题，就要去Banner collection中查找，并且只查找类型为专题的Banner
        if (appType.equals("subject")) {
            appType = "banner";
            query.addCriteria(Criteria.where("bannerType").is(BannerEnum.Subject.getType()));
        }
        String app = commonJSONService.findOne(query, appType);
        return app;
    }

    @RequestMapping(value = "banner/edit", method = RequestMethod.POST)
    public ModelAndView banneredit(HttpServletRequest request, String bannerId, String title, String intro, String h5url, //
            String elementId, String appType, String previewImageUrl) {
        JSONObject result = new JSONObject();
        //获取封面
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Iterator<String> it = multipartRequest.getFileNames();
        MultipartFile cover = null;
        while (it.hasNext()) {
            String filenindex = it.next();
            MultipartFile file = multipartRequest.getFile(filenindex);
            if (!file.isEmpty()) {
                cover = file;
                break;
            }
        }
        BannerEntity banner = bannerService.findByElementId(bannerId);
        //封面存在的情况下更新封面
        if (cover != null) {
            String coverPath = UploadUtility.upload_file(cover, UploadUtility.banner_voer_path);
            banner.setPreviewImageUrl(coverPath);
        }
        banner.setTitle(title);
        banner.setIntro(intro);

        JSONObject content = banner.getContent() == null ? new JSONObject() : banner.getContent();
        if (banner.getBannerType().equals(BannerEnum.H5.getType())) { //如果是H5
            content.put("url", h5url);
        } else { //专题 或 合辑

            if (StringUtility.isNotEmpty(elementId) && StringUtility.isNotEmpty(appType) && StringUtility.isNotEmpty(previewImageUrl)) {
                if (content.containsKey("appType") && !appType.equals(content.getString("appType"))) {
                    result.put("error", "类型不符，一个专题不能有不同类型的APP");
                    result.put("status", -1);
                    return new ModelAndView("redirect:edit/" + bannerId);
                }
                content.put("appType", appType);
                JSONArray apps = content.containsKey("apps") ? content.getJSONArray("apps") : new JSONArray();
                JSONObject app = new JSONObject();
                app.put("elementId", elementId);
                app.put("previewImageUrl", previewImageUrl);
                apps.add(app);
                content.put("apps", apps);
            }
        }
        banner.setContent(content);
        bannerService.upsertBanner(banner);
        return new ModelAndView("redirect:edit/" + bannerId);
    }

    @RequestMapping(value = "banner/del/app/{bannerId}/{appId}", method = RequestMethod.GET)
    public @ResponseBody JSON bannerdelapp(@PathVariable("bannerId") String bannerId, @PathVariable String appId) {
        BannerEntity banner = bannerService.findByElementId(bannerId);
        JSONObject content = banner.getContent();
        JSONArray apps = content.containsKey("apps")?content.getJSONArray("apps"):new JSONArray();
        JSONArray apps_ = new JSONArray();
        for (int i = 0; i < apps.size(); i++) {
            JSONObject app = apps.getJSONObject(i);
            if(!app.getString("elementId").equals(appId)) {
                apps_.add(app);
            }
        }
        content.put("apps", apps_);
        banner.setContent(content);
        bannerService.upsertBanner(banner);
        return null;
    }
}
