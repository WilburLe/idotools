package com.toolbox.controller.server;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

@Controller
@RequestMapping("/service")
public class IndexController {

    //////////////////////////////////////////////////////
    
    @RequestMapping(value = "/")
    public ModelAndView index() {
        System.out.println(1234);
        return new ModelAndView("index");
    }

    @RequestMapping(value = "test")
    public ModelAndView test() {
        System.out.println(23467);
        return new ModelAndView("/404");
    }

    @RequestMapping(value = "ccc")
    public @ResponseBody JSON ccc() {
        JSONObject result = new JSONObject();
        result.put("a", "123456");
        return result;
    }

}
