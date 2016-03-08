package com.toolbox.controller.server;

import java.util.Calendar;
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
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, 1);
        c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), 1, 0, 0);
        Date next_moth_date = DateUtility.parseDate(DateUtility.format(c, "yyyy-MM-dd"), "yyyy-MM-dd");

        //每个月只能分享一次
        if (sharehistory == null || sharehistory.getSharedate().after(next_moth_date)) {
            sharehistory = new SharehistoryEntity();
            sharehistory.setUsername(username);
            sharehistory.setSharedate(new Date());
            long date = sharehistoryService.save(sharehistory);

            result.put("expiredDate", date);
            result.put("isPro", 1); //高级用户
            result.put("regType", 1); //已注册
        } else {
            result.put("status", SystemErrorEnum.share.getStatus());
            result.put("error", SystemErrorEnum.share.getError());
        }
        return result;
    }

}
