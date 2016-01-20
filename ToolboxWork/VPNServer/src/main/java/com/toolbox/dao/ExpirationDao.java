package com.toolbox.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.toolbox.entity.ExpirationEntity;
import com.toolbox.framework.spring.support.BaseDao;
import com.toolbox.framework.utils.SqlUtility;

/**
* @author E-mail:86yc@sina.com
* 
*/
@Repository
public class ExpirationDao extends BaseDao {
    public void save(ExpirationEntity expiration) {
        insertBean("expiration", expiration);
    }

    public List<ExpirationEntity> findsByUsernames(List<String> usernames) {
        return queryForList("select * from expiration where " + SqlUtility.in("username", usernames.size()), ExpirationEntity.class, usernames.toArray());
    }

    public ExpirationEntity findByUsername(String username) {
        return queryForBean("select * from expiration where username=?", ExpirationEntity.class, username);
    }
    
    public void update(ExpirationEntity expiration) {
        updateBean("expiration", expiration, "username='"+expiration.getUsername()+"'");
    }
}
