package com.toolbox.dao;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.toolbox.entity.ReporthistoryEntity;
import com.toolbox.framework.spring.support.BaseDao;

/**
* @author E-mail:86yc@sina.com
* 
*/
@Repository
public class ReporthistoryDao extends BaseDao {
    public void save(ReporthistoryEntity reporthistory) {
        insertBean("reporthistory", reporthistory);
    }

    public ReporthistoryEntity findByUsername(String username) {
        return queryForBean("select * from reporthistory where username=? order by reportdate desc limit 1", ReporthistoryEntity.class, username);
    }

    public List<ReporthistoryEntity> findsByUsernames(String username, Date date) {
        return queryForList("select * from reporthistory where username=? and reportdate>? order by reportdate desc", ReporthistoryEntity.class, username, date);
    }

}
