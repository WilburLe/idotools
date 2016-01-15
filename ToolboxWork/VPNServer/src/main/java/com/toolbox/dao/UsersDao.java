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

    public int save(UsersEntity users) {
        return insertBean("users", users).intValue();
    }

}
