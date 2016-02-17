package com.toolbox.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
* @author E-mail:86yc@sina.com
* 
*/
@Controller
public class BannerController {

    @RequestMapping(value = "banner/add", method = RequestMethod.GET)
    public void add() {

    }

    @RequestMapping(value = "banner/add", method = RequestMethod.POST)
    public void save() {

        
    }
}
