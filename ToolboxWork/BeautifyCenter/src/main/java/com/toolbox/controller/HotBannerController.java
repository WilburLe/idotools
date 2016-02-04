package com.toolbox.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
* @author E-mail:86yc@sina.com
* 
*/
@Controller
public class HotBannerController {
    
    @RequestMapping(value = "wallpaper/banner/")
    public ModelAndView banner() {
        
        
        return new ModelAndView();
    }
    
}
