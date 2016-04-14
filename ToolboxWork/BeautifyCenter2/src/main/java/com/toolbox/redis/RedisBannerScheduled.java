package com.toolbox.redis;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toolbox.common.AppEnum;
import com.toolbox.common.BannerEnum;
import com.toolbox.common.LanguageEnum;
import com.toolbox.common.SystemConfigEnum;
import com.toolbox.utils.JSONUtility;
import com.toolbox.web.entity.BannerContentEntity;
import com.toolbox.web.entity.BannerEntity;
import com.toolbox.web.entity.SystemConfigEmtity;
import com.toolbox.web.service.BannerContentService;
import com.toolbox.web.service.BannerService;
import com.toolbox.web.service.CommonJSONService;
import com.toolbox.web.service.RedisService;
import com.toolbox.web.service.SystemConfigService;

/**
* @author E-mail:86yc@sina.com
* 
*/
@Service
public class RedisBannerScheduled {
    private final static Log     log = LogFactory.getLog(RedisBannerScheduled.class);
    @Autowired
    private BannerService        bannerService;
    @Autowired
    private CommonJSONService    commonJSONService;
    @Autowired
    private BannerContentService bannerContentService;
    @Autowired
    private RedisService         redisService;
    @Autowired
    private SystemConfigService  configService;

    public void banner() {
        bannerConfig();
        banners();

        bannerConfigEn();
        bannersEn();
    }

    private void bannerConfig() {
        AppEnum[] apps = AppEnum.values();
        for (AppEnum app : apps) {
            String appType = app.getCollection();
            SystemConfigEmtity bannerConfig = configService.findByConfigType(SystemConfigEnum.config_banner.getType() + "_" + appType);
            if (bannerConfig == null) {
                continue;
            }
            JSONArray arr = new JSONArray();
            JSONObject configj = bannerConfig.getConfig();
            JSONArray cbanners = configj.containsKey("banners") ? configj.getJSONArray("banners") : new JSONArray();
            for (int i = 0; i < cbanners.size(); i++) {
                JSONObject banner = cbanners.getJSONObject(i);
                String bannerId = banner.getString("elementId");
                int sortNu = banner.getIntValue("sortNu");
                BannerEntity entity = bannerService.findByElementId(bannerId);
                JSONObject json = new JSONObject();
                json.put("sortNu", sortNu);
                json.put("elementId", entity.getElementId());
                json.put("previewImageUrl", entity.getPreviewImageUrl());
                json.put("enPreviewImageUrl", entity.getEnPreviewImageUrl());
                json.put("title", entity.getTitle());
                json.put("url", entity.getShareUrl());
                json.put("isOpenInBrowser", entity.isOpenInBrowser());
                arr.add(json);
            }
            JSONArray result = JSONUtility.asc(arr, "sortNu");
            redisService.set("zh_CN_banners_" + app.getCollection(), result.toJSONString());
        }
        log.info("------------ RedisBannerConfigScheduled cn cache success ------------");
    }

    private void banners() {
        List<BannerEntity> banners = bannerService.findAll();
        for (BannerEntity banner : banners) {
            JSONObject result = new JSONObject();
            String bannerType = banner.getBannerType();
            result.put("elementId", banner.getElementId());
            result.put("title", banner.getTitle());
            result.put("previewImageUrl", banner.getPreviewImageUrl());
            result.put("enPreviewImageUrl", banner.getEnPreviewImageUrl());
            result.put("bannerType", bannerType);
            result.put("isOpenInBrowser", banner.isOpenInBrowser());

            if (BannerEnum.H5.getType().equals(banner.getBannerType())) {
                JSONObject content = banner.getContent();
                result.put("data", content.getString("url"));
                redisService.set("zh_CN_banner_" + banner.getBannerType() + "_" + banner.getElementId(), result.toJSONString());
            } else {
                JSONArray data = new JSONArray();
                List<BannerContentEntity> contents = bannerContentService.findsByBannerId(banner.getElementId());
                for (int i = 0; i < contents.size(); i++) {
                    BannerContentEntity content = contents.get(i);
                    String colleationName = content.getAppType();
                    if (content.getAppType().equals(BannerEnum.Subject.getType())) {
                        colleationName = SystemConfigEnum.config_banner.getType();
                    }
                    String appStr = commonJSONService.findOne(new Query(Criteria.where("elementId").is(content.getAppId())), colleationName);
                    JSONObject json = JSONObject.parseObject(appStr);
                    String appType = content.getAppType();
                    if (BannerEnum.Subject.getType().equals(appType)) {
                        json.put("url", json.getString("shareUrl"));
                        json.put("type", "idotools_topic");
                    } else {
                        json = RedisAppUtil.getJSON(json, appType);
                    }
                    data.add(json);
                }
                result.put("data", data);

                String redis_key = null;
                if (BannerEnum.Subject.getType().equals(bannerType)) {
                    redis_key = "zh_CN_topic_";
                } else if (BannerEnum.Group.getType().equals(bannerType)) {
                    redis_key = "zh_CN_topicColl_";
                } else if (BannerEnum.H5.getType().equals(bannerType)) {
                    redis_key = "zh_CN_h5_";
                }
                redisService.set(redis_key + banner.getElementId(), result.toJSONString());
            }
        }
        log.info("------------ RedisBannerScheduled cn cache success ------------");
    }

