package com.toolbox.controller.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.toolbox.framework.utils.StringUtility;
import com.toolbox.service.RedisService;

/**
* @author E-mail:86yc@sina.com
* 
*/
@Controller
public class AdServerController {
    @Autowired
    private RedisService redisService;

    @RequestMapping(value = "/adconfig", method = RequestMethod.GET)
    public @ResponseBody JSON adconfig() {
        JSONObject result = new JSONObject();
        try {
            String data = redisService.get("adconfig");
            if (StringUtility.isNotEmpty(data)) {
                result = JSONObject.parseObject(data);
            }
            return result;
        } catch (Exception e) {
        }
        return null;
    }

}
