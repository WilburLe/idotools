package com.toolbox.controller.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.toolbox.common.RadgroupTypeEnum;
import com.toolbox.service.SubscriptionService;

/**
* @author E-mail:86yc@sina.com
* 
*/
@Controller
public class SubscriptionController {
    @Autowired
    private SubscriptionService subscriptionService;

    @RequestMapping(value = "subscribe/{username}/{subscribetype}/{subscribeno}")
    public @ResponseBody JSON subscribe(//
            @PathVariable("username") String username//
            , @PathVariable("subscribetype") String subscribetype//
            , @PathVariable("subscribeno") String subscribeno//
    ) {
        
        JSONObject result = new JSONObject();
        if (RadgroupTypeEnum.byName(subscribetype) == null) {
            result.put("error", "can not find [subscribetype=" + subscribetype + "]");
            return result;
        }
        
        long expiredDate = subscriptionService.updateSubscribe(username, subscribetype, subscribeno);

        result.put("expiredDate", expiredDate);
        result.put("isPro", 1); //高级用户
        result.put("regType", 1); //已注册
        return result;
    }
}
