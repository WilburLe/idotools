package com.toolbox.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toolbox.framework.utils.StringUtility;
import com.toolbox.framework.utils.UUIDUtility;
import com.toolbox.redis.RedisAppTabScheduled;
import com.toolbox.web.entity.AppTabEntity;
import com.toolbox.web.service.AppTabService;

/**
* @author E-mail:86yc@sina.com
* 
*/
@Controller
@RequestMapping("config")
public class ConfigTabController {
    @Autowired
    private AppTabService        tabEditService;
    @Autowired
    private RedisAppTabScheduled appTabScheduled;

    @RequestMapping(value = "tab", method = RequestMethod.GET)
    public ModelAndView tabview() {
        return new ModelAndView("config/tab").addObject("tabs", tabEditService.findAllTab());
    }

    @RequestMapping(value = "tab/add", method = RequestMethod.POST)
    public @ResponseBody JSON tabadd(String elementId, String code, String name, String apps, int sortNu) {
        AppTabEntity apptab = new AppTabEntity();
        if (StringUtility.isNotEmpty(elementId)) {
            apptab = tabEditService.findTabByElementId(elementId);
        }

        if (apptab == null) {
            apptab = new AppTabEntity();
        }
        apptab.setElementId(UUIDUtility.uuid32());
        apptab.setCode(code);
        apptab.setName(JSONObject.parseObject(name));
        apptab.setCreateDate(System.currentTimeMillis());
        apptab.setSortNu(sortNu);
        JSONArray arr = JSONArray.parseArray(apps);
        String[] apps_ = new String[arr.size()];
        for (int i = 0; i < arr.size(); i++) {
            apps_[i] = arr.getString(i);
        }
        apptab.setApps(apps_);
        if (apptab.getId() == null) {
            tabEditService.save(apptab);
        } else {
            Update update = new Update();
            update.set("apps", apptab.getApps());
            update.set("code", apptab.getCode());
            update.set("name", apptab.getName());
            update.set("sortNu", apptab.getSortNu());
            tabEditService.updateFirst(new Query(Criteria.where("elementId").is(elementId)), update);
        }
        //Redis 数据更新
        appTabScheduled.apptab();
        return null;
    }

    @RequestMapping(value = "tab/delete", method = RequestMethod.POST)
    public @ResponseBody JSON tabdelete(String elementId) {
        tabEditService.delTab(elementId);
        //Redis 数据更新
        appTabScheduled.apptab();
        return null;
    }

    @RequestMapping(value = "tab/byid/{elementId}")
    public @ResponseBody JSONObject tabByid(@PathVariable("elementId") String elementId) {
        return JSONObject.parseObject(JSONObject.toJSONString(tabEditService.findTabByElementId(elementId)));
    }

}
