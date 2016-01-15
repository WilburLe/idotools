package com.toolbox.dao;

import org.springframework.stereotype.Repository;

import com.toolbox.entity.RadcheckEntity;
import com.toolbox.framework.spring.support.BaseDao;

/**
* @author E-mail:86yc@sina.com
* 
*/
@Repository
public class RadcheckDao extends BaseDao {

    public int save(RadcheckEntity radcheck) {
        return insertBean("radcheck", radcheck).intValue();
    }
}
