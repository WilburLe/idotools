package com.toolbox.controller.server;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.toolbox.common.SystemErrorEnum;
import com.toolbox.common.UserEnum;
import com.toolbox.entity.ExpirationEntity;
import com.toolbox.entity.ReporthistoryEntity;
import com.toolbox.entity.UsersEntity;
import com.toolbox.framework.utils.DateUtility;
import com.toolbox.service.ExpirationService;
import com.toolbox.service.RadacctService;
import com.toolbox.service.ReporthistoryService;
import com.toolbox.service.UsersService;

/**
* @author E-mail:86yc@sina.com
* 
*/
@Controller
public class CheckinController {
    @Autowired
    private ReporthistoryService reporthistoryService;
    @Autowired
    private ExpirationService    expirationService;
    @Autowired
    private RadacctService       radacctService;
    @Autowired
    private UsersService         usersService;

    @RequestMapping(value = "checkin", method = RequestMethod.POST)
    public @ResponseBody JSON checkin(String username, String offset) {
        JSONObject result = new JSONObject();
        //匿名用户不允许签到
        UsersEntity user = usersService.findByUsername(username);
        if (user == null) {
            result.put("status", SystemErrorEnum.nouser.getStatus());
            result.put("error", SystemErrorEnum.nouser.getError());
            return result;
        }
        //时区
        Calendar c = Calendar.getInstance();
        try {
            int hour = Integer.parseInt(offset) / 1000 / 60 / 60;
            c.add(Calendar.HOUR_OF_DAY, hour);
        } catch (Exception e) {
        }
        Date date = c.getTime();

        //VIP不允许签到
        ExpirationEntity expiration = expirationService.findByUsername(username);
        if (expiration != null && expiration.getExpireddate().after(new Date())) {//this date use system date
            result.put("status", SystemErrorEnum.report_vip.getStatus());
            result.put("error", SystemErrorEnum.report_vip.getError());
            return result;
        }

        ReporthistoryEntity reporthistory = reporthistoryService.findByUsername(username);
        String thisDayDate = DateUtility.format(date, "yyyy-MM-dd");
        String lastDate = reporthistory != null ? DateUtility.format(reporthistory.getReportdate(), "yyyy-MM-dd") : null;
        if (reporthistory == null || !thisDayDate.equals(lastDate)) {
            reporthistory = new ReporthistoryEntity();
            reporthistory.setReportdate(date);
            reporthistory.setUsername(username);
            reporthistory.setBonus("5");
            reporthistoryService.save(reporthistory);
        }
        //计算剩余流量
        Date monthStart = DateUtility.parseDate(DateUtility.format(new Date()), "yyyy-MM");
        //已使用流量
        long useaccts = radacctService.findUserFreeAcc(username, monthStart);
        //每月固定的总流量
        long dataRemain = UserEnum.named.getDataRemain();
        System.out.println("checkin > username=" + username);
        if (UserEnum.anonymous.name().equals(user.getUsertype())) {
            dataRemain = UserEnum.anonymous.getDataRemain();
        }
        //签到赢取的流量
        JSONObject checkInData = reporthistoryService.checkInData(username, monthStart, offset);
        long reportRemail = checkInData.getLongValue("reportRemail");
        //剩余流量=每月固定的总流量+签到赢取的流量-已使用流量
        result.put("dataRemain", (dataRemain + reportRemail) - (useaccts / 1024));
        result.put("checkInCount", checkInData.getInteger("checkInCount")); //连续签到次数
        result.put("isCheckedInToday", 1); //今天已经签到

        result.put("isPro", 0); //普通用户
        result.put("regType", 1); //已注册
        return result;
    }

}
