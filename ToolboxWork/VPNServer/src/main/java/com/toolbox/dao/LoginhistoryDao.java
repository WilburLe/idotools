package com.toolbox.dao;

import org.springframework.stereotype.Repository;

import com.toolbox.entity.LoginhistoryEntity;
import com.toolbox.framework.spring.support.BaseDao;

/**
* @author E-mail:86yc@sina.com
* 
*/
@Repository
public class LoginhistoryDao extends BaseDao {

    public void save(LoginhistoryEntity loginhistory) {
        insertBean("loginhistory", loginhistory);
    }
}
