package com.toolbox.controller.server;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.toolbox.entity.ToolboxConfigEntity;
import com.toolbox.service.RedisStatService;
import com.toolbox.service.ToolboxConfigService;

@Controller
public class FinanceController {
    @Autowired
    private ToolboxConfigService toolboxConfigService;
    @Autowired
    private RedisStatService     redisStatService;

    @Value("${service.url}")
    private String               service_url;
    private final static String  code = "finance";

    @RequestMapping(value = "/finance", method = RequestMethod.GET)
    public @ResponseBody JSON financing() {

        ToolboxConfigEntity config = toolboxConfigService.findByCode(code);

        JSONObject result = JSON.parseObject(config.getContent());
        result.put("adurl", service_url + "openad/");
        if (!result.getBooleanValue("open")) {
            result.remove("adurl");
        }
        return result;
    }

    @RequestMapping(value = "/openad/{uid}", method = RequestMethod.GET)
    public void openad(//
            @PathVariable(value = "uid") String uid,//
            HttpServletResponse response//
    ) {
        ToolboxConfigEntity config = toolboxConfigService.findByCode(code);
        if (config == null) {
            return;
        }
        JSONObject result = JSON.parseObject(config.getContent());
        String adurl = result.getString("adurl");

        redisStatService.openstat(code, uid);

        try {
            response.sendRedirect(adurl);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
