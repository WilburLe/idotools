package com.toolbox.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.toolbox.dao.ExpirationDao;
import com.toolbox.entity.ExpirationEntity;
import com.toolbox.service.ExpirationService;

/**
* @author E-mail:86yc@sina.com
* 
*/
@Service("ExpirationService")
public class ExpirationServiceImpl implements ExpirationService {
    @Autowired
    private ExpirationDao expirationDao;

    public List<ExpirationEntity> findsByUsernames(List<String> usernames) {
        return expirationDao.findsByUsernames(usernames);
    }

    public ExpirationEntity findByUsername(String username) {
        return expirationDao.findByUsername(username);
    }

    @Override
    public void update(ExpirationEntity expiration) {
        expirationDao.update(expiration);
    }

    @Override
    public void save(ExpirationEntity expiration) {
        expirationDao.save(expiration);
        
    }

}
