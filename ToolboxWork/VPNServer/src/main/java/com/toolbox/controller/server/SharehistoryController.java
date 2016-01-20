package com.toolbox.controller.server;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.toolbox.entity.SharehistoryEntity;
import com.toolbox.framework.utils.DateUtility;
import com.toolbox.service.SharehistoryService;

/**
* @author E-mail:86yc@sina.com
* 
*/
@Controller
public class SharehistoryController {
    @Autowired
    private SharehistoryService sharehistoryService;

    @RequestMapping(value = "share/{username}")
    public @ResponseBody  JSON share(@PathVariable("username") String username) {
        SharehistoryEntity sharehistory = sharehistoryService.findByUsername(username);
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, 1);
        c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), 1, 0, 0);
        Date next_moth_date = DateUtility.parseDate(DateUtility.format(c, "yyyy-MM-dd"), "yyyy-MM-dd");
        JSONObject result = new JSONObject();
        //每个月只能分享一次
        if (sharehistory == null || sharehistory.getSharedate().after(next_moth_date)) {
            sharehistory = new SharehistoryEntity();
            sharehistory.setUsername(username);
            sharehistory.setSharedate(new Date());
            long date = sharehistoryService.save(sharehistory);

            result.put("expresdDate", date);
            result.put("isPro", 1); //高级用户
            result.put("regType", 1); //已注册
        } else {
            
            result.put("error", "Only once a month");
        }
        return result;
    }

    public static void main(String[] args) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, 1);
        c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), 1, 0, 0);
        Date moth_date = DateUtility.parseDate(DateUtility.format(c, "yyyy-MM-dd"), "yyyy-MM-dd");

        System.out.println(DateUtility.format(moth_date));
    }
}
