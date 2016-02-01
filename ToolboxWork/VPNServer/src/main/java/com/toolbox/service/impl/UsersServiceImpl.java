package com.toolbox.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.toolbox.common.RadgroupTypeEnum;
import com.toolbox.dao.RadcheckDao;
import com.toolbox.dao.RadusergroupDao;
import com.toolbox.dao.UsersDao;
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
        radusergroup.setPriority(1);
        radusergroupDao.save(radusergroup);
        //用户注册
        return usersDao.save(users);
    }

}
