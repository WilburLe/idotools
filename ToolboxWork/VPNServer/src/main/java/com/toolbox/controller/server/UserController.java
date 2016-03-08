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
import com.toolbox.entity.UsersEntity;
import com.toolbox.framework.utils.DateUtility;
import com.toolbox.framework.utils.StringUtility;
import com.toolbox.service.ExpirationService;
import com.toolbox.service.LoginhistoryService;
import com.toolbox.service.RadacctService;
import com.toolbox.service.RadusergroupService;
import com.toolbox.service.UsersService;

/**
* @author E-mail:86yc@sina.com
* 
*/
@Controller
public class UserController {
    @Autowired
    private UsersService        usersService;
    @Autowired
    private ExpirationService   expirationService;
    @Autowired
    private RadusergroupService radusergroupService;
    @Autowired
    private RadacctService      radacctService;
    @Autowired
    private LoginhistoryService loginhistoryService;

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
        return regist(username, bindid, deviceid, appid, version, UserEnum.named.name());
    }

    @RequestMapping(value = "regist", method = RequestMethod.POST)
    public @ResponseBody JSON registPost(String username, String bindid, String deviceid, String appid, String version, String usertype) {
        return regist(username, bindid, deviceid, appid, version, usertype);
    }

    private JSON regist(String username, String bindid, String deviceid, String appid, String version, String usertype) {
        UsersEntity users = usersService.findByUsername(username);
        JSON result = null;
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
            result = login(users);
        }
        //login history
        LoginhistoryEntity loginhistory = new LoginhistoryEntity();
        loginhistory.setLogindate(new Date());
        loginhistory.setUsername(username);
        loginhistory.setUsertype(usertype);
        loginhistoryService.save(loginhistory);
        return result;
    }

    private JSON reg(String username, String bindid, String deviceid, String appid, String version, String usertype) {
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
            result.put("dataRemain", UserEnum.anonymous.getDataRemain()); //剩余流量
        } else {
            result.put("dataRemain", UserEnum.named.getDataRemain()); //剩余流量
        }
        return result;
    }

    private JSON login(UsersEntity users) {
        usersService.update(users);

        JSONObject result = new JSONObject();
        Date date = new Date();
        if (UserEnum.anonymous.name().equals(users.getUsertype())) { //匿名用户 相当于普通用 
            result.put("isPro", 0); //普通用户
            //计算剩余流量
            Date monthStart = DateUtility.parseDate(DateUtility.format(new Date()), "yyyy-MM");
            long useaccts = radacctService.findUserFreeAcc(users.getUsername(), monthStart);
            result.put("dataRemain", UserEnum.anonymous.getDataRemain() - (useaccts / 1024));
        } else { //实名用户 
            ExpirationEntity expiration = expirationService.findByUsername(users.getUsername());
            if (expiration == null || expiration.getExpireddate().before(date)) {
                result.put("isPro", 0); //普通用户
                //计算剩余流量
                Date monthStart = DateUtility.parseDate(DateUtility.format(new Date()), "yyyy-MM");
                long useaccts = radacctService.findUserFreeAcc(users.getUsername(), monthStart);
                result.put("dataRemain", UserEnum.named.getDataRemain() - (useaccts / 1024));

                //过期需要将更新用户组表
                if (expiration != null) {
                    radusergroupService.updateSubscribetype(users.getUsername(), RadgroupTypeEnum.FREE.getName());
                }
            } else {
                result.put("expiredDate", expiration.getExpireddate().getTime());
                result.put("isPro", 1); //高级用户
            }
        }
        result.put("regType", 1); //已注册
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
