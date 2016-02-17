package com.toolbox.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
* @author E-mail:86yc@sina.com
* 
*/
@Controller
@RequestMapping("config/")
public class ConfigBannerController {

    @RequestMapping(value = "banner", method = RequestMethod.GET)
    public ModelAndView banner() {
        
        
        return new ModelAndView("config/banner");
    }

}
