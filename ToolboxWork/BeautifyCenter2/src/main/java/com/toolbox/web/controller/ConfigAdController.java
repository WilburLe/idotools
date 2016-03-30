package com.toolbox.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.toolbox.common.SystemConfigEnum;
import com.toolbox.web.entity.SystemConfigEmtity;
import com.toolbox.web.service.RedisService;
import com.toolbox.web.service.SystemConfigService;

/**
* @author E-mail:86yc@sina.com
* 
*/
@Controller
@RequestMapping("config")
public class ConfigAdController {
    @Autowired
    private SystemConfigService systemConfigService;
    @Autowired
    private RedisService        redisService;

    @RequestMapping(value = "ad", method = RequestMethod.GET)
    public ModelAndView adview() {
        SystemConfigEmtity adconfig = systemConfigService.findByConfigType(SystemConfigEnum.config_ad.getType());
        return new ModelAndView("config/ad").addObject("adconfig", adconfig);
    }

    @RequestMapping(value = "ad/save", method = RequestMethod.POST)
    public ModelAndView adsave(int isopen, int count) {
        SystemConfigEmtity adconfig = systemConfigService.findByConfigType(SystemConfigEnum.config_ad.getType());
        if (adconfig == null) {
            adconfig = new SystemConfigEmtity();
            adconfig.setConfigType(SystemConfigEnum.config_ad.getType());
        }
        JSONObject config = new JSONObject();
        config.put("count", count);
        config.put("isopen", isopen == 0 ? false : true);
        adconfig.setConfig(config);
        systemConfigService.updateInser(adconfig);

        //update redis
        redisService.set("adconfig", config.toJSONString());
        return new ModelAndView("redirect:/config/ad");
    }
}
