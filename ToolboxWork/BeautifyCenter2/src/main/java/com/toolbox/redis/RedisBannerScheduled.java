package com.toolbox.redis;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toolbox.common.BannerEnum;
import com.toolbox.common.SystemConfigEnum;
import com.toolbox.web.entity.BannerContentEntity;
import com.toolbox.web.entity.BannerEntity;
import com.toolbox.web.service.BannerContentService;
import com.toolbox.web.service.BannerService;
import com.toolbox.web.service.CommonJSONService;
import com.toolbox.web.service.RedisService;

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

    @Scheduled(fixedRate = 1000 * 60 * 40)
    public void banner() {
        List<BannerEntity> banners = bannerService.findAll();
        for (BannerEntity banner : banners) {
            JSONObject result = new JSONObject();
            String bannerType = banner.getBannerType();
            result.put("elementId", banner.getElementId());
            result.put("title", banner.getTitle());
            result.put("previewImageUrl", banner.getPreviewImageUrl());
            result.put("enPreviewImageUrl", banner.getEnPreviewImageUrl());
            result.put("bannerType", bannerType);

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
        log.info("------------ RedisBannerScheduled cache success ------------");
    }

}
