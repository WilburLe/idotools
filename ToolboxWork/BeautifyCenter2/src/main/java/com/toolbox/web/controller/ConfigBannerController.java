package com.toolbox.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toolbox.common.SystemConfigEnum;
import com.toolbox.redis.RedisBannerScheduled;
import com.toolbox.utils.JSONUtility;
import com.toolbox.web.entity.BannerEntity;
import com.toolbox.web.entity.SystemConfigEmtity;
import com.toolbox.web.service.BannerService;
import com.toolbox.web.service.SystemConfigService;

/**
* @author E-mail:86yc@sina.com
* 
*/
@Controller
@RequestMapping("config")
public class ConfigBannerController {
    @Autowired
    private SystemConfigService configService;
    @Autowired
    private BannerService       bannerService;
    @Autowired
    private RedisBannerScheduled redisBannerScheduled;
    
    @RequestMapping(value = "banner/{market}", method = RequestMethod.GET)
    public ModelAndView banner(@PathVariable("market") String market) {
        SystemConfigEmtity bannerConfig = configService.findByConfigType(SystemConfigEnum.config_banner.getType() + "_" + market);
        List<BannerEntity> banners = new ArrayList<BannerEntity>();
        JSONArray result = new JSONArray();
        if (bannerConfig != null) {
            JSONObject configj = bannerConfig.getConfig();
            JSONArray cbanners = configj.containsKey("banners") ? configj.getJSONArray("banners") : new JSONArray();
            JSONArray delBanners = new JSONArray();
            for (int i = 0; i < cbanners.size(); i++) {
                JSONObject banner = cbanners.getJSONObject(i);
                String bannerId = banner.getString("elementId");
                int sortNu = banner.getIntValue("sortNu");
                BannerEntity entity = bannerService.findByElementId(bannerId);
                if (entity != null) {
                    banners.add(entity);
                    JSONObject json = JSONObject.parseObject(JSONObject.toJSONString(entity));
                    json.put("sortNu", sortNu);
                    result.add(json);
                } else {
                    delBanners.add(banner);
                }
            }

            if (delBanners.size() > 0) {
                cbanners.removeAll(delBanners);
                configj.put("banners", cbanners);
                bannerConfig.setConfig(configj);
                configService.updateInser(bannerConfig);
            }
        }
        return new ModelAndView("config/banner").addObject("bannerConfig", bannerConfig).addObject("market", market).addObject("banners", JSONUtility.asc(result, "sortNu"));
    }

    @RequestMapping(value = "banner/add", method = RequestMethod.POST)
    public @ResponseBody JSON banneradd(String market, String bannerId) {
        String configType = SystemConfigEnum.config_banner.getType() + "_" + market;
        SystemConfigEmtity bannerConfig = configService.findByConfigType(configType);
        JSONObject config = new JSONObject();
        if (bannerConfig == null) {
            bannerConfig = new SystemConfigEmtity();
            bannerConfig.setConfigType(configType);
        } else {
            config = bannerConfig.getConfig();
        }
        JSONArray banners = config.containsKey("banners") ? config.getJSONArray("banners") : new JSONArray();
        JSONObject banner = new JSONObject();
        banner.put("elementId", bannerId);
        banner.put("sortNu", banners.size());
        banners.add(banner);
        config.put("banners", banners);
        bannerConfig.setConfig(config);
        configService.updateInser(bannerConfig);
        
        redisBannerScheduled.banner();
        return null;
    }

    @RequestMapping(value = "banner/edit", method = RequestMethod.POST)
    public @ResponseBody JSON banneredit(String market, String bannerId, int sortNu) {
        String configType = SystemConfigEnum.config_banner.getType() + "_" + market;
        SystemConfigEmtity bannerConfig = configService.findByConfigType(configType);
        JSONObject config =  bannerConfig.getConfig();
        JSONArray banners = config.getJSONArray("banners");
        for (int i = 0; i < banners.size(); i++) {
            JSONObject banner = banners.getJSONObject(i);
            if (!banner.getString("elementId").equals(bannerId)) {
                continue;
            }
            banner.put("sortNu", sortNu);
        }
        config.put("banners", banners);
        bannerConfig.setConfig(config);
        configService.updateInser(bannerConfig);
        
        redisBannerScheduled.banner();
        return null;
    }

    @RequestMapping(value = "banner/del/{market}/{bannerId}", method = RequestMethod.GET)
    public @ResponseBody JSON bannerdel(@PathVariable("market") String market, @PathVariable("bannerId") String bannerId) {
        String configType = SystemConfigEnum.config_banner.getType() + "_" + market;
        SystemConfigEmtity bannerConfig = configService.findByConfigType(configType);
        JSONObject config = new JSONObject();
        if (bannerConfig == null) {
            bannerConfig = new SystemConfigEmtity();
            bannerConfig.setConfigType(configType);
        } else {
            config = bannerConfig.getConfig();
        }
        JSONArray banners = config.containsKey("banners") ? config.getJSONArray("banners") : new JSONArray();
        JSONArray banners_ = new JSONArray();
        for (int i = 0; i < banners.size(); i++) {
            JSONObject banner = banners.getJSONObject(i);
            if (banner.getString("elementId").equals(bannerId)) {
                continue;
            }
            banners_.add(banner);
        }
        config.put("banners", banners_);
        bannerConfig.setConfig(config);
        configService.updateInser(bannerConfig);

        redisBannerScheduled.banner();
        return null;
    }
}
