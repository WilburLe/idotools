package com.toolbox.redis;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toolbox.common.AppEnum;
import com.toolbox.common.BannerResourceEnum;
import com.toolbox.common.SystemConfigEnum;
import com.toolbox.framework.utils.RandomUtility;
import com.toolbox.web.entity.BannerEntity;
import com.toolbox.web.entity.SystemConfigEmtity;
import com.toolbox.web.service.BannerService;
import com.toolbox.web.service.CommonJSONService;
import com.toolbox.web.service.SystemConfigService;

/**
* @author E-mail:86yc@sina.com
* 
*/
@Service
public class RedisBannerScheduled extends AbstractRedisService<String, String> {
    private final static Log    log = LogFactory.getLog(RedisBannerScheduled.class);
    @Autowired
    private BannerService       bannerService;
    @Autowired
    private SystemConfigService configService;
    @Autowired
    private CommonJSONService   commonJSONService;

//    @Scheduled(fixedRate = 1000 * 60 * 5)
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
                json.put("url", entity.getUrl());
                arr.add(json);
            }
            JSONArray result = asc(arr, null, "sortNu");
            this.set("zh_CN_banners_" + app.getCollection(), result.toJSONString());
        }
        log.info("redis >>> banners cache success ~");
    }

//    @Scheduled(fixedRate = 1000 * 60 * 5)
    public void bannerContent() {
    }

    private static JSONArray result = new JSONArray();

    private static JSONArray asc(JSONArray arr, JSONObject max, String key) {
        result = desc(arr, max, key);
        JSONArray result_ = new JSONArray();
        for (int i = result.size() - 1; i >= 0; i--) {
            result_.add(result.get(i));
        }
        return result_;
    }

    private static JSONArray desc(JSONArray arr, JSONObject max, String key) {
        if (max != null) {
            result.add(max);
        }
        if (arr == null || arr.size() == 0) {
            return result;
        }
        int temp = 0;
        for (int i = 0; i < arr.size(); i++) {
            JSONObject json = arr.getJSONObject(i);
            int sortNu = json.getIntValue(key);
            if (sortNu >= temp) {
                temp = sortNu;
                max = json;
            }
        }
        arr.remove(max);
        return desc(arr, max, key);
    }

    public static void main(String[] args) {
        JSONArray arr = new JSONArray();
        for (int i = 0; i < 10; i++) {
            JSONObject json = new JSONObject();
            int num = RandomUtility.nextInt(100);
            json.put("sortNu", num);
            arr.add(json);
            System.out.println(num);
        }

        System.out.println("####################################");
        JSONArray result = asc(arr, null, "sortNu");

        for (int i = 0; i < result.size(); i++) {
            JSONObject json = result.getJSONObject(i);
            int sortNu = json.getIntValue("sortNu");
            System.out.println(sortNu);
        }
    }
}
