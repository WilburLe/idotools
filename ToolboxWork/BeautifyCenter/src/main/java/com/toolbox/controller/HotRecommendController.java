package com.toolbox.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.toolbox.service.TagEditService;

/**
* @author E-mail:86yc@sina.com
* 
*/
@Controller
public class HotRecommendController {
    @Autowired
    private TagEditService tagEditService;

    @RequestMapping(value = "hot/recommend/{type}", method = RequestMethod.GET)
    public ModelAndView hotrecommend(@PathVariable("type") String type) {
        List<JSONObject> tags = tagEditService.findAllTag();
        
        return new ModelAndView("hot/recommend").addObject("tags", tags);
    }

}
