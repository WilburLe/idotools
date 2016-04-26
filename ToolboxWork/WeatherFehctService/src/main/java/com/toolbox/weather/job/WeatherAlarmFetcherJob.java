/*
 * Copyright (c) 2010-2013 www.pixshow.net All Rights Reserved
 *
 * File:WeatherAlarmFetcher.java Project: WeatherService
 * 
 * Creator:4399-lvtu-8 
 * Date:Dec 27, 2013 4:55:24 PM
 * 
 */
package com.toolbox.weather.job;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toolbox.framework.utils.DateUtility;
import com.toolbox.framework.utils.HttpUtility;
import com.toolbox.framework.utils.RegexUtility;
import com.toolbox.weather.bean.CityWeatherAlarmBean;
import com.toolbox.weather.bean.CityWeatherBean;
import com.toolbox.weather.service.WeatherService;

/**
 * 
 * 
 * 
 * @author 4399-lvtu-8
 * @author $Author:$
 * @version $Revision:$ $Date:$ 
 * @since Dec 27, 2013
 * 
 */
@Component
public class WeatherAlarmFetcherJob implements Job {
    private Log            log = LogFactory.getLog(WeatherAlarmFetcherJob.class);
    @Autowired
    private WeatherService weatherService;

    private void fetch() {
        String data = HttpUtility.get("http://www.weather.com.cn/alarm/warninglist.shtml");

        Document doc = Jsoup.parse(data);
        Element script = doc.select("script").get(1);
        String URL = RegexUtility.findGroup(script.html(), "URL='(.+?)'", 1);
        JSONArray yjlb = JSONArray.parseArray(RegexUtility.findGroup(script.html(), "yjlb=(.+?);", 1));
        JSONArray gdlb = JSONArray.parseArray(RegexUtility.findGroup(script.html(), "gdlb=(.+?);", 1));
        JSONArray yjyc = JSONArray.parseArray(RegexUtility.findGroup(script.html(), "yjyc=(.+?);", 1));
        JSONArray gdyc = JSONArray.parseArray(RegexUtility.findGroup(script.html(), "gdyc=(.+?);", 1));

        String alarmUrl = URL.split("=")[0] + "=" + HttpUtility.urlEncode(URL.split("\\=")[1], "UTF-8");
        String alarmData = HttpUtility.get(alarmUrl);
        JSONObject json = JSONObject.parseObject(alarmData.substring(alarmData.indexOf("{"), alarmData.lastIndexOf(";")));
        JSONArray arr = json.getJSONArray("data");

        for (int i = 0; i < arr.size(); i++) {
            JSONArray item = arr.getJSONArray(i);
            String cityName = item.getString(0);
            //10123-20131227105025-1102.html
            String code = item.getString(1);
            String cityCode = RegexUtility.findGroup(code, "(\\w+)-", 1);
            String date = RegexUtility.findGroup(code, "-(\\w+)-", 1);
            String alarmCode = RegexUtility.findGroup(code, "-\\w+-(.+?)\\.", 1);

            String alarmType = "";
            int validateType = Integer.parseInt(alarmCode.substring(0, 1));
            if (validateType > 1) {
                alarmType = gdlb.getString(Integer.parseInt(alarmCode.substring(1, 2)) - 1);
            } else {
                alarmType = yjlb.getString(Integer.parseInt(alarmCode.substring(0, 2)) - 1);
            }

            String alarmYs = "";
            int validateYs = Integer.parseInt(alarmCode.substring(2, 3));
            if (validateYs > 0) {
                alarmYs = gdyc.getString(Integer.parseInt(alarmCode.substring(3)) - 1);
            } else {
                alarmYs = yjyc.getString(Integer.parseInt(alarmCode.substring(2)) - 1);
            }
            cityCode = cityCode + "01";
            if (cityCode.length() == 7) {
                cityCode = cityCode + "00";
            }

            CityWeatherBean cityWeather = weatherService.findCityWeatherByWeatherCityId(cityCode);
            Date publicDate = DateUtility.parseDate(date, "yyyyMMddHHmmss");
            CityWeatherAlarmBean alarm = weatherService.findAlarmByCityIdAndPublicDate(cityCode, publicDate);
            if (alarm == null) {
                alarm = new CityWeatherAlarmBean();
                alarm.setAlarmType(alarmType);
                alarm.setAlarmYs(alarmYs);
                if (cityWeather != null) {
                    alarm.setCityId(cityWeather.getCityId());
                }
                alarm.setCreateDate(new Date());
                alarm.setPublicDate(publicDate);
                alarm.setWeatherCityId(cityCode);
                alarm.setContent(cityName + "气象台发布" + alarmType + alarmYs + "预警");
                weatherService.saveAlarm(alarm);
            }
            log.info(cityCode + "> " + alarm.getContent());
        }
    }

    @Override
    public void execute(JobExecutionContext arg0) throws JobExecutionException {
        fetch();
    }

}
