package com.toolbox.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.toolbox.entity.RadacctEntity;
import com.toolbox.framework.spring.support.BaseDao;

/**
* @author E-mail:86yc@sina.com
* 
*/
@Repository
public class RadacctDao extends BaseDao {

    public RadacctEntity findByUsername(String username) {
        return queryForBean("select * from radacct where username=? order by radacctid desc limit 1", RadacctEntity.class, username);
    }

    public List<Map<String, Object>> findUserFreeAcc(String username, Date start) {
        return getJdbcTemplate().queryForList("select acctinputoctets, acctoutputoctets from radacct where username=?  and acctstarttime>=? order by radacctid desc", username, start);
    }
    
    public void deleteUserFreeAcc(String username, Date start) {
        update("delete from radacct where username=?  and acctstarttime>=?", username, start);
    }
    
    public void save(RadacctEntity radacct) {
        insertBean("radacct", radacct);
    }

}
