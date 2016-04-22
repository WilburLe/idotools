/*
 * Copyright (c) 2010-2013 www.pixshow.net All Rights Reserved
 *
 * File:FetchWeatherDataService.java Project: LvWeatherService
 * 
 * Creator:4399-lvtu-8 
 * Date:Dec 2, 2013 6:52:18 PM
 * 
 */
package com.toolbox.weather.data;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.toolbox.framework.utils.ConfigUtility;
import com.toolbox.framework.utils.DateUtility;
import com.toolbox.framework.utils.ListUtiltiy;
import com.toolbox.framework.utils.StringUtility;
import com.toolbox.framework.utils.XPathUtility;
import com.toolbox.weather.bean.CityBean;
import com.toolbox.weather.bean.CityWeatherBean;
import com.toolbox.weather.constant.CitySplitId;
import com.toolbox.weather.data.parsor.pm.BaiduPM25Parsor;
import com.toolbox.weather.data.parsor.pm.ChaPM25Parsor;
import com.toolbox.weather.data.parsor.pm.HongkongPM25Parsor;
import com.toolbox.weather.data.parsor.pm.SinaPm25Parsor;
import com.toolbox.weather.data.parsor.pm.SogouPM25Parsor;
import com.toolbox.weather.data.parsor.weather.Parsor360WeatherData;
import com.toolbox.weather.data.parsor.weather.ParsorChinaWeatherData2;
import com.toolbox.weather.data.parsor.weather.ParsorQqWeatherData;
import com.toolbox.weather.data.parsor.weather.TaiWanWeatherParsor;
import com.toolbox.weather.enums.WeatherSourceEnum;
import com.toolbox.weather.service.FindCityService;
import com.toolbox.weather.service.SystemLogService;
import com.toolbox.weather.service.WeatherService;

/**
 * 
 * 
 * 
 * @author 4399-lvtu-8
 * @author $Author:$
 * @version $Revision:$ $Date:$
 * @since Dec 2, 2013
 * 
 */
@Component
public class WeatherDataFetcher {

    private Log              log = LogFactory.getLog(WeatherDataFetcher.class);
    @Autowired
    private WeatherService   weatherService;
    @Autowired
    private FindCityService  cityService;
    @Autowired
    private SystemLogService systemLog;

    public void fetchDataAllCity() {
        //天气、PM获取
        List<CityBean> citys = weatherService.findAllChinaCity();
        List<List<CityBean>> citysList = ListUtiltiy.split(citys, 10);
        for (List<CityBean> list : citysList) {
            List<Integer> cityIds = XPathUtility.getList(list, "id");
            JSONObject json = new JSONObject();
            json.put("cityIds", cityIds);
            try {
                DTaskManager.getInstance().createJob("WeatherService.FetchWeatherData", json.toString());
            } catch (Exception e) {
                log.warn("分布式任务发送失败 城市->" + json, e);
            }
        }
    }

