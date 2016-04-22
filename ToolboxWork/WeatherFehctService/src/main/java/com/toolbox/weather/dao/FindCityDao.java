package com.toolbox.weather.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.toolbox.framework.spring.support.BaseDao;
import com.toolbox.weather.bean.CityAreaGeoBean;
import com.toolbox.weather.bean.CityAreaPointBean;
import com.toolbox.weather.bean.CityBean;

@Repository
public class FindCityDao extends BaseDao {

    public CityBean findCity(int id) {
        return queryForBean("SELECT * FROM city WHERE id = ? ", CityBean.class, id);
    }

    public CityBean findCity(int id, String lang) {
        StringBuilder sb = new StringBuilder();
        sb.append("select name_").append(lang);
        sb.append(" as name,id,parentId,locationLat,locationLng,level,name_py,treePath,createDate,updateDate from city where id = ?");
        return queryForBean(sb.toString(), CityBean.class, id);
    }

    public List<CityBean> findCityChild(int parentId) {
        return queryForList("SELECT * FROM city  WHERE parentId = ? order by name desc", CityBean.class, parentId);
    }

    public List<CityBean> findCityByName(String name) {
        return queryForList("SELECT * FROM city  WHERE name LIKE ?", CityBean.class, "%" + name + "%");
    }

    public List<CityAreaPointBean> findCityAreaPoint(int cityId) {
        return queryForList("select * from city_area  where cityId = ? order by idx", CityAreaPointBean.class, cityId);
    }

    public List<CityAreaGeoBean> findCityAreaGeo(double lng, double lat) {
        return queryForList("select * from city_area_geo where lng = ? and lat = ?", CityAreaGeoBean.class, lng, lat);
    }

}
