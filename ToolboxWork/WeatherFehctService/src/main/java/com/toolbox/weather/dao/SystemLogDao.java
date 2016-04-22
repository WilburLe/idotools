package com.toolbox.weather.dao;

import java.util.Date;

import org.springframework.stereotype.Repository;

import com.toolbox.framework.spring.support.BaseDao;

@Repository
public class SystemLogDao extends BaseDao {

    public void log(String name, String message) {
        getJdbcTemplate().update("insert into system_log(name, message, createDate) values(?,?,?)", name, message, new Date());
    }
}
