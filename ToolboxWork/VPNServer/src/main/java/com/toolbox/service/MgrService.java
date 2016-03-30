package com.toolbox.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.toolbox.dao.MgrDao;

/**
* @author E-mail:86yc@sina.com
* 
*/
@Service("MgrService")
public class MgrService {
    @Autowired
    private MgrDao mgrDao;

    public void deleteUser(String username) {
        mgrDao.deleteUser(username);
    }

    public void delCheckin(int id) {
        mgrDao.delCheckin(id);
    }

    public void delCheckin(String username, String datestr) {
        mgrDao.delCheckin(username, datestr);
    }

    public void checkinDays(String username, int days) {
        mgrDao.checkinDays(username, days);
    }

}
