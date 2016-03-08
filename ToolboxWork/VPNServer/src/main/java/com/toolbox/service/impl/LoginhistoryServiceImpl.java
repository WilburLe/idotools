package com.toolbox.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.toolbox.dao.LoginhistoryDao;
import com.toolbox.entity.LoginhistoryEntity;
import com.toolbox.service.LoginhistoryService;

/**
* @author E-mail:86yc@sina.com
* 
*/
@Service("LoginhistoryService")
public class LoginhistoryServiceImpl implements LoginhistoryService {
    @Autowired
    private LoginhistoryDao loginhistoryDao;

    public void save(LoginhistoryEntity loginhistory) {
        loginhistoryDao.save(loginhistory);
    }

}
