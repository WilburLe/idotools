package com.toolbox.controller;
/**
* @author E-mail:86yc@sina.com
* 
*/

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.toolbox.common.CollectionEnum;
import com.toolbox.framework.utils.StringUtility;
import com.toolbox.service.TagEditService;
import com.toolbox.service.WallpaperService;

@Controller
public class WallpaperController {
    @Autowired
    private WallpaperService wallpaperService;
    @Autowired
    private TagEditService   tagEditService;

    @RequestMapping(value = "wallpaper/upload", method = RequestMethod.GET)
    public ModelAndView uploadpage() {
        JSONObject tags = tagEditService.findTagByName(CollectionEnum.wallpaper.getCollection());
        return new ModelAndView("upload").addObject("tablename", CollectionEnum.wallpaper.getCollection()).addObject("tags", tags);
    }

    @RequestMapping(value = "wallpaper/view/{tag}/{start}")
    public ModelAndView wallpaperView(@PathVariable("tag") String tag, @PathVariable("start") int start) {
        JSONObject tags = tagEditService.findTagByName(CollectionEnum.wallpaper.getCollection());
        List<JSONObject> wallpapers = wallpaperService.findByPage(tag, start, 100);
        return new ModelAndView("wall/wallpaperView").addObject("wallpapers", wallpapers).addObject("tags", tags);
    }

    @RequestMapping(value = "wallpaper/info/{elementId}")
    public ModelAndView wallpaperInfo(@PathVariable("elementId") String elementId) {
        JSONObject tags = tagEditService.findTagByName(CollectionEnum.wallpaper.getCollection());
        return new ModelAndView("wall/wallpaperInfo").addObject("wallpaper", wallpaperService.findByElementId(elementId)).addObject("tags", tags);
    }

    @RequestMapping(value = "wallpaper/changetag/", method = RequestMethod.POST)
    public @ResponseBody JSON wallpaperChangeTag(String elementId, String tags) {
        tags = StringUtility.isEmpty(tags) ? "[]" : tags;
        JSONObject data = wallpaperService.findByElementId(elementId);
        data.put("tags", JSON.parseArray(tags));
        wallpaperService.update(data);
        return null;
    }

    @RequestMapping(value = "wallpaper/delete/{elementId}", method = RequestMethod.GET)
    public @ResponseBody JSON delwallpaper(@PathVariable("elementId") String elementId) {
        wallpaperService.deleteByElementId(elementId);
        return null;
    }
}
