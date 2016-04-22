package com.toolbox.weather.dao;

import org.springframework.stereotype.Repository;

import com.toolbox.framework.spring.support.BaseDao;

@Repository
public class PropertiesDao extends BaseDao {

    public String getValue(String key) {
        return getJdbcTemplate().queryForObject("select value from properties where type = ?", String.class, key);
    }

    public void setValue(String key, String value) {
        int count = getJdbcTemplate().queryForInt("select count(*) counts from properties where type = ?", key);
        if (count == 0) {
            getJdbcTemplate().update("insert into properties(type,value) values(?,?)", key, value);
        } else {
            getJdbcTemplate().update("update properties set value = ? where type =?", value, key);
        }
    }
}
