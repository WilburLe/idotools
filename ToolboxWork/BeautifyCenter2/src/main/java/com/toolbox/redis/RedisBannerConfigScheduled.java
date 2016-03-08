package com.toolbox.redis;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toolbox.common.AppEnum;
import com.toolbox.common.SystemConfigEnum;
import com.toolbox.utils.JSONUtility;
import com.toolbox.web.entity.BannerEntity;
import com.toolbox.web.entity.SystemConfigEmtity;
import com.toolbox.web.service.BannerService;
import com.toolbox.web.service.SystemConfigService;

/**
* @author E-mail:86yc@sina.com
* 
*/
@Service
public class RedisBannerConfigScheduled extends AbstractRedisService<String, String> {
    private final static Log    log = LogFactory.getLog(RedisBannerConfigScheduled.class);
    @Autowired
    private SystemConfigService configService;
    @Autowired
    private BannerService       bannerService;

    @Scheduled(fixedRate = 1000 * 60 * 5)
    public void banner() {
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
                json.put("title", entity.getTitle());
                json.put("url", entity.getShareUrl());
                arr.add(json);
            }
            JSONArray result = JSONUtility.asc(arr, "sortNu");
            this.set("zh_CN_banners_" + app.getCollection(), result.toJSONString());
        }
        log.info("redis >>> zh_CN_banners cache success ~");
    }

}
