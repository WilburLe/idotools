package com.toolbox.web.controller;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
import com.alibaba.fastjson.JSONObject;
import com.toolbox.common.AppMarketEnum;
import com.toolbox.common.BannerEnum;
import com.toolbox.common.SystemConfigEnum;
import com.toolbox.framework.utils.ConfigUtility;
import com.toolbox.framework.utils.StringUtility;
import com.toolbox.framework.utils.UUIDUtility;
import com.toolbox.redis.RedisBannerScheduled;
import com.toolbox.utils.UploadUtility;
import com.toolbox.web.entity.BannerContentEntity;
import com.toolbox.web.entity.BannerEntity;
import com.toolbox.web.service.BannerContentService;
import com.toolbox.web.service.BannerService;
import com.toolbox.web.service.CommonJSONService;

/**
* @author E-mail:86yc@sina.com
* 
*/
@Controller
@RequestMapping("banner")
public class BannerController {
    @Autowired
    private BannerService        bannerService;
    @Autowired
    private BannerContentService bannerContentService;
    @Autowired
    private CommonJSONService    commonJSONService;
    @Autowired
    private RedisBannerScheduled redisBannerScheduled;

    @RequestMapping(value = "{bannerType}/view/{market}", method = RequestMethod.GET)
    public ModelAndView bannerview(@PathVariable("bannerType") String bannerType, @PathVariable("market") String market) {
        List<BannerEntity> banners = bannerService.findByBannerType(bannerType, market);
        return new ModelAndView("banner/view").addObject("banners", banners).addObject("bannerType", bannerType);
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public ModelAndView add(HttpServletRequest request, String bannerType, String title, String intro, String h5url, String market, boolean isOpenInBrowser) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Iterator<String> it = multipartRequest.getFileNames();
        Map<String, MultipartFile> covers = new HashMap<String, MultipartFile>();
        while (it.hasNext()) {
            String filenindex = it.next();
            MultipartFile file = multipartRequest.getFile(filenindex);
            if (!file.isEmpty()) {
                covers.put(file.getName(), file);
                break;
            }
        }

        String coverPath = null;
        if (covers.containsKey("cover")) {
            coverPath = UploadUtility.upload_file(covers.get("cover"), UploadUtility.banner_voer_path);
        }

        String bannerId = UUIDUtility.uuid32();
        BannerEntity banner = new BannerEntity();
        banner.setCreateDate(System.currentTimeMillis());
        banner.setElementId(bannerId);
        banner.setPreviewImageUrl(coverPath);
        banner.setOpenInBrowser(isOpenInBrowser);

        banner.setTitle(title);
        banner.setBannerType(bannerType);
        String shareUrl = ConfigUtility.getInstance().getString("app.service.cn.share.url");
        if(AppMarketEnum.GooglePlay.getCode().equals(market)) {
            shareUrl = ConfigUtility.getInstance().getString("app.service.en.share.url");
        }
        
        if (BannerEnum.H5.getType().equals(bannerType)) {
            //            shareUrl += "h5?id=" + bannerId;
            shareUrl = h5url;
            JSONObject content = new JSONObject();
            content.put("url", h5url);
            banner.setContent(content);
        } else if (BannerEnum.Subject.getType().equals(bannerType)) {
            shareUrl += "topic?id=" + bannerId;
        } else if (BannerEnum.Group.getType().equals(bannerType)) {
            shareUrl += "topicColl?id=" + bannerId;
        }
        banner.setMarket(market);
        banner.setShareUrl(shareUrl);
        bannerService.save(banner);
        //更新redis
        redisBannerScheduled.banner();
        return new ModelAndView("redirect:" + bannerType + "/view/");
    }

