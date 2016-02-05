package com.toolbox.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.toolbox.entity.tag.AppTagGroupEntity;
import com.toolbox.entity.tag.AppTagEntity;
import com.toolbox.framework.utils.UUIDUtility;
import com.toolbox.service.TagEditService;

/**
* @author E-mail:86yc@sina.com
* 
*/
@Controller
public class TagEditController {
    @Autowired
    private TagEditService tagEditService;

    @RequestMapping(value = "tag/view/", method = RequestMethod.GET)
    public ModelAndView tagview() {
        return new ModelAndView("tag/view").addObject("tags", tagEditService.findAllTag());
    }

    @RequestMapping(value = "tag/add/", method = RequestMethod.POST)
    public @ResponseBody JSON tagadd(String tagdata) {
        JSONObject tagJSON = JSON.parseObject(tagdata);
        String appType = tagJSON.getString("appType");

        AppTagGroupEntity apptag = tagEditService.findTagByAppType(appType);
        if (apptag == null) {
            apptag = new AppTagGroupEntity();
            apptag.setTags(new ArrayList<AppTagEntity>());
            apptag.setAppType(appType);
            apptag.setId(UUIDUtility.uuid32());
        }
        JSONObject tag = new JSONObject();
        tag.put("name", tagJSON.getJSONObject("name"));
        tag.put("uuid", UUIDUtility.uuid32());
        AppTagEntity tagd = JSONObject.parseObject(tag.toJSONString(), AppTagEntity.class);
        apptag.getTags().add(tagd);
        tagEditService.upsertTag(apptag);
        return null;
    }

    @RequestMapping(value = "tag/delete/", method = RequestMethod.POST)
    public @ResponseBody JSON tagdelete(String appType, String tagUuid) {
        AppTagGroupEntity apptag = tagEditService.findTagByAppType(appType);
        List<AppTagEntity> tags = apptag.getTags();
        for (AppTagEntity tag : tags) {
            if(tag.getUuid().equals(tagUuid)) {
                tags.remove(tag);
                break;
            }
        }
        tagEditService.upsertTag(apptag);
        return null;
    }
}
