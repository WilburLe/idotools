package com.toolbox.dao;

import org.springframework.stereotype.Repository;

import com.toolbox.entity.SpeedreportEntity;
import com.toolbox.framework.spring.support.BaseDao;

/**
* @author E-mail:86yc@sina.com
* 
*/
@Repository
public class SpeedreportDao extends BaseDao {

    public void save(SpeedreportEntity speedreport) {
        insertBean("speedreport", speedreport);
    }
}
