package com.toolbox.controller.server;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.toolbox.common.RadgroupTypeEnum;
import com.toolbox.common.SystemErrorEnum;
import com.toolbox.common.UserEnum;
import com.toolbox.entity.ExpirationEntity;
import com.toolbox.entity.LoginhistoryEntity;
import com.toolbox.entity.RadusergroupEntity;
import com.toolbox.entity.UsersEntity;
import com.toolbox.framework.utils.DateUtility;
import com.toolbox.framework.utils.StringUtility;
import com.toolbox.service.ExpirationService;
import com.toolbox.service.LoginhistoryService;
import com.toolbox.service.RadacctService;
import com.toolbox.service.RadusergroupService;
import com.toolbox.service.RedisService;
import com.toolbox.service.ReporthistoryService;
import com.toolbox.service.UsersService;

/**
* @author E-mail:86yc@sina.com
* 
*/
@Controller
public class UserController {
    @Autowired
    private UsersService         usersService;
    @Autowired
    private ExpirationService    expirationService;
    @Autowired
    private RadusergroupService  radusergroupService;
    @Autowired
    private RadacctService       radacctService;
    @Autowired
    private LoginhistoryService  loginhistoryService;
    @Autowired
    private ReporthistoryService reporthistoryService;
    @Autowired
    private RedisService         redisService;

    private final static String pass_append_1 = "f8Udt9diChCe";
    private final static String pass_append_2 = "Jdsd8fdLfh7O";

    @RequestMapping(value = "regist/{username}/{bindid}/{deviceid}/{appid}/{version}", method = RequestMethod.GET)
    public @ResponseBody JSON registGet(//
            @PathVariable(value = "username") String username //
            , @PathVariable(value = "bindid") String bindid // 
            , @PathVariable(value = "deviceid") String deviceid //
            , @PathVariable(value = "appid") String appid//
            , @PathVariable(value = "version") String version//
    ) {
        return regist(username, bindid, deviceid, appid, version, UserEnum.named.name(), "0");
    }

    @RequestMapping(value = "regist", method = RequestMethod.POST)
    public @ResponseBody JSON registPost(String username, String bindid, String deviceid, String appid, String version, String usertype, String offset) {
        return regist(username, bindid, deviceid, appid, version, usertype, offset);
    }

    private JSON regist(String username, String bindid, String deviceid, String appid, String version, String usertype, String offset) {
        UsersEntity users = usersService.findByUsername(username);
        JSONObject result = null;
        if (users == null) {
            result = reg(username, bindid, deviceid, appid, version, usertype);
        } else {
            String pass = getPass(username);
            if (!pass.equals(users.getPassword())) {
                JSONObject error = new JSONObject();
                error.put("status", SystemErrorEnum.pass.getStatus());
                error.put("error", SystemErrorEnum.pass.getError());
                return error;
            }
            if (StringUtility.isNotEmpty(deviceid)) {
                users.setDeviceid(deviceid);
            }
            if (StringUtility.isNotEmpty(appid)) {
                users.setAppid(appid);
            }
            if (StringUtility.isNotEmpty(bindid)) {
                users.setBindid(bindid);
            }
            if (StringUtility.isNotEmpty(version)) {
                users.setVersion(version);
            }
            users.setUsertype(usertype);
            result = login(users, offset);
        }
        //login history
        LoginhistoryEntity loginhistory = new LoginhistoryEntity();
        loginhistory.setLogindate(new Date());
        loginhistory.setUsername(username);
        loginhistory.setUsertype(usertype);
        loginhistoryService.save(loginhistory);

        //广告开关
        String data = redisService.get("adconfig");
        int configToolbox = 0;
        int configDu = 0;
        if (StringUtility.isNotEmpty(data)) {
            try {
                JSONObject adconfig = JSONObject.parseObject(data);
                configToolbox = adconfig.getInteger("configToolbox");
                configDu = adconfig.getInteger("configDu");
            } catch (Exception e) {
            }

        }
        result.put("configToolbox", configToolbox);
        result.put("configDu", configDu);
        return result;
    }