    public void fetchDataByCityId(int cityId) {
        CityBean city = cityService.findCity(cityId);
        CityWeatherBean cityWeather = weatherService.findCityWeather(cityId);
        int nowDate = DateUtility.currentUnixTime();
        //最后一次抓取时间在自定义范围时间内不做抓取  【超过20分钟】
        if (cityWeather == null) {//
            log.info(">>> 跳过抓取，" + cityId + ", " + city.getName() + "[cityWeather is null~]");
            return;
        }
        if (cityWeather.getFetchCount() > 100 || nowDate - cityWeather.getCreateDate() < Integer.parseInt(ConfigUtility.getInstance().getString("weather.fetchtime"))) {//
            log.info(">>> 跳过抓取，" + cityId + ", " + city.getName() + "[抓取次数(" + cityWeather.getFetchCount() + ")大于100,"//
                    + " 距离上次抓取时间(" + (nowDate - cityWeather.getCreateDate()) + ")小于" + ConfigUtility.getInstance().getString("weather.fetchtime") + "秒]");
            return;
        }
        if (city.getId() == CitySplitId.twId || city.getParentId() == CitySplitId.twId) { //台湾
            TaiWanWeatherParsor taiWanWeatherParsor = new TaiWanWeatherParsor(cityWeather);
            taiWanWeatherParsor.parseCityWeather();
            taiWanWeatherParsor.parsePM25Value();

            if (taiWanWeatherParsor.getExceptionList().size() > 0) {
                systemLog.log("台湾天气解析", taiWanWeatherParsor.getExceptionList().toString());
            }
        } else {
            if (cityWeather.getWeatherCityId() > 0) {
                cityWeather.setCurCode(-1);
                //                ParsorChinaWeatherData chinaWeatherData = new ParsorChinaWeatherData(cityWeather, city);
                ParsorChinaWeatherData2 chinaWeatherData = new ParsorChinaWeatherData2(cityWeather);
                chinaWeatherData.parse();
                cityWeather.setSource(WeatherSourceEnum.China.toString());
                if (chinaWeatherData.getExceptionList().size() > 0) {
                    systemLog.log("中国天气解析", chinaWeatherData.getExceptionList().toString());
                    //中国天气网数据获取不到，抓取次数+1
                    weatherService.addFetchCount(cityWeather.getId());
                }
                log.info("开始抓取 " + cityWeather.getId() + ", " + city.getName() + " > " + cityWeather.getWeatherCityId() + " > ChinaWeather **************************************");
            } else if (StringUtility.isNotEmpty(cityWeather.getQqCityId())) {
                new ParsorQqWeatherData(cityWeather).parse();
                cityWeather.setUnixTime(DateUtility.currentUnixTime());
                cityWeather.setSource("QQ");
                log.info("开始抓取 " + cityWeather.getId() + ", " + city.getName() + " > " + cityWeather.getWeatherCityId() + " > QQ **************************************");
            }
            //----------------------------------PM 抓取--------------------------------------------------------
            if (city.getId() == CitySplitId.hkId || city.getParentId() == CitySplitId.hkId) { //香港
                cityWeather.setPm25(HongkongPM25Parsor.parsePM25Value());
                cityWeather.setPmSource(HongkongPM25Parsor.dataUrl);
//            } else if (city.getId() == CitySplitId.amId || city.getParentId() == CitySplitId.amId) { //澳门
//                cityWeather.setPm25(AomenPM25Parsor.parsePM25Value());
//                cityWeather.setPmSource(AomenPM25Parsor.url);
            } else {
                chainaPM25(city, cityWeather);
            }
        }
        
        if (cityWeather.getCurCode() == -1) {
            new Parsor360WeatherData(cityWeather).parse();
        }
        if (cityWeather.getCurCode() != -1) {
            log.info("抓取成功 " + cityWeather.getId() + ", " + city.getName() + ", curCodeInfo=" + cityWeather.getCurCodeInfo() + ", curTemp=" + cityWeather.getCurTemp() + ", pm=" + cityWeather.getPm25());
            cityWeather.setFetchCount(0);
            cityWeather.setCreateDate(DateUtility.currentUnixTime());
            weatherService.updateCityWeather(cityWeather);
        } else {
            log.info("抓取失败 " + cityWeather.getId() + ", " + city.getName() + ", curCode=" + cityWeather.getCurCode() + ", curCodeInfo=" + cityWeather.getCurCodeInfo());
        }
    }

    private void chainaPM25(CityBean city, CityWeatherBean cityWeather) {
        int pm25 = BaiduPM25Parsor.parsePM25Value(city.getName());
        if (pm25 >= 0) {
            cityWeather.setPmSource("baidu");
        }

        //        if (pm25 < 0) {
        //            pm25 = CNPM25Parsor.parsePM25Value(cityWeather.getCityId());
        //            if (pm25 >= 0) {
        //                cityWeather.setPmSource("CNPM25");
        //            }
        //        }

        if (pm25 < 0) {
            pm25 = SogouPM25Parsor.parsePM25Value(city.getName());
            if (pm25 >= 0) {
                cityWeather.setPmSource("SOGOU");
            }
        }

        if (pm25 < 0) {
            pm25 = SinaPm25Parsor.parsePM25Value(cityWeather.getCityId());
            if (pm25 >= 0) {
                cityWeather.setPmSource("SINA");
            }
        }

        if (pm25 < 0) {
            pm25 = ChaPM25Parsor.parsePM25Value(cityWeather.getCityId());
            if (pm25 >= 0) {
                cityWeather.setPmSource("CHAPM25");
            }
        }
        cityWeather.setPm25(pm25);
    }
}
