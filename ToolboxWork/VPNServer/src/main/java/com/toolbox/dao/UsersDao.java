package com.toolbox.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.toolbox.entity.UsersEntity;
import com.toolbox.framework.spring.support.BaseDao;

/**
* @author E-mail:86yc@sina.com
* 
*/
@Repository
public class UsersDao extends BaseDao {

    public List<UsersEntity> findsByPage(int start, int limit) {
        return queryForList("select * from users limit ?, ?", UsersEntity.class, start, limit);
    }

    public UsersEntity findByUsername(String username) {
        return queryForBean("select * from users where username=?", UsersEntity.class, username);
    }

    public UsersEntity findById(int id) {
        return queryForBean("select * from users where id=?", UsersEntity.class, id);
    }

    public int save(UsersEntity users) {
        return insertBean("users", users).intValue();
    }

    public void update(UsersEntity users) {
        updateBean("users", users, "username='" + users.getUsername() + "'");
    }
}
