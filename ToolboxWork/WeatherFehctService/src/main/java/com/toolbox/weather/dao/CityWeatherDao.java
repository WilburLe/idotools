package com.toolbox.weather.dao;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.toolbox.framework.spring.support.BaseDao;
import com.toolbox.framework.utils.DateUtility;
import com.toolbox.weather.bean.CityBean;
import com.toolbox.weather.bean.CityWeatherBean;
import com.toolbox.weather.bean.CityWeatherRankBean;
import com.toolbox.weather.tools.DateTool;

@Repository
public class CityWeatherDao extends BaseDao {

    public int addCityWeather(CityWeatherBean bean) {
        return insertBean("city_weather", bean, "id").intValue();
    }

    public int updateCityWeather(CityWeatherBean bean) {
        return updateBean("city_weather", bean, "id=" + bean.getId()).intValue();
    }

    public void addFetchCount(long id) {
        update("update city_weather set fetchCount=fetchCount+1 where id=?", id);
    }

    public CityWeatherBean findCityWeather(int cityId) {
        return queryForBean("select * from city_weather where cityId =?", CityWeatherBean.class, cityId);
    }

    public CityWeatherBean findSimpleCityWeather(int cityId) {
        return queryForBean("select id,cityId,weatherCityId,qqCityId,curCode,curCodeInfo,curTemp,low,high,code,codeInfo,nightCode," + //
                "nightCodeInfo,unixTime,windLevel,windLevelInfo,windDirection,sunriseTime,sunsetTime,tempRanking,tempRankingTaiwan,pm25," + //
                "pmRanking,pmRankingTaiwan,pmSource,humidity,visibility,source,createDate" + //
                " from city_weather where cityId =?", CityWeatherBean.class, cityId);
    }

    public CityWeatherBean findCityWeatherByCode(String weatherCityId) {
        return queryForBean("select * from city_weather where weatherCityId = ?", CityWeatherBean.class, weatherCityId);
    }

    public void updateDaLuRanking(CityWeatherBean bean) {
        getJdbcTemplate().update("update city_weather set  tempRanking = ?  where cityId = ? ", bean.getTempRanking(), bean.getCityId());
    }

    public void updateTaiwanRanking(CityWeatherBean bean) {
        getJdbcTemplate().update("update city_weather set  tempRankingTaiwan = ?  where cityId = ? ", bean.getTempRankingTaiwan(), bean.getCityId());
    }

    public CityWeatherBean find(String where, Object... params) {
        return queryForBean("select * from city_weather " + where, CityWeatherBean.class, params);
    }

    public List<CityWeatherBean> finds(String where, Object... params) {
        return queryForList("select * from city_weather " + where, CityWeatherBean.class, params);
    }

    public List<CityWeatherBean> findsSimple(String where, Object... params) {
        return queryForList("select id,cityId,weatherCityId,qqCityId,curCode,curCodeInfo,curTemp,low,high,code,codeInfo,nightCode," + //
                "nightCodeInfo,unixTime,windLevel,windLevelInfo,windDirection,sunriseTime,sunsetTime,tempRanking,tempRankingTaiwan,pm25," + //
                "pmRanking,pmRankingTaiwan,pmSource,humidity,visibility,source,createDate from city_weather " + where, CityWeatherBean.class, params);
    }

    public List<CityWeatherBean> findAllTemps() {
        return queryForList("select w.id,w.cityId,w.curTemp from city_weather w where w.curTemp is not null order by w.curTemp asc ", CityWeatherBean.class);
    }

    public List<CityWeatherBean> findTaiwanTemps() {
        return queryForList("select w.id,w.cityId,w.curTemp from city_weather w where w.cityId >=12140 && w.cityId <=12165 and w.curTemp is not null order by w.curTemp asc ", CityWeatherBean.class);
    }

    public List<CityBean> findAllChinaCity() {
        return queryForList("SELECT * FROM city WHERE treePath LIKE ?", CityBean.class, "[1]%");
    }

    public List<CityWeatherRankBean> tempAllRanking(boolean isTw, String seq, int limitNum) {
        if (!isTw) {
            return queryForList("SELECT w.cityId, c.name AS cityName, w.curTemp, w.tempRanking, w.tempRankingTaiwan FROM city_weather w " + // 
                    " JOIN city c ON w.cityId=c.id" + // 
                    " where (c.name LIKE '%市' OR c.name LIKE '%地区') and w.cityId!=w.weatherCityId order by curTemp " + seq + " limit " + limitNum, CityWeatherRankBean.class);
        } else {
            return queryForList("SELECT w.cityId, c.name AS cityName, w.curTemp, w.tempRanking, w.tempRankingTaiwan FROM city_weather w " + // 
                    " JOIN city c ON w.cityId=c.id" + // 
                    " where w.cityId=w.weatherCityId order by curTemp " + seq + " limit " + limitNum, CityWeatherRankBean.class);
        }
    }

    public List<CityWeatherRankBean> pmAllRanking(boolean isTw, String seq, int limitNum) {
        if (!isTw) {
            return queryForList("SELECT w.cityId, c.name AS cityName, w.pm25, w.pmRanking, w.pmRankingTaiwan FROM city_weather w " + // 
                    " JOIN city c ON w.cityId=c.id " + // 
                    " where (c.name LIKE '%市' OR c.name LIKE '%地区') and w.pm25>0 and w.cityId!=w.weatherCityId order by w.pm25 " + seq + " limit " + limitNum, CityWeatherRankBean.class);
        } else {
            return queryForList("SELECT w.cityId, c.name AS cityName, w.pm25, w.pmRanking, w.pmRankingTaiwan FROM city_weather w " + // 
                    " JOIN city c ON w.cityId=c.id" + // 
                    " where w.pm25>0 and w.cityId=w.weatherCityId order by w.pm25 " + seq + " limit " + limitNum, CityWeatherRankBean.class);
        }
    }

    ///////////////////////////////////
    public void backupCityWeatherData() {
        Calendar date = Calendar.getInstance();
        String yyyy_MM = DateUtility.format(date, "yyyy_MM");
        String tableName = "city_weather_histroy_" + yyyy_MM;
        int bacakupDate = DateTool.getHalfHourDate();
        List<Map<String, Object>> show = getJdbcTemplate().queryForList("SHOW TABLES LIKE '" + tableName + "'");
        if (show.isEmpty()) {
            update("create table " + tableName + " like city_weather_histroy");
        }
        insert("insert into " + tableName//
                + " select cityId,weatherCityId,qqCityId,curCode,curCodeInfo,low,high,code,codeInfo,nightCode,nightCodeInfo,unixTime,windLevel,"//
                + " windLevelInfo,windDirection,sunriseTime,sunsetTime,curTemp,pm25,pmSource,humidity,visibility,trafficControls,extInfo,source,createDate,"//
                + bacakupDate + " as bacakupDate"//
                + " from city_weather");
    }

    public CityWeatherBean findHistoryCityWeather(int cityId, int historyDate) {
        int bacakupDate = DateTool.getHalfHourDate(historyDate);
        String yyyy_MM = DateUtility.format(historyDate, "yyyy_MM");
        String tableName = "city_weather_histroy_" + yyyy_MM;
        return queryForBean("select * from " + tableName + " where cityId =? and bacakupDate=?", CityWeatherBean.class, cityId, bacakupDate);
    }
    ///////////////////////////////////

}
