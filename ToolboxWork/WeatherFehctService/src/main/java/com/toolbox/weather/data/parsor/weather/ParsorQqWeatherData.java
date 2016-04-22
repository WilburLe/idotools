/*
 * Copyright (c) 2010-2013 www.pixshow.net All Rights Reserved
 *
 * File:ParsorQQData.java Project: LvWeatherService
 * 
 * Creator:4399-lvtu-8 
 * Date:Dec 3, 2013 6:44:25 PM
 * 
 */
package com.toolbox.weather.data.parsor.weather;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toolbox.framework.utils.DateUtility;
import com.toolbox.framework.utils.StringUtility;
import com.toolbox.weather.bean.CityWeatherBean;
import com.toolbox.weather.enums.QqWithYahooCode;
import com.toolbox.weather.tools.QqCodeTool;
import com.toolbox.weather.tools.WeatherTool;

/**
 * 
 * 
 * 
 * @author 4399-lvtu-8
 * @author $Author:$
 * @version $Revision:$ $Date:$ 
 * @since Dec 3, 2013
 * 
 */
public class ParsorQqWeatherData {
    public static void main(String[] args) {
        //        CityWeatherBean cityWeather = new CityWeatherBean();
        //        //cityWeather.setCityId(11345);
        //        //cityWeather.setQqCityId("0101230604");
        //        cityWeather.setCityId(12166);
        //        cityWeather.setQqCityId("01010101");
        //        new ParsorQqWeatherData(cityWeather).parse();
        System.out.println(DateUtility.format(DateUtility.parseDate("2014-12-23", "yyyy-MM-dd"), "yyyy-M-d"));
    }

    private CityWeatherBean cityWeather;

    public ParsorQqWeatherData(CityWeatherBean cityWeather) {
        this.cityWeather = cityWeather;
    }

