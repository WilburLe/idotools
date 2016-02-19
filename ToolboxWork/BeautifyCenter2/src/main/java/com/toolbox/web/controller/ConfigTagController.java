package com.toolbox.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
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
    private AppTagService tagEditService;

    @RequestMapping(value = "tag", method = RequestMethod.GET)
    public ModelAndView tagview() {
        return new ModelAndView("config/tag").addObject("tags", tagEditService.findAllTag());
    }

    @RequestMapping(value = "tag/add", method = RequestMethod.POST)
    public @ResponseBody JSON tagadd(String appType, String name) {
        AppTagEntity apptag = new AppTagEntity();
        apptag.setElementId(UUIDUtility.uuid32());
        apptag.setAppType(appType);
        apptag.setName(JSONObject.parseObject(name));
        tagEditService.save(apptag);
        return null;
    }

    @RequestMapping(value = "tag/delete", method = RequestMethod.POST)
    public @ResponseBody JSON tagdelete(String elementId) {
        tagEditService.delTag(elementId);
        return null;
    }
}
