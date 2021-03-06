package com.toolbox.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.toolbox.web.service.HotRankService;
import com.toolbox.web.service.StatService;

/**
* @author E-mail:86yc@sina.com
* 
*/
@Controller
public class RefreshDataController {
    @Autowired
    private StatService    statService;
    @Autowired
    private HotRankService hotRankService;

    @RequestMapping(value = "refresh/stat", method = RequestMethod.GET)
    public @ResponseBody String refreshStat() {
        statService.statWallpaper();
        statService.statLockscreen();
        return "refresh stat success";
    }

    @RequestMapping(value = "refresh/hot", method = RequestMethod.GET)
    public @ResponseBody String refreshHot() {
        hotRankService.hot2DB();
        hotRankService.hot2redis();
        return "refresh hot success";
    }
}
