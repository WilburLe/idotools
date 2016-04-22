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

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toolbox.framework.utils.AstronomyUtility;
import com.toolbox.framework.utils.DateUtility;
import com.toolbox.framework.utils.HttpUtility;
import com.toolbox.framework.utils.StringUtility;
import com.toolbox.weather.bean.CityBean;
import com.toolbox.weather.bean.CityWeatherBean;
import com.toolbox.weather.enums.WeatherSourceEnum;
import com.toolbox.weather.enums.WeatherState;
import com.toolbox.weather.tools.WeatherTool;
import com.toolbox.weather.tools.YahooCodeFromChinaTool;

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
public class ParsorChinaWeatherDataJson {
    public static void main(String[] args) {
        CityWeatherBean cityWeather = new CityWeatherBean();
        cityWeather.setCityId(11345);
        cityWeather.setWeatherCityId(101200703);
        CityBean city = new CityBean();
        city.setLocationLat(35.581140);
        city.setLocationLng(116.986534);
        //        cityWeather.setCityId(12166);
        //        cityWeather.setWeatherCityId(101010100);
        new ParsorChinaWeatherDataJson(cityWeather, city).parse();
    }

    private CityWeatherBean cityWeather;
    private boolean         isSunrise = false;
    private CityBean        city;

    public ParsorChinaWeatherDataJson(CityWeatherBean cityWeather, CityBean city) {
        this.cityWeather = cityWeather;
        this.city = city;
        isSunrise = AstronomyUtility.isDay(city.getLocationLat(), city.getLocationLng());
    }

    private static final Pattern pattern    = Pattern.compile("[-+]?\\d+(\\.\\d+)?");
    private static final double  LowestTemp = -273.15;