    private JSONObject reg(String username, String bindid, String deviceid, String appid, String version, String usertype) {
        UsersEntity users = new UsersEntity();
        users.setUsername(username);
        users.setPassword(getPass(username));
        users.setDeviceid(deviceid);
        users.setAppid(appid);
        users.setBindid(bindid);
        users.setVersion(version);
        users.setUsertype(usertype);
        users.setSignindate(new Date());
        usersService.regist(users);

        JSONObject result = new JSONObject();
        result.put("regType", 0);//新注册
        result.put("isPro", 0); //普通用户
        //result.put("expiredDate", 0);
        if (UserEnum.anonymous.name().equals(usertype)) {
            result.put("dataMaxLimit", UserEnum.anonymous.getDataRemain());
            result.put("dataRemain", UserEnum.anonymous.getDataRemain()); //剩余流量
        } else {
            result.put("dataMaxLimit", UserEnum.named.getDataRemain());
            result.put("dataRemain", UserEnum.named.getDataRemain()); //剩余流量
        }
        //连续签到次数
        result.put("checkInCount", 0);
        //今天是否签到
        result.put("isCheckedInToday", 0);
        return result;
    }

    private JSONObject login(UsersEntity users, String offset) {
        //更新用户信息
        usersService.update(users);

        JSONObject result = new JSONObject();
        result.put("regType", 1); //已注册用户
        Date date = new Date();
        ExpirationEntity expiration = expirationService.findByUsername(users.getUsername());
        if (expiration != null && expiration.getExpireddate().after(date)) {
            result.put("expiredDate", expiration.getExpireddate().getTime());
            result.put("isPro", 1); //高级用户
        } else {
            result.put("isPro", 0); //普通用户

            //每月固定的总流量
            long dataRemain = 0;
            String groupname = "";
            if (UserEnum.named.name().equals(users.getUsertype())) {
                dataRemain = UserEnum.named.getDataRemain();
                groupname = RadgroupTypeEnum.FREE.getName();
            } else {
                dataRemain = UserEnum.anonymous.getDataRemain();
                groupname = RadgroupTypeEnum.Guest.getName();
            }
            //更新用户组类型
            RadusergroupEntity radusergroup = radusergroupService.findByUsername(users.getUsername());
            if (radusergroup != null && !radusergroup.getGroupname().equals(groupname)) {
                radusergroup.setGroupname(groupname);
                radusergroupService.update(radusergroup);
            }

            //计算剩余流量
            Date monthStart = DateUtility.parseDate(DateUtility.format(date), "yyyy-MM");
            //已使用流量
            long useaccts = radacctService.findUserFreeAcc(users.getUsername(), monthStart);

            //签到赢取的流量
            JSONObject checkInData = reporthistoryService.checkInData(users.getUsername(), monthStart, offset);
            long reportRemail = checkInData.getLongValue("reportRemail");
            long dataMaxLimit = dataRemain + reportRemail;
            result.put("dataMaxLimit", dataMaxLimit);
            //剩余流量=每月固定的总流量+签到赢取的流量-已使用流量
            result.put("dataRemain", dataMaxLimit - (useaccts / 1024));
            result.put("checkInCount", checkInData.getInteger("checkInCount")); //连续签到次数
            result.put("isCheckedInToday", checkInData.getInteger("isCheckedInToday")); //今天是否签到
        }
        return result;
    }

    private String getPass(String username) {
        String pass = DigestUtils.sha256Hex(username + pass_append_1);
        String sha256Hex = DigestUtils.sha256Hex(pass + pass_append_2);
        try {
            String saltHex = Hex.encodeHexString(pass_append_2.getBytes("UTF-8"));
            return sha256Hex + saltHex;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

}
