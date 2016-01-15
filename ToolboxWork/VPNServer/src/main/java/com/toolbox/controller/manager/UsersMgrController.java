package com.toolbox.controller.manager;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toolbox.common.RadgroupTypeEnum;
import com.toolbox.entity.ExpirationEntity;
import com.toolbox.entity.RadacctEntity;
import com.toolbox.entity.UsersEntity;
import com.toolbox.framework.utils.DateUtility;
import com.toolbox.framework.utils.ListUtiltiy;
import com.toolbox.framework.utils.XPathUtility;
import com.toolbox.service.ExpirationService;
import com.toolbox.service.RadacctService;
import com.toolbox.service.UsersService;

/**
* @author E-mail:86yc@sina.com
* 
*/
@Controller
@RequestMapping("/mgr")
public class UsersMgrController {
    @Autowired
    private UsersService      usersService;
    @Autowired
    private RadacctService    radacctService;
    @Autowired
    private ExpirationService expirationService;

    @RequestMapping(value = "/viewusers/{start}/{limit}")
    public ModelAndView viewusers(@PathVariable int start, @PathVariable int limit) {

        List<UsersEntity> users = usersService.findsByPage(start, limit);
        List<String> usernames = XPathUtility.getList(users, "username");

        List<ExpirationEntity> expirations = expirationService.findsByUsernames(usernames);
        Map<String, ExpirationEntity> expirationUserMap = ListUtiltiy.groupToObject(expirations, "username");

        JSONArray result = new JSONArray();
        for (UsersEntity user : users) {
            JSONObject data = new JSONObject();

            String username = user.getUsername();
            RadacctEntity radacct = radacctService.findByUsername(username);

            ExpirationEntity expiration = expirationUserMap.get(username);
            if (RadgroupTypeEnum.FREE.getName().equals(expiration.getSubscribetype())) {
                Date monthStart = DateUtility.parseDate(DateUtility.format(new Date()), "yyyy-MM-dd");
                long freeaccts = radacctService.findUserFreeAcc(username, monthStart);
                data.put("freeaccts", freeaccts);
                data.put("differDays", -1);
            } else {
                int differDays = DateUtility.differDays(expiration.getExpireddate(), expiration.getSubscribedate());
                data.put("differDays", differDays);
            }

            data.put("userid", user.getId());
            data.put("username", username);
            data.put("subscribetype", expiration.getSubscribetype());//等级
            data.put("acctstarttime", radacct.getAcctstarttime()); //建立时间
            data.put("acctstoptime", radacct.getAcctstoptime()); //终止时间，null 在线
            data.put("acctinputoctets", radacct.getAcctinputoctets()); //下行
            data.put("acctoutputoctets", radacct.getAcctoutputoctets());//上行

            result.add(data);
        }
        return new ModelAndView("/mgr/viewusers").addObject("result", result);
    }
}
