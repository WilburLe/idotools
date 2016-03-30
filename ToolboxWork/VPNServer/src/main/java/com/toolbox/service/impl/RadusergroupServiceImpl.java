package com.toolbox.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.toolbox.dao.RadusergroupDao;
import com.toolbox.entity.RadusergroupEntity;
import com.toolbox.service.RadusergroupService;

/**
* @author E-mail:86yc@sina.com
* 
*/
@Service("RadusergroupService")
public class RadusergroupServiceImpl implements RadusergroupService {
    @Autowired
    private RadusergroupDao radusergroupDao;

    public List<RadusergroupEntity> findsByUsernames(List<String> usernames) {
        return radusergroupDao.findsByUsernames(usernames);
    }

    public RadusergroupEntity findByUsername(String username) {
        return radusergroupDao.findByUsername(username);
    }
    public void update(RadusergroupEntity radusergroup) {
        radusergroupDao.update(radusergroup);
    }

}
