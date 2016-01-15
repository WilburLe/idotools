package com.toolbox.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.toolbox.dao.UsersDao;
import com.toolbox.entity.UsersEntity;
import com.toolbox.service.UsersService;

/**
* @author E-mail:86yc@sina.com
* 
*/
@Service("UsersService")
public class UsersServiceImpl implements UsersService {
    @Autowired
    private UsersDao usersDao;

    public List<UsersEntity> findsByPage(int start, int limit) {
        return usersDao.findsByPage(start, limit);
    }

    public int save(UsersEntity users) {
        return usersDao.save(users);
    }
}