    @RequestMapping(value = "edit/{elementId}", method = RequestMethod.GET)
    public ModelAndView editPage(@PathVariable("elementId") String elementId) {
        BannerEntity banner = bannerService.findByElementId(elementId);
        List<BannerContentEntity> contents = bannerContentService.findsByBannerId(elementId);
        Map<String, JSONObject> contentMap = new HashMap<String, JSONObject>();
        for (int i = 0; i < contents.size(); i++) {
            BannerContentEntity content = contents.get(i);
            String colleationName = content.getAppType();
            if (content.getAppType().equals(BannerEnum.Subject.getType())) {
                colleationName = SystemConfigEnum.config_banner.getType();
            }
            String data = commonJSONService.findOne(new Query(Criteria.where("elementId").is(content.getAppId())), colleationName);
            contentMap.put(content.getAppId(), JSONObject.parseObject(data));
        }
        return new ModelAndView("banner/edit").addObject("banner", banner).addObject("contents", contents).addObject("contentMap", contentMap);
    }

    @RequestMapping(value = "edit", method = RequestMethod.POST)
    public ModelAndView edit(HttpServletRequest request, String elementId, String title, String h5url, String intro, String market, boolean isOpenInBrowser) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Iterator<String> it = multipartRequest.getFileNames();
        Map<String, MultipartFile> covers = new HashMap<String, MultipartFile>();
        while (it.hasNext()) {
            String filenindex = it.next();
            MultipartFile file = multipartRequest.getFile(filenindex);
            if (!file.isEmpty()) {
                covers.put(file.getName(), file);
            }
        }
        String coverPath = null;
        if (covers.containsKey("cover")) {
            coverPath = UploadUtility.upload_file(covers.get("cover"), UploadUtility.banner_voer_path);
        }

        BannerEntity banner = bannerService.findByElementId(elementId);
        if (StringUtility.isNotEmpty(coverPath)) {
            banner.setPreviewImageUrl(coverPath);
        }
        banner.setTitle(title);
        banner.setOpenInBrowser(isOpenInBrowser);
        JSONObject content = banner.getContent();
        content = content == null ? new JSONObject() : content;
     
        String shareUrl = ConfigUtility.getInstance().getString("app.service.cn.share.url");
        if(AppMarketEnum.GooglePlay.getCode().equals(market)) {
            shareUrl = ConfigUtility.getInstance().getString("app.service.en.share.url");
        }
        if (BannerEnum.H5.getType().equals(banner.getBannerType())) {
            content.put("url", h5url);
            shareUrl = h5url;
        }else if (BannerEnum.Subject.getType().equals(banner.getBannerType())) {
            shareUrl += "topic?id=" + banner.getElementId();
        } else if (BannerEnum.Group.getType().equals(banner.getBannerType())) {
            shareUrl += "topicColl?id=" + banner.getElementId();
        }
        banner.setShareUrl(shareUrl);
        banner.setMarket(market);
        banner.setContent(content);
        bannerService.upsertBanner(banner);

        //更新redis
        redisBannerScheduled.banner();
        return new ModelAndView("redirect:edit/" + elementId);
    }

    @RequestMapping(value = "edit/content", method = RequestMethod.POST)
    public ModelAndView editContent(String bannerId, String appType, String appId) {
        BannerContentEntity content = new BannerContentEntity();
        content.setAppId(appId);
        content.setBannerId(bannerId);
        content.setAppType(appType);
        bannerContentService.save(content);
        //更新redis
        redisBannerScheduled.banner();
        return new ModelAndView("redirect:/banner/edit/" + bannerId);
    }

    @RequestMapping(value = "del/{elementId}", method = RequestMethod.GET)
    public @ResponseBody JSON del(@PathVariable("elementId") String elementId) {
        commonJSONService.delApp("banner", elementId);
        //更新redis
        redisBannerScheduled.banner();
        return null;
    }

    @RequestMapping(value = "search", method = RequestMethod.POST)
    public @ResponseBody String search(String appType, String elementId) {
        if (StringUtility.isEmpty(appType)) {
            return null;
        }
        Query query = new Query();
        query.addCriteria(Criteria.where("elementId").is(elementId));
        if (BannerEnum.Subject.getType().equals(appType)) {
            appType = "banner";
        }
        String app = commonJSONService.findOne(query, appType);
        return app;
    }

    @RequestMapping(value = "app/del", method = RequestMethod.POST)
    public @ResponseBody JSON appDel(String bannerId, String appId) {
        bannerContentService.del(bannerId, appId);

        redisBannerScheduled.banner();
        return null;
    }

}
