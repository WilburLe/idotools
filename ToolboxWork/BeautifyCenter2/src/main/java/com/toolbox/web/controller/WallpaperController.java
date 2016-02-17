package com.toolbox.web.controller;
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
import com.toolbox.common.AppEnum;
import com.toolbox.web.entity.AppTagEntity;
import com.toolbox.web.entity.WallpaperEntity;
import com.toolbox.web.service.AppTagService;
import com.toolbox.web.service.WallpaperService;

@Controller
public class WallpaperController {
    @Autowired
    private WallpaperService wallpaperService;
    @Autowired
    private AppTagService   tagEditService;

    private final static String collection_name = AppEnum.wallpaper.getCollection();
    
    @RequestMapping(value = "wallpaper/upload", method = RequestMethod.GET)
    public ModelAndView uploadpage() {
        List<AppTagEntity> apptags = tagEditService.findTagByAppType(collection_name);
        return new ModelAndView("upload").addObject("tablename", collection_name).addObject("apptags", apptags);
    }

    @RequestMapping(value = "wallpaper/view/{tag}/{start}")
    public ModelAndView wallpaperView(@PathVariable("tag") String tag, @PathVariable("start") int start) {
        List<AppTagEntity> apptags = tagEditService.findTagByAppType(collection_name);
        List<WallpaperEntity> wallpapers = wallpaperService.findByPage(tag, start, 100);
        return new ModelAndView("wall/wallpaperView").addObject("wallpapers", wallpapers).addObject("apptags", apptags);
    }

    @RequestMapping(value = "wallpaper/info/{elementId}")
    public ModelAndView wallpaperInfo(@PathVariable("elementId") String elementId) {
        List<AppTagEntity> apptags = tagEditService.findTagByAppType(collection_name);
        return new ModelAndView("wall/wallpaperInfo").addObject("wallpaper", wallpaperService.findByElementId(elementId)).addObject("apptags", apptags);
    }

    @RequestMapping(value = "wallpaper/changetag/", method = RequestMethod.POST)
    public @ResponseBody JSON wallpaperChangeTag(String elementId, String tags) {
        wallpaperService.updateTags(elementId, tags);
        return null;
    }

    @RequestMapping(value = "wallpaper/delete/{elementId}", method = RequestMethod.GET)
    public @ResponseBody JSON delwallpaper(@PathVariable("elementId") String elementId) {
        wallpaperService.deleteByElementId(elementId);
        return null;
    }
}
