package com.toolbox.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.toolbox.framework.utils.UUIDUtility;
import com.toolbox.web.entity.AppTagEntity;
import com.toolbox.web.service.AppTagService;

/**
* @author E-mail:86yc@sina.com
* 
*/
@Controller
@RequestMapping("config")
public class ConfigTagController {
    @Autowired
    private AppTagService appTagService;

    @RequestMapping(value = "tag", method = RequestMethod.GET)
    public ModelAndView tagview() {
        return new ModelAndView("config/tag").addObject("tags", appTagService.findAllTag());
    }

    @RequestMapping(value = "tag/add", method = RequestMethod.POST)
    public @ResponseBody JSON tagadd(String elementId, String appType, int sortNu, String name) {
        AppTagEntity apptag = appTagService.findByElementId(elementId);
        if (apptag == null) {
            apptag = new AppTagEntity();
        }
        apptag.setElementId(UUIDUtility.uuid32());
        apptag.setAppType(appType);
        apptag.setSortNu(sortNu);
        apptag.setName(JSONObject.parseObject(name));
        if (apptag.getId() == null) {
            appTagService.save(apptag);
        } else {
            Update update = new Update();
            update.set("sortNu", sortNu);
            update.set("name", JSONObject.parseObject(name));
            appTagService.updateFirst(new Query(Criteria.where("elementId").is(elementId)), update);
        }
        return null;
    }

    @RequestMapping(value = "tag/delete", method = RequestMethod.POST)
    public @ResponseBody JSON tagdelete(String elementId, int status) {
        Update update = new Update();
        update.set("status", status);
        appTagService.updateFirst(new Query(Criteria.where("elementId").is(elementId)), update);
        return null;
    }
}
