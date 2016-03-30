package com.toolbox.controller.server;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.toolbox.common.SystemErrorEnum;
import com.toolbox.entity.SharehistoryEntity;
import com.toolbox.entity.UsersEntity;
import com.toolbox.framework.utils.DateUtility;
import com.toolbox.service.SharehistoryService;
import com.toolbox.service.UsersService;

/**
* @author E-mail:86yc@sina.com
* 
*/
@Controller
public class SharehistoryController {
    @Autowired
    private SharehistoryService sharehistoryService;
    @Autowired
    private UsersService        usersService;

    @RequestMapping(value = "share/{username}", method = RequestMethod.GET)
    public @ResponseBody JSON shareGet(@PathVariable("username") String username) {
        return share(username);
    }

    @RequestMapping(value = "share", method = RequestMethod.POST)
    public @ResponseBody JSON sharePost(String username) {
        return share(username);
    }

    private JSON share(String username) {
        JSONObject result = new JSONObject();
        UsersEntity user = usersService.findByUsername(username);
        if (user == null) {
            result.put("status", SystemErrorEnum.nouser.getStatus());
            result.put("error", SystemErrorEnum.nouser.getError());
            return result;
        }
        SharehistoryEntity sharehistory = sharehistoryService.findByUsername(username);
        Date date = new Date();
        String thisMonthDate = DateUtility.format(date, "yyyy-MM");
        String lastShareDate = sharehistory!=null?DateUtility.format(sharehistory.getSharedate(), "yyyy-MM"):null;
        
        //每个月只能分享一次
        if (sharehistory == null || !lastShareDate.equals(thisMonthDate)) {
            sharehistory = new SharehistoryEntity();
            sharehistory.setUsername(username);
            sharehistory.setSharedate(date);
            long expiredDate = sharehistoryService.save(sharehistory);

            result.put("expiredDate", expiredDate);
            result.put("isPro", 1); //高级用户
            result.put("regType", 1); //已注册
        } else {
            result.put("status", SystemErrorEnum.share.getStatus());
            result.put("error", SystemErrorEnum.share.getError());
        }
        return result;
    }
}