    private void bannerConfigEn() {
        AppEnum[] apps = AppEnum.values();
        for (AppEnum app : apps) {
            String appType = app.getCollection();
            SystemConfigEmtity bannerConfig = configService.findByConfigType(SystemConfigEnum.config_banner.getType() + "_" + appType);
            if (bannerConfig == null) {
                continue;
            }
            JSONArray arr = new JSONArray();
            JSONObject configj = bannerConfig.getConfig();
            JSONArray cbanners = configj.containsKey("banners") ? configj.getJSONArray("banners") : new JSONArray();
            for (int i = 0; i < cbanners.size(); i++) {
                JSONObject banner = cbanners.getJSONObject(i);
                String bannerId = banner.getString("elementId");
                int sortNu = banner.getIntValue("sortNu");
                BannerEntity entity = bannerService.findByElementId(bannerId);
                JSONObject json = new JSONObject();
                json.put("sortNu", sortNu);
                json.put("elementId", entity.getElementId());
                json.put("previewImageUrl", entity.getEnPreviewImageUrl());
                json.put("title", entity.getTitle());
                json.put("url", entity.getShareUrl());
                json.put("isOpenInBrowser", entity.isOpenInBrowser());
                arr.add(json);
            }
            JSONArray result = JSONUtility.asc(arr, "sortNu");
            redisService.set(LanguageEnum.en_US.getCode() + "_banners_" + app.getCollection(), result.toJSONString());
        }
        log.info("------------ RedisBannerConfigScheduled en cache success ------------");
    }

    private void bannersEn() {
        List<BannerEntity> banners = bannerService.findAll();
        for (BannerEntity banner : banners) {
            JSONObject result = new JSONObject();
            String bannerType = banner.getBannerType();
            result.put("elementId", banner.getElementId());
            result.put("title", banner.getTitle());
            result.put("previewImageUrl", banner.getEnPreviewImageUrl());
            result.put("bannerType", bannerType);
            result.put("isOpenInBrowser", banner.isOpenInBrowser());

            if (BannerEnum.H5.getType().equals(banner.getBannerType())) {
                JSONObject content = banner.getContent();
                result.put("data", content.getString("url"));
                redisService.set(LanguageEnum.en_US.getCode() + "_banner_" + banner.getBannerType() + "_" + banner.getElementId(), result.toJSONString());
            } else {
                JSONArray data = new JSONArray();
                List<BannerContentEntity> contents = bannerContentService.findsByBannerId(banner.getElementId());
                for (int i = 0; i < contents.size(); i++) {
                    BannerContentEntity content = contents.get(i);
                    String colleationName = content.getAppType();
                    if (content.getAppType().equals(BannerEnum.Subject.getType())) {
                        colleationName = SystemConfigEnum.config_banner.getType();
                    }
                    String appStr = commonJSONService.findOne(new Query(Criteria.where("elementId").is(content.getAppId())), colleationName);
                    JSONObject json = JSONObject.parseObject(appStr);
                    String appType = content.getAppType();
                    if (BannerEnum.Subject.getType().equals(appType)) {
                        json.put("url", json.getString("shareUrl"));
                        json.put("type", "idotools_topic");
                    } else {
                        json = RedisAppUtil.getJSON(json, appType);
                    }
                    data.add(json);
                }
                result.put("data", data);

                String redis_key = LanguageEnum.en_US.getCode();
                if (BannerEnum.Subject.getType().equals(bannerType)) {
                    redis_key += "_topic_";
                } else if (BannerEnum.Group.getType().equals(bannerType)) {
                    redis_key += "_topicColl_";
                } else if (BannerEnum.H5.getType().equals(bannerType)) {
                    redis_key += "_h5_";
                }
                redisService.set(redis_key + banner.getElementId(), result.toJSONString());
            }
        }
        log.info("------------ RedisBannerScheduled en cache success ------------");
    }
}
