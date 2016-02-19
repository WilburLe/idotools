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
import com.toolbox.common.BannerResourceEnum;
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

    @RequestMapping(value = "banner/view", method = RequestMethod.GET)
    public ModelAndView bannerview() {
        List<BannerEntity> banners = bannerService.findAll();
        return new ModelAndView("banner/view").addObject("banners", banners);
    }

    @RequestMapping(value = "banner/find/{elementId}", method = RequestMethod.GET)
    public @ResponseBody BannerEntity bannerFindByEleId(@PathVariable("elementId") String elementId) {
        BannerEntity banner = bannerService.findByElementId(elementId);
        return banner;
    }

    @RequestMapping(value = "banner/add", method = RequestMethod.POST)
    public ModelAndView banneradd(HttpServletRequest request, String title, String intro, String url) {
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
        banner.setCreateDate(System.currentTimeMillis());
        banner.setElementId(UUIDUtility.uuid32());
        banner.setIntro(intro);
        banner.setPreviewImageUrl(coverPath);
        banner.setTitle(title);
        banner.setUrl(url);
        bannerService.save(banner);
        return new ModelAndView("redirect:view/");
    }

    @RequestMapping(value = "banner/edit/{elementId}", method = RequestMethod.GET)
    public ModelAndView banneredit(@PathVariable("elementId") String elementId) {
        BannerEntity banner = bannerService.findByElementId(elementId);
        return new ModelAndView("banner/edit").addObject("banner", banner);
    }

    @RequestMapping(value = "banner/edit", method = RequestMethod.POST)
    public ModelAndView banneredit(HttpServletRequest request, String elementId, String title, String intro, String url) {
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
        BannerEntity banner = bannerService.findByElementId(elementId);
        //封面存在的情况下更新封面
        if (cover != null) {
            String coverPath = UploadUtility.upload_file(cover, UploadUtility.banner_voer_path);
            banner.setPreviewImageUrl(coverPath);
        }
        banner.setTitle(title);
        banner.setIntro(intro);
        banner.setUrl(url);
        bannerService.upsertBanner(banner);
        return new ModelAndView("redirect:edit/" + elementId);
    }

    @RequestMapping(value = "banner/del/{elementId}", method = RequestMethod.GET)
    public @ResponseBody JSON bannerdel(@PathVariable("elementId") String elementId) {
        bannerService.delBanner(elementId);
        return null;
    }

}
