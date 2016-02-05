package com.toolbox.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
* @author E-mail:86yc@sina.com
* 
*/
@Controller
@RequestMapping("config")
public class ConfigHotController {
    
    @RequestMapping(value = "hot", method = RequestMethod.GET)
    public ModelAndView confighot() {

        return new ModelAndView("config/hot");
    }

}
