package com.toolbox.web.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.toolbox.redis.tools.RedisPoolFactory;

@Controller
@RequestMapping("mgr/")
public class TestController {

    @RequestMapping(value = "aaa")
    public @ResponseBody JSON aaa() {

        List<String> keys = RedisPoolFactory.getInstance().keys(RedisPoolFactory.poolName.abroad.name(), "*", 5);
        for (int i = 0; i < keys.size(); i++) {
            System.out.println(i+" > "+keys.get(i));
        }
        JSONArray arr =JSONArray.parseArray(JSONArray.toJSON(keys).toString());
        return arr;
    }
}
