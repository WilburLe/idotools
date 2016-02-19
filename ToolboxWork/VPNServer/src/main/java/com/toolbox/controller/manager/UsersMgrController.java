package com.toolbox.controller.manager;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toolbox.common.RadgroupTypeEnum;
import com.toolbox.entity.ExpirationEntity;
import com.toolbox.entity.RadacctEntity;
import com.toolbox.entity.UsersEntity;
import com.toolbox.framework.utils.DateUtility;
import com.toolbox.framework.utils.ListUtiltiy;
import com.toolbox.framework.utils.UUIDUtility;
import com.toolbox.framework.utils.XPathUtility;
import com.toolbox.service.ExpirationService;
import com.toolbox.service.RadacctService;
import com.toolbox.service.SubscriptionService;
import com.toolbox.service.UsersService;

/**
* @author E-mail:86yc@sina.com
* 
*/
@Controller
@RequestMapping("/mgr")
public class UsersMgrController {
    @Autowired
    private UsersService        usersService;
    @Autowired
    private RadacctService      radacctService;
    @Autowired
    private ExpirationService   expirationService;
    @Autowired
    private SubscriptionService subscriptionService;

    @RequestMapping(value = "/alterusers1", method = RequestMethod.POST)
    public @ResponseBody JSON alterusers1(int userid, String subscribetype) {
        UsersEntity users = usersService.findById(userid);
        if (users == null) {
            return null;
        }
        ExpirationEntity expiration = expirationService.findByUsername(users.getUsername());
        Date date = new Date();
        if (subscribetype.equals(RadgroupTypeEnum.FREE.getName())) {
            if (expiration != null && expiration.getExpireddate().after(date)) {
                expiration.setExpireddate(date);
                expirationService.update(expiration);
            }
        } else {
            RadgroupTypeEnum type = RadgroupTypeEnum.byName(subscribetype);
            if (expiration == null) {
                Calendar c = Calendar.getInstance();
                c.add(Calendar.DAY_OF_MONTH, type.getDays());

                expiration = new ExpirationEntity();
                expiration.setSubscribetype(type.getName());
                expiration.setSubscribedate(date);
                expiration.setUsername(users.getUsername());
                expiration.setExpireddate(c.getTime());
                expirationService.save(expiration);
                
                

                //                subscriptionService.updateSubscribe(users.getUsername(), subscribetype, "test");
            } else {
                Calendar c = Calendar.getInstance();
                c.add(Calendar.DAY_OF_MONTH, type.getDays());
                
                expiration.setSubscribetype(type.getName());
                expiration.setSubscribedate(date);
                expiration.setExpireddate(c.getTime());
                expirationService.update(expiration);
            }
        }

        return null;
    }

    @RequestMapping(value = "/alterusers2", method = RequestMethod.POST)
    public @ResponseBody JSON alterusers2(int userid, String subscribetype, int surplus) {
        UsersEntity users = usersService.findById(userid);
        if (users == null) {
            return null;
        }
        Date date = new Date();
        if (subscribetype.equals(RadgroupTypeEnum.FREE.getName())) {
            Date monthStart = DateUtility.parseDate(DateUtility.format(date), "yyyy-MM-dd");

            //当前剩余流量 M
            int frees = (int) ((1024 * 1024 - (radacctService.findUserFreeAcc(users.getUsername(), monthStart) / 1024)) / 1024);
            if (surplus > frees) {
                radacctService.deleteUserFreeAcc(users.getUsername(), monthStart);
            } else {
                int shengyu = frees - surplus;
                RadacctEntity radacct = new RadacctEntity();
                RadacctEntity radacct_last = radacctService.findByUsername(users.getUsername());
                if (radacct_last != null && radacct_last.getAcctstarttime().after(monthStart)) {
                    Calendar c = Calendar.getInstance();
                    c.setTime(radacct_last.getAcctstarttime());
                    c.add(Calendar.MINUTE, -1);
                    radacct.setAcctstarttime(c.getTime());
                } else {
                    radacct.setAcctstarttime(date);
                }

                radacct.setAcctinputoctets((shengyu / 2) * 1024 * 1024L);
                radacct.setAcctoutputoctets((shengyu / 2) * 1024 * 1024L);
                radacct.setAcctstoptime(date);
                radacct.setAcctupdatetime(date);
                radacct.setUsername(users.getUsername());
                radacct.setCalledstationid("");
                radacct.setCallingstationid("192.168.1.93");
                radacct.setAcctuniqueid(UUIDUtility.uuid32());
                radacct.setAcctterminatecause("");
                radacct.setFramedipaddress("10.83.0.22");
                radacct.setNasipaddress("10.4.1.4");
                radacct.setGroupname(subscribetype);
                radacct.setAcctsessionid(UUIDUtility.uuid32());
                radacctService.save(radacct);
            }
        } else {
            ExpirationEntity expiration = expirationService.findByUsername(users.getUsername());
            if (expiration != null) {
                Calendar c = Calendar.getInstance();
                c.add(Calendar.DAY_OF_MONTH, surplus);
                expiration.setExpireddate(c.getTime());
                expirationService.update(expiration);
            }
        }

        return null;
    }

    @RequestMapping(value = "/viewusers/{start}/{limit}", method = RequestMethod.GET)
    public ModelAndView viewusers(@PathVariable int start, @PathVariable int limit) {
        List<UsersEntity> users = usersService.findsByPage(start, limit);
        List<String> usernames = XPathUtility.getList(users, "username");

        List<ExpirationEntity> expirations = expirationService.findsByUsernames(usernames);
        Map<String, ExpirationEntity> expirationUserMap = ListUtiltiy.groupToObject(expirations, "username");
        Date date = new Date();
        JSONArray result = new JSONArray();
        for (UsersEntity user : users) {
            JSONObject data = new JSONObject();
            String username = user.getUsername();
            data.put("userid", user.getId());
            data.put("username", username);
            data.put("bindid", user.getBindid());

            ExpirationEntity expiration = expirationUserMap.get(username);
            /*
             * 时效表为空 和 时效过期 的都是FREE用户
             */
            if (expiration == null || expiration.getExpireddate().before(date)) {
                /*
                 * 免费用户一个月有1G的免费流量
                 * 计算当月已使用的流量，数据库中存的是byte
                 * 展示要换算为kb，切是剩余的流量
                 */
                Date monthStart = DateUtility.parseDate(DateUtility.format(date), "yyyy-MM");
                long freeaccts = 1024 * 1024 - (radacctService.findUserFreeAcc(username, monthStart) / 1024);
                data.put("freeaccts", freeaccts);
                data.put("differDays", -1);
                data.put("subscribetype", RadgroupTypeEnum.FREE.getName());//等级
            } else {
                //计算剩余时间
                int differDays = DateUtility.differDays(date, expiration.getExpireddate());
                data.put("differDays", differDays);
                data.put("subscribetype", expiration.getSubscribetype());//等级
            }
            //用户最后一次的连接
            RadacctEntity radacct = radacctService.findByUsername(username);
            if (radacct != null) {
                data.put("acctstarttime", radacct.getAcctstarttime()); //建立时间
                data.put("acctstoptime", radacct.getAcctstoptime()); //终止时间，null 在线
                data.put("acctinputoctets", radacct.getAcctinputoctets()); //下行
                data.put("acctoutputoctets", radacct.getAcctoutputoctets());//上行    
            }

            result.add(data);
        }
        return new ModelAndView("/mgr/viewusers").addObject("result", result);
    }

}
