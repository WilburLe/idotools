package com.toolbox.weather.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.toolbox.framework.spring.support.BaseDao;
import com.toolbox.weather.bean.CityBean;

@Repository
public class PM25Dao extends BaseDao {

    public List<CityBean> findPm25Citys() {
        return queryForList("select * from city where id>= ? and (level like ? or level like ?) ", CityBean.class, 10000, "地级市", "省");
    }

}
