/*
 * Copyright (c) 2010-2013 www.pixshow.net All Rights Reserved
 *
 * File:Rreads.java Project: WeatherService
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:Dec 27, 2013 3:07:43 PM
 * 
 */
package com.toolbox.weather.data;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.toolbox.weather.bean.CityWeatherBean;
import com.toolbox.weather.dao.CityWeatherDao;

/**
 * 
 * 
 * 
 * @author <a href="mailto:jifangliang@163.com">Time</a>
 * @author $Author:$
 * @version $Revision:$ $Date:$ 
 * @since Dec 27, 2013
 * 
 */

@Component
public class RankingListProcessor {

    @Autowired
    private CityWeatherDao cityWeatherDAO;

    /**
     * 实时温度排序，包括大陆、台湾
     * 大陆排序包括（大陆，港澳台）
     * 台湾排序只包括（台湾全岛）
     */
    public void tempRanking() {
        List<CityWeatherBean> citys = cityWeatherDAO.findAllTemps();

        int rank = 1;
        int rank_tw = 1;
        for (int j = 0; j < citys.size(); j++) {
            CityWeatherBean city = citys.get(j);
            if (city.getCityId() < 12140 || city.getCityId() > 12165) { //大陆
                city.setTempRanking(rank);
                cityWeatherDAO.updateDaLuRanking(city);
                rank++;
            } else { //台湾
                city.setTempRankingTaiwan(rank_tw);
                cityWeatherDAO.updateTaiwanRanking(city);
                rank_tw++;
            }
        }
    }

    public void pmRanking() {
        List<CityWeatherBean> zhPms = cityWeatherDAO.finds("where cityId!=weatherCityId and pm25>=0 order by pm25");
        for (int i = 1; i <= zhPms.size(); i++) {
            CityWeatherBean zh = zhPms.get(i - 1);
            CityWeatherBean before = i != 1 ? zhPms.get(i - 2) : null;
            if (before == null) {
                zh.setPmRanking(i);
            } else if (before.getPm25() == zh.getPm25()) {
                zh.setPmRanking(before.getPmRanking());
            } else {
                zh.setPmRanking(before.getPmRanking() + 1);
            }
            cityWeatherDAO.updateCityWeather(zh);
        }
        List<CityWeatherBean> twPms = cityWeatherDAO.finds("where cityId=weatherCityId and pm25>=0 order by pm25");
        for (int i = 1; i <= twPms.size(); i++) {
            CityWeatherBean tw = twPms.get(i - 1);
            CityWeatherBean before = i != 1 ? twPms.get(i - 2) : null;
            if (before == null) {
                tw.setPmRanking(i);
            } else if (before.getPm25() == tw.getPm25()) {
                tw.setPmRanking(before.getPmRanking());
            } else {
                tw.setPmRanking(before.getPmRanking() + 1);
            }
            cityWeatherDAO.updateCityWeather(tw);
        }

    }
}
