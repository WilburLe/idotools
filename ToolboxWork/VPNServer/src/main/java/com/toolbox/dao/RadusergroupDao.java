package com.toolbox.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.toolbox.entity.RadusergroupEntity;
import com.toolbox.framework.spring.support.BaseDao;
import com.toolbox.framework.utils.SqlUtility;

/**
* @author E-mail:86yc@sina.com
* 
*/
@Repository
public class RadusergroupDao extends BaseDao {
    public void save(RadusergroupEntity radusergroup) {
        insertBean("radusergroup", radusergroup);
    }

    public List<RadusergroupEntity> findsByUsernames(List<String> usernames) {
        return queryForList("select * from radusergroup where " + SqlUtility.in("username", usernames.size()), RadusergroupEntity.class, usernames.toArray());
    }

    public RadusergroupEntity findByUsername(String username) {
        return queryForBean("select * from radusergroup where username=?", RadusergroupEntity.class, username);
    }

    public void update(RadusergroupEntity radusergroup) {
        updateBean("radusergroup", radusergroup, "username='" + radusergroup.getUsername() + "'");
    }

}