    public boolean parse() {
        String data = WeatherTool.getQQFetchData(cityWeather.getQqCityId());
        if (StringUtility.isEmpty(data)) { return false; }
        String regex = "\\{.*\\}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(data);
        if (matcher.find()) {
            JSONObject fetchData = JSONObject.fromObject(matcher.group(0));
            getCur(fetchData);

            cityWeather.setFetchData(fetchData.toString());
            cityWeather.setHourForcast(getHourForcast(fetchData).toString());
            cityWeather.setDayForcast(getDayForcast(fetchData).toString());
            cityWeather.setLiveInfo(getLiveInfo(fetchData).toString());
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * 当前天气状态
     * @param cityWeather
     * @param fetchData
     *
     */
    private void getCur(JSONObject fetchData) {
        QqWithYahooCode yahooCode = QqWithYahooCode.getObject("a" + fetchData.getInt("sk_wt"));
        if (yahooCode != null) {
            cityWeather.setCurCode(yahooCode.getYahooCode());
            cityWeather.setCurCodeInfo(yahooCode.getDescription());
            cityWeather.setCode(yahooCode.getYahooCode());
            cityWeather.setCodeInfo(yahooCode.getDescription());
        }
        try {
            cityWeather.setCurTemp(fetchData.getDouble("sk_tp"));
        } catch (Exception e) {
        }
        try {
            cityWeather.setWindLevel(QqCodeTool.wpl[fetchData.getInt("sk_wp")]);
            cityWeather.setWindLevelInfo(QqCodeTool.wp[fetchData.getInt("sk_wp")]);
        } catch (Exception e) {
        }
        try {
            cityWeather.setHumidity((int) fetchData.getDouble("sk_hd"));
        } catch (Exception e) {
        }
        try {
            cityWeather.setVisibility(fetchData.getInt("sk_vb"));
        } catch (Exception e) {
        }
    }

    /**
     * 天气小时预测
     * @param fetchData
     * @return
     *
     */
    private JSONObject getHourForcast(JSONObject fetchData) {
        JSONObject hourForcast = new JSONObject();
        JSONArray hourForcasts = new JSONArray();
        hourForcast.put("publicDate", fetchData.getJSONObject("h3").getString("rt"));
        JSONArray hours = fetchData.getJSONObject("h3").getJSONArray("0");
        for (int i = 0; i < hours.size(); i++) {
            JSONObject sourHour = hours.getJSONObject(i);
            JSONObject newHour = new JSONObject();
            QqWithYahooCode yahooCode = QqWithYahooCode.getObject("a" + sourHour.getInt("wt"));
            if (yahooCode != null) {
                newHour.put("code", yahooCode.getYahooCode());
                newHour.put("codeInfo", yahooCode.getDescription());
            }
            newHour.put("startDate", sourHour.getString("ts"));
            newHour.put("entDate", sourHour.getString("te"));
            newHour.put("temp", sourHour.getString("tp"));
            newHour.put("windDirection", QqCodeTool.wd[sourHour.getInt("wd")]);
            newHour.put("windLevel", QqCodeTool.wpl[sourHour.getInt("wp")]);
            newHour.put("windLevelInfo", QqCodeTool.wp[sourHour.getInt("wp")]);
            hourForcasts.add(newHour);
        }
        hourForcast.put("hourForcasts", hourForcasts);
        return hourForcast;
    }

    /**
     * 一周内天气预测
     * @param fetchData
     * @return
     *
     */
    private JSONObject getDayForcast(JSONObject fetchData) {

        JSONObject dayForcast = new JSONObject();
        JSONArray dayForcasts = new JSONArray();
        dayForcast.put("publicDate", fetchData.getJSONObject("wk").getString("rt")); //发布时间
        JSONArray hours = fetchData.getJSONObject("wk").getJSONArray("0");
        for (int i = 0; i < hours.size(); i++) {
            JSONObject sourDay = hours.getJSONObject(i);
            JSONObject newDay = new JSONObject();

            QqWithYahooCode yahooCode = QqWithYahooCode.getObject("a" + sourDay.getInt("wt"));
            if (yahooCode != null) {
                newDay.put("code", yahooCode.getYahooCode());
            }
            String startDate = sourDay.getString("ts");
            String today = DateUtility.format(new Date(), "yyyy-M-d");
            String tmin = sourDay.getString("tmin");
            String tmax = sourDay.getString("tmax");
            if (startDate.startsWith(today)) {
                if (!"NULL".equals(tmin)) {
                    cityWeather.setLow(Double.parseDouble(tmin));
                }
                if (!"NULL".equals(tmax)) {
                    cityWeather.setHigh(Double.parseDouble(tmax));
                }
            }

            newDay.put("startDate", startDate);
            newDay.put("entDate", sourDay.getString("te"));
            newDay.put("lowTemp", ("NULL".equals(tmin) ? null : tmin)); //低温
            newDay.put("highTemp", ("NULL".equals(tmax) ? null : tmax)); //高温
            newDay.put("windDirection", QqCodeTool.wd[sourDay.getInt("wd")]); //风向
            newDay.put("windLevel", QqCodeTool.wpl[sourDay.getInt("wp")]); //风力
            newDay.put("windLevelInfo", QqCodeTool.wp[sourDay.getInt("wp")]); //风力
            newDay.put("codeInfo", QqCodeTool.wt[sourDay.getInt("wt")]); //天气状态
            dayForcasts.add(newDay);
        }
        dayForcast.put("dayForcasts", dayForcasts);
        return dayForcast;
    }

    /**
     * 各种指数
     * zs_XXX
     * @param fetchData
     * @return
     *
     */
    private JSONObject getLiveInfo(JSONObject fetchData) {
        JSONObject liveInfo = new JSONObject();
        Iterator<String> keys = fetchData.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            if (key.startsWith("zs_")) {
                Map<String, Object> m = QqCodeTool.zhishu.get(key);
                if (m != null && !m.isEmpty()) {
                    JSONObject liveC = new JSONObject();
                    liveC.put("name", m.get("name"));

                    String[][] all = (String[][]) m.get("content");
                    String[] zs = all[fetchData.getInt(key)];
                    liveC.put("level", zs[0]);
                    liveC.put("dict", zs[1]);
                    liveInfo.put(key, liveC);
                }
            }
        }
        return liveInfo;
    }

}
