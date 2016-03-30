package com.toolbox.redis;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.toolbox.common.TabEnum;
import com.toolbox.web.entity.AppTabEntity;
import com.toolbox.web.service.AppTabService;
import com.toolbox.web.service.RedisService;

/**
* @author E-mail:86yc@sina.com
* 
*/
@Service
public class RedisAppTabScheduled {
    private final static Log log = LogFactory.getLog(RedisAppTabScheduled.class);
    @Autowired
    private AppTabService    tabEditService;
    @Autowired
    private RedisService     redisService;

    public void apptab() {
        List<AppTabEntity> tabs = tabEditService.findAllTab();
        TabEnum[] apps = TabEnum.values();
        for (TabEnum app : apps) {
            List<JSONObject> list = new ArrayList<JSONObject>();
            JSONObject in8n = new JSONObject();
            for (AppTabEntity tab : tabs) {
                String[] sapps = tab.getApps();
                if (sapps == null || sapps.length == 0) {
                    continue;
                }
                for (String sapp : sapps) {
                    if (app.getCollection().equals(sapp)) {
                        JSONObject json = new JSONObject();
                        json.put("titleKey", tab.getCode());
                        json.put("loadUrl", "javascript:WebInterface.toPage('" + tab.getCode() + "')");
                        list.add(json);

                        in8n.put(tab.getCode(), tab.getName());
                        break;
                    }
                }
            }
            JSONObject data = new JSONObject();
            data.put("timestamp", System.currentTimeMillis());
            data.put("list", list);
            data.put("i18n", in8n);
            redisService.set("tabs_" + app.getCollection(), data.toJSONString());
        }
        log.info("redis >>> tabs cache success ~");
    }
}
