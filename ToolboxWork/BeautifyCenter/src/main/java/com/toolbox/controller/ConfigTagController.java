package com.toolbox.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toolbox.framework.utils.UUIDUtility;
import com.toolbox.service.TagEditService;

/**
* @author E-mail:86yc@sina.com
* 
*/
@RequestMapping("config/")
@Controller
public class ConfigTagController {
    @Autowired
    private TagEditService tagEditService;

    @RequestMapping(value = "tag", method = RequestMethod.GET)
    public ModelAndView tagview() {
        return new ModelAndView("config/tag").addObject("tags", tagEditService.findAllTag());
    }

    @RequestMapping(value = "tag/add/", method = RequestMethod.POST)
    public @ResponseBody JSON tagadd(String tag) {
        JSONObject tagData = JSON.parseObject(tag);
        String tablename = tagData.getString("tablename");
        tagData.remove("tablename");
        JSONObject data = tagEditService.findTagByName(tablename);
        if(data == null) {
            data = new JSONObject();
            data.put("name", tablename);
        }
        JSONArray arr = data.getJSONArray("arr");
        if(arr == null) {
            arr = new JSONArray();
        }
        tagData.put("uuid", UUIDUtility.uuid32());
        arr.add(tagData);
        data.put("arr", arr);
        if(data.containsKey("_id")) {
            tagEditService.update(data);
        } else {
            tagEditService.save(data);
        }
        return null;
    }

    @RequestMapping(value = "tag/delete/", method = RequestMethod.POST)
    public @ResponseBody JSON tagdelete(String name, String uuid) {
        tagEditService.delete(name, uuid);
        return null;
    }
}
