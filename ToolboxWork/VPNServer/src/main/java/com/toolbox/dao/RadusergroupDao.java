package com.toolbox.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.toolbox.entity.RadacctEntity;
import com.toolbox.entity.RadusergroupEntity;
import com.toolbox.framework.spring.support.BaseDao;
import com.toolbox.framework.utils.SqlUtility;

/**
* @author E-mail:86yc@sina.com
* 
*/
@Repository
public class RadusergroupDao extends BaseDao {

    public List<RadusergroupEntity> findsByUsernames(List<String> usernames) {
        return queryForList("select * from radusergroup where " + SqlUtility.in("username", usernames.size()), RadusergroupEntity.class, usernames.toArray());
    }
}
