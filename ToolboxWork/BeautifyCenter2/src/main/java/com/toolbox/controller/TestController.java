package com.toolbox.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

@Controller
public class TestController {
    
    @RequestMapping(value="aaaa")
    public @ResponseBody JSON json1() {
        JSONObject json = new JSONObject();
        json.put("k1", "v1");
        json.put("k2", "v2");
        
         return json;
    }
}
