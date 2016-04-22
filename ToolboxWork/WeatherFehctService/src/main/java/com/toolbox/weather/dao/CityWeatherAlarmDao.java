package com.toolbox.weather.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.toolbox.framework.spring.support.BaseDao;
import com.toolbox.weather.bean.CityWeatherAlarmBean;

@Repository
public class CityWeatherAlarmDao extends BaseDao {

    public void save(CityWeatherAlarmBean bean) {
        insertBean("city_weather_alarm", bean);
    }

    public CityWeatherAlarmBean find(String where, Object... params) {
        return queryForBean("select * from city_weather_alarm " + where, CityWeatherAlarmBean.class, params);
    }

    public List<CityWeatherAlarmBean> finds(String where, Object... params) {
        return queryForList("select * from city_weather_alarm " + where, CityWeatherAlarmBean.class, params);
    }
}
