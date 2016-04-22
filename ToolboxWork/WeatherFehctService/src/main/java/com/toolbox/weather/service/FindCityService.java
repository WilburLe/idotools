package com.toolbox.weather.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.toolbox.framework.utils.LatLngUtility;
import com.toolbox.framework.utils.LatLngUtility.LatLng;
import com.toolbox.framework.utils.ListUtiltiy;
import com.toolbox.framework.utils.StringUtility;
import com.toolbox.weather.bean.CityAreaBean;
import com.toolbox.weather.bean.CityAreaGeoBean;
import com.toolbox.weather.bean.CityAreaPointBean;
import com.toolbox.weather.bean.CityBean;
import com.toolbox.weather.dao.FindCityDao;
import com.toolbox.weather.tools.LanguageTool;

@Service
public class FindCityService {

    @Autowired
    private FindCityDao cityDao;

    private static final int scale = 10;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public CityBean findCity(int id) {
        return cityDao.findCity(id);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public CityBean findCity(int id, String lang) {
        return cityDao.findCity(id, LanguageTool.get(lang));
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<CityBean> findCityChild(int parentId) {
        return cityDao.findCityChild(parentId);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<CityAreaBean> findCityArea(int cityId) {
        List<CityAreaPointBean> points = cityDao.findCityAreaPoint(cityId);
        Map<Integer, CityAreaBean> areas = new HashMap<Integer, CityAreaBean>();
        for (CityAreaPointBean point : points) {
            int no = point.getNo();
            CityAreaBean area = areas.get(no);
            if (area == null) {
                area = new CityAreaBean();
                area.setNo(no);
                area.setCityId(cityId);
                areas.put(no, area);

            }
            area.addPoint(point);
        }
        return ListUtiltiy.toList(areas);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public CityAreaBean findCityArea(int cityId, int areaNo) {
        List<CityAreaBean> areas = findCityArea(cityId);
        for (CityAreaBean area : areas) {
            if (area.getNo() == areaNo) {
                return area;
            }
        }
        return null;
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public CityBean findCity(double lat, double lng, String lang) {
        int slng = (int) (lng * scale);
        int slat = (int) (lat * scale);

        slng = slng < 0 ? slng - 1 : slng;
        slat = slat < 0 ? slat - 1 : slat;

        List<CityAreaGeoBean> list = cityDao.findCityAreaGeo((double) slng / scale, (double) slat / scale);
        List<CityAreaBean> areaList = new ArrayList<CityAreaBean>();
        // 以最小面积的区域为准
        double minArea = -1;
        CityAreaBean city = null;
        for (CityAreaGeoBean cag : list) {
            CityAreaBean area = findCityArea(cag.getCityId(), cag.getAreaNo());
            if (area == null) {
                continue;
            }
            areaList.add(area);
            List<LatLng> points = toLatLng(area.getPoints());
            if (LatLngUtility.contains(points, new LatLng(lat, lng))) {
                double pArea = LatLngUtility.getPolygonArea(points);
                if (city == null || minArea > pArea) {
                    city = area;
                    minArea = pArea;
                }
            }
        }

        // 边界误差，计算距离最近的区域
        if (city == null && areaList.size() > 0) {
            CityAreaBean nearArea = null;
            double minDistance = Double.MAX_VALUE;
            for (CityAreaBean area : areaList) {
                List<LatLng> polygon = toLatLng(area.getPoints());
                double distance = LatLngUtility.distanceSq(polygon, new LatLng(lat, lng));
                if (nearArea == null || minDistance > distance) {
                    nearArea = area;
                    minDistance = distance;
                }
            }

            /* if (minDistance < 1.6D) {
             	city = nearArea;
             }*/

            if (Math.sqrt(minDistance) < 0.06) {//  小于 6公里 
                city = nearArea;
            }
        }

        if (city != null) {
            if (StringUtility.isEmpty(lang)) {
                return cityDao.findCity(city.getCityId());
            } else {
                return cityDao.findCity(city.getCityId(), LanguageTool.get(lang));
            }
        }
        return null;
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public CityBean findCity(double lat, double lng) {
        return findCity(lat, lng, null);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<CityBean> findCityByName(String name) {
        return cityDao.findCityByName(name);
    }

    private static List<LatLng> toLatLng(List<CityAreaPointBean> points) {
        List<LatLng> lnglatList = new ArrayList<LatLng>();
        for (CityAreaPointBean point : points) {
            lnglatList.add(new LatLng(point.getLat(), point.getLng()));
        }
        return lnglatList;
    }

}
