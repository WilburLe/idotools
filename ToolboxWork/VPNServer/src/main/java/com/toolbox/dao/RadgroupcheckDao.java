package com.toolbox.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.toolbox.entity.RadgroupcheckEntity;
import com.toolbox.framework.spring.support.BaseDao;
import com.toolbox.framework.utils.SqlUtility;

/**
* @author E-mail:86yc@sina.com
* 
*/
@Repository
public class RadgroupcheckDao extends BaseDao {
    public List<RadgroupcheckEntity> findsByGroupnames(List<String> groupnames) {
        return queryForList("select * from radgroupcheck where " + SqlUtility.in("groupname", groupnames.size()), RadgroupcheckEntity.class, groupnames.toArray());
    }
}