    public boolean parse() {
        boolean result = false;
        try {
            //当前天气 
            String curWeatherStr = WeatherTool.getCurrentWeather(cityWeather.getWeatherCityId() + "");
            //当天天气详情》最高、最低气温、天气情况 
            String todayWeatherStr = WeatherTool.getTodayWeather(cityWeather.getWeatherCityId() + "");
            //预测天气
            String forcastWeather = WeatherTool.getForecastWeather(cityWeather.getWeatherCityId() + "");
            //一天内小时数据
            String hourWeather = WeatherTool.getHourWeather(cityWeather.getWeatherCityId() + "");
            //当前天气
            getCur(curWeatherStr, todayWeatherStr);

            JSONObject fetchData = new JSONObject();
            fetchData.put("curWeatherStr", curWeatherStr);
            fetchData.put("todayWeatherStr", todayWeatherStr);
            fetchData.put("forcastWeather", forcastWeather);
            fetchData.put("hourWeather", hourWeather);
            cityWeather.setFetchData(fetchData.toString());
            cityWeather.setDayForcast(getDayForcast(forcastWeather));
            cityWeather.setHourForcast(getHourForcast(hourWeather, forcastWeather).toString());
            cityWeather.setLiveInfo(getLiveInfo(forcastWeather).toString());
            cityWeather.setSource(WeatherSourceEnum.China.toString());
            cityWeather.setUnixTime(DateUtility.currentUnixTime());
            cityWeather.setCreateDate(DateUtility.currentUnixTime());
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 当前天气状态
     * @param curWeatherStr
     * @param todayWeatherStr
     *
     */
    public void getCur(String curWeatherStr, String todayWeatherStr) {
        try {
            if (StringUtility.isEmpty(curWeatherStr) || StringUtility.isEmpty(todayWeatherStr)) { return; }
            JSONObject curWeatherInfo = JSONObject.fromObject(curWeatherStr).getJSONObject("weatherinfo");
            JSONObject todayWeatherInfo = JSONObject.fromObject(todayWeatherStr).getJSONObject("weatherinfo");
            //当前气温
            double curTemp = parseTemp(curWeatherInfo.getString("temp"));
            //最低温度
            double low = parseTemp(todayWeatherInfo.getString("temp2"));
            //最高温度
            double high = parseTemp(todayWeatherInfo.getString("temp1"));

            if (LowestTemp == low && LowestTemp == high) { //最高温度，最低温度都没有取到
                return;
            }
            cityWeather.setLow(low);
            cityWeather.setHigh(high);

            cityWeather.setCurTemp(curTemp);
            cityWeather.setCodeInfo(todayWeatherInfo.getString("weather"));
            cityWeather.setCurCodeInfo(todayWeatherInfo.getString("weather"));

            String img1 = todayWeatherInfo.getString("img1").replace(".gif", "");
            String img2 = todayWeatherInfo.getString("img2").replace(".gif", "");
            WeatherState dInfo = WeatherState.getObject(img1.startsWith("d") ? img1 : img2);
            WeatherState nInfo = WeatherState.getObject(img2.startsWith("n") ? img2 : img1);

            cityWeather.setCode(Integer.parseInt(dInfo.getYahooCode()));
            cityWeather.setCodeInfo(dInfo != null ? dInfo.getDescription() : null);
            cityWeather.setNightCode(Integer.parseInt(nInfo.getYahooCode()));
            cityWeather.setNightCodeInfo(nInfo != null ? nInfo.getDescription() : null);

            if (isSunrise) {
                cityWeather.setCurCode(Integer.parseInt(dInfo.getYahooCode()));
                cityWeather.setCurCodeInfo(dInfo != null ? dInfo.getDescription() : null);
            } else {
                cityWeather.setCurCode(Integer.parseInt(nInfo.getYahooCode()));
                cityWeather.setCurCodeInfo(nInfo != null ? nInfo.getDescription() : null);
            }

            //风力等级
            cityWeather.setWindLevel(curWeatherInfo.getInt("WSE"));
            //风力等级
            cityWeather.setWindLevelInfo(curWeatherInfo.getString("WS"));
            //风向信息
            cityWeather.setWindDirection(curWeatherInfo.getString("WD"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 一天内小时数据
     * 温度、风向、风力、降水、湿度
     * @param hourWeather
     * @return
     *
     */
    private JSONObject getHourForcast(String hourWeather, String forcastWeather) {
        if (StringUtility.isEmpty(hourWeather)) { return null; }
        JSONObject json = new JSONObject();
        JSONArray hourForcasts = new JSONArray();
        try {
            XMLSerializer xmlSerializer = new XMLSerializer();
            JSONObject data = (JSONObject) xmlSerializer.read(hourWeather);
            String ptime = data.getString("@ptime"); //发布时间 : 14-02-12 11:00
            JSONArray qw = data.getJSONArray("qw");
            boolean isToday = true;
            for (int i = qw.size() - 1; i >= 0; i--) {
                JSONObject hour = qw.getJSONObject(i);
                JSONObject hourJson = new JSONObject();

                String wd = hour.getString("@wd");
                if (StringUtility.isEmpty(wd)) {
                    continue;
                }
                hourJson.put("temp", wd);//温度
                int fx = hour.optInt("@fx", 0); //风向
                if (fx < 22.5 || fx > 337.5) {
                    hourJson.put("windDirection", "南风");
                } else if (fx > 22.5 && fx < 67.5) {
                    hourJson.put("windDirection", "西南风");
                } else if (fx > 67.5 && fx < 112.5) {
                    hourJson.put("windDirection", "西风");
                } else if (fx > 112.5 && fx < 157.5) {
                    hourJson.put("windDirection", "西北风");
                } else if (fx > 157.5 && fx < 202.5) {
                    hourJson.put("windDirection", "北风");
                } else if (fx > 202.5 && fx < 247.5) {
                    hourJson.put("windDirection", "东北风");
                } else if (fx > 247.5 && fx < 292.5) {
                    hourJson.put("windDirection", "东风");
                } else if (fx > 292.5 && fx < 337.5) {
                    hourJson.put("windDirection", "东南风");
                }
                int fl = hour.getInt("@fl"); //风力
                hourJson.put("windLevel", fl);
                hourJson.put("windLevelInfo", fl + "级");

                double js = hour.optDouble("@js", 0); //降水
                hourJson.put("precipitation", js);
                hourJson.put("humidity", hour.getString("@sd") + "%");//湿度

                int h = hour.getInt("@h"); //时间 : 11
                if (h == 0) {
                    isToday = false;
                }
                Calendar c = Calendar.getInstance();
                c.set(Calendar.SECOND, 0);
                c.set(Calendar.MINUTE, 0);
                String date = null;
                if (isToday) {
                    c.set(Calendar.HOUR_OF_DAY, h);
                    date = DateUtility.format(c, "yyyy-MM-dd HH:mm:ss");
                } else {
                    c.add(Calendar.DAY_OF_MONTH, 1);
                    c.set(Calendar.HOUR_OF_DAY, h);
                    date = DateUtility.format(c, "yyyy-MM-dd HH:mm:ss");
                }
                hourJson.put("date", date);
                boolean sun = AstronomyUtility.isDay(city.getLocationLat(), city.getLocationLng(), DateUtility.parseDate(date, "yyyy-MM-dd HH:mm:ss"));
                JSONObject dayInfo = null;
                if (isToday) {
                    dayInfo = JSONObject.fromObject(cityWeather.getDayForcast()).getJSONArray("dayForcasts").getJSONObject(0);
                } else {
                    dayInfo = JSONObject.fromObject(cityWeather.getDayForcast()).getJSONArray("dayForcasts").getJSONObject(1);
                }
                if (sun) {
                    hourJson.put("code", dayInfo.getString("code"));
                    hourJson.put("codeInfo", dayInfo.getString("codeInfo"));
                } else {
                    hourJson.put("code", dayInfo.optString("codeNight", "-1"));
                    hourJson.put("codeInfo", dayInfo.optString("codeInfoNight", ""));
                }
                hourForcasts.add(hourJson);
            }
            json.put("publicDate", "20" + ptime);
            json.put("hourForcasts", hourForcasts);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }

    /**
     * 六天天气预测
     * @param fetchData
     * @return
     *
     */
    private String getDayForcast(String forcastWeather) {
        String data = HttpUtility.get("http://61.4.185.202/weather/" + cityWeather.getWeatherCityId() + ".shtml?_" + DateUtility.currentUnixTime(), null);
        //        String data = null;
        //        try {
        //            data = FileUtility.readFileToString(new File("C:\\Users\\4399-lvtu-8\\Desktop/test.txt"));
        //        } catch (IOException e) {
        //            e.printStackTrace();
        //        }
        if (data.contains("您要访问的页面不存在")) { return null; }
        Calendar c = Calendar.getInstance();
        JSONObject result = new JSONObject();
        result.put("publicDate", DateUtility.format(c, "yyyy-MM-dd HH:mm:ss"));
        JSONArray dayForcasts = new JSONArray();

        Document doc = Jsoup.parse(data);
        Elements sevendays = doc.select(".weatherYubao .weatherYubaoBox .yuBaoTable");
        for (int m = 0; m < sevendays.size(); m++) {
            if (m > 0) {
                c.add(Calendar.DAY_OF_MONTH, 1);
            }
            String date = DateUtility.format(c, "yyyy-MM-dd");
            Element day = sevendays.get(m);
            Elements trs = day.select("tr");
            Element day1 = trs.get(0);
            Element day2 = null;
            if (trs.size() == 2) {
                day2 = trs.get(1);
            } else {
                continue;
            }
            String dayNight1 = day1.select("td").get(1).text();
            //            String img1 = day1.select("td").get(2).select("img").attr("src");
            String weatherDir1 = day1.select("td").get(3).text();
            int code1 = YahooCodeFromChinaTool.getYahooCode4Title(dayNight1 + weatherDir1);
            String temp1 = day1.select("td").get(4).text().replace(" ", "").replace("高温", "").replace("低温", "").replace("℃", "");
            String fx1 = day1.select("td").get(5).text();
            String fl1 = day1.select("td").get(6).text();

            String dayNight2 = day2.select("td").get(0).text();
            //                String img2 = day2.select("td").get(1).select("img").attr("src");
            String weatherDir2 = day2.select("td").get(2).text();
            int code2 = YahooCodeFromChinaTool.getYahooCode4Title(dayNight2 + weatherDir2);
            String temp2 = day2.select("td").get(3).text().replace(" ", "").replace("高温", "").replace("低温", "").replace("℃", "");
            String fx2 = day2.select("td").get(4).text();
            String fl2 = day2.select("td").get(5).text();

            JSONObject newDay = new JSONObject();
            newDay.put("startDate", date);
            newDay.put("entDate", date);
            newDay.put("highTemp", temp1);
            newDay.put("lowTemp", temp1);
            newDay.put("windLevel", fl1);
            newDay.put("windLevelInfo", fx1);
            newDay.put("windDirection", fx1);
            newDay.put("code", code1);
            newDay.put("codeInfo", weatherDir1);

            newDay.put("codeNight", code2);
            newDay.put("codeInfoNight", weatherDir2);
            newDay.put("windLevelNight", fl2);
            newDay.put("windLevelInfoNight", fx2);
            newDay.put("windDirectionNight", fx2);
            if (Integer.parseInt(temp1) < Integer.parseInt(temp2)) {
                newDay.put("highTemp", temp2);
                newDay.put("lowTemp", temp1);
            } else {
                newDay.put("lowTemp", temp2);
            }

            dayForcasts.add(newDay);
        }
        result.put("dayForcasts", dayForcasts);
        return result.toString();
    }

    /**
     * 各种指数
     * @param fetchData
     * @return
     *
     */
    private JSONObject getLiveInfo(String forcastWeather) {
        JSONObject fetchData = JSONObject.fromObject(forcastWeather).getJSONObject("weatherinfo");

        JSONObject liveInfo = JSONObject.fromObject(cityWeather.getLiveInfo());

        JSONObject cyInfo = new JSONObject();
        cyInfo.put("name", "穿衣");
        cyInfo.put("level", fetchData.getString("index"));
        cyInfo.put("dict", fetchData.getString("index_d"));
        liveInfo.put("zs_cy", cyInfo);

        JSONObject zwxInfo = new JSONObject();
        zwxInfo.put("name", "紫外线强度");
        zwxInfo.put("level", fetchData.getString("index_uv"));
        liveInfo.put("zs_zwx", zwxInfo);

        JSONObject xc = new JSONObject();
        JSONObject xcInfo = new JSONObject();
        xcInfo.put("name", "洗车");
        xcInfo.put("level", fetchData.getString("index_xc"));
        xc.put("zs_xc", xcInfo);

        JSONObject lyInfo = new JSONObject();
        lyInfo.put("name", "旅游");
        lyInfo.put("level", fetchData.getString("index_tr"));
        liveInfo.put("zs_ly", lyInfo);

        JSONObject clInfo = new JSONObject();
        clInfo.put("name", "晨练");
        clInfo.put("level", fetchData.getString("index_cl"));
        liveInfo.put("zs_cl", clInfo);

        JSONObject lsInfo = new JSONObject();
        lsInfo.put("name", "晾晒");
        lsInfo.put("level", fetchData.getString("index_ls"));
        liveInfo.put("zs_ls", lsInfo);

        JSONObject gmInfo = new JSONObject();
        gmInfo.put("name", "过敏");
        gmInfo.put("level", fetchData.getString("index_ag"));
        liveInfo.put("zs_gm", gmInfo);

        return liveInfo;
    }

    private static Double parseTemp(String text) {
        String tempString = null;
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            tempString = text.substring(matcher.start(), matcher.end());
        }
        try {
            return Double.valueOf(tempString);
        } catch (Exception e) {
        }
        return LowestTemp;

    }
}
