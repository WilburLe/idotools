package com.toolbox.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.toolbox.common.RadgroupTypeEnum;
import com.toolbox.dao.ExpirationDao;
import com.toolbox.dao.RadcheckDao;
import com.toolbox.dao.RadusergroupDao;
import com.toolbox.dao.UsersDao;
import com.toolbox.entity.ExpirationEntity;
import com.toolbox.entity.RadcheckEntity;
import com.toolbox.entity.RadusergroupEntity;
import com.toolbox.entity.UsersEntity;
import com.toolbox.service.UsersService;

/**
* @author E-mail:86yc@sina.com
* 
*/
@Service("UsersService")
public class UsersServiceImpl implements UsersService {
    @Autowired
    private UsersDao        usersDao;
    @Autowired
    private RadcheckDao     radcheckDao;
    @Autowired
    private RadusergroupDao radusergroupDao;
    @Autowired
    private ExpirationDao   expirationDao;

    public List<UsersEntity> findsByPage(int start, int limit) {
        return usersDao.findsByPage(start, limit);
    }

    public UsersEntity findByUsername(String username) {
        return usersDao.findByUsername(username);
    }

    public void update(UsersEntity users) {
        usersDao.update(users);
    }

    public int regist(UsersEntity users) {
        //用户密码
        RadcheckEntity radcheck = new RadcheckEntity();
        radcheck.setUsername(users.getUsername());
        radcheck.setValue(users.getPassword());
        radcheck.setOp(":=");
        radcheck.setAttribute("SSHA2-256-Password");
        radcheckDao.save(radcheck);
        //用户类型
        RadusergroupEntity radusergroup = new RadusergroupEntity();
        radusergroup.setUsername(users.getUsername());
        radusergroup.setGroupname(RadgroupTypeEnum.FREE.getName());
        radusergroupDao.save(radusergroup);
        //        //免费订阅
        //        Date date = new Date();
        //        ExpirationEntity expiration = new ExpirationEntity();
        //        expiration.setUsername(users.getUsername());
        //        expiration.setSubscribetype(RadgroupTypeEnum.FREE.getName());
        //        expiration.setSubscribedate(date);
        //        expiration.setExpireddate(date);
        //        expirationDao.save(expiration);
        //用户注册
        return usersDao.save(users);
    }

}
