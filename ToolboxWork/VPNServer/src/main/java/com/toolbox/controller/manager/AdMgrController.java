package com.toolbox.controller.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.toolbox.framework.utils.StringUtility;
import com.toolbox.service.RedisService;

/**
* @author E-mail:86yc@sina.com
* 
*/
@Controller
@RequestMapping("/mgr")
public class AdMgrController {
    @Autowired
    private RedisService redisService;

    @RequestMapping(value = "/adconfig", method = RequestMethod.GET)
    public ModelAndView adconfig() {
        JSONObject result = new JSONObject();
        try {
            String data = redisService.get("adconfig");
            if (StringUtility.isNotEmpty(data)) {
                result = JSONObject.parseObject(data);
            }
        } catch (Exception e) {
        }
        return new ModelAndView("/mgr/adconfig").addObject("result", result);
    }

    @RequestMapping(value = "/saveadconfig", method = RequestMethod.GET)
    public ModelAndView saveadconfig(int configToolbox, int configDu) {
        JSONObject result = new JSONObject();
        try {
            String data = redisService.get("adconfig");
            if (StringUtility.isNotEmpty(data)) {
                result = JSONObject.parseObject(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        result.put("configToolbox", configToolbox);
        result.put("configDu", configDu);
        redisService.set("adconfig", result.toJSONString());
        return new ModelAndView("redirect:/mgr/adconfig");
    }

}
