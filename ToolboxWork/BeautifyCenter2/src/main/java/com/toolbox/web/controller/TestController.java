package com.toolbox.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.toolbox.web.entity.WallpaperEntity;
import com.toolbox.web.service.WallpaperService;

@Controller
@RequestMapping("mgr/")
public class TestController {
    @Autowired
    private WallpaperService wallpaperService;

    @RequestMapping(value = "aaa/{tag}/{start}/{size}")
    public @ResponseBody List<WallpaperEntity> aaa(//
            @PathVariable("tag") String tag//
            , @PathVariable("start") int start//
            , @PathVariable("size") int size) {
        return wallpaperService.findByPage(tag, start, size);
    }
}
