package com.toolbox.dao;

import org.springframework.stereotype.Repository;

import com.toolbox.entity.SharehistoryEntity;
import com.toolbox.framework.spring.support.BaseDao;

/**
* @author E-mail:86yc@sina.com
* 
*/
@Repository
public class SharehistoryDao extends BaseDao {
    
    public SharehistoryEntity findByUsername(String username) {
        return queryForBean("select * from sharehistory where username=? order by id desc limit 1", SharehistoryEntity.class, username);
    }

    public int save(SharehistoryEntity sharehistory) {
        return insertBean("sharehistory", sharehistory).intValue();
    }

}
