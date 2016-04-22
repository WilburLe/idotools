package com.toolbox.weather.tools;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toolbox.framework.utils.AstronomyUtility;
import com.toolbox.framework.utils.ConfigUtility;
import com.toolbox.framework.utils.DateUtility;
import com.toolbox.framework.utils.StringUtility;
import com.toolbox.weather.bean.CityBean;
import com.toolbox.weather.bean.CityWeatherBean;

public class ChinaWeatherAnalyze {
    private int hourInterval = 3; //小时间距,默认3
    private int dayMerge;        //天是否合并,默认0 不合并，大于 0 合并

    public ChinaWeatherAnalyze(int hourInterval, int dayMerge) {
        this.dayMerge = dayMerge;
        this.hourInterval = hourInterval;
    }

    public Map<String, Object> getCn(CityBean city, CityWeatherBean weatherBean, String lang) {
        Map<String, Object> result = new HashMap<String, Object>();
        if (weatherBean == null || weatherBean.getCurCode() == -1) {
            result.put("state", "-1");
            return result;
        }

        int nowDate = DateUtility.currentUnixTime();
        if (nowDate - weatherBean.getCreateDate() > Integer.parseInt(ConfigUtility.getInstance().getString("weather.overtime"))) {
            result.put("state", "-1");
            return result;
        }

        result.put("cityId", city.getId());
        result.put("cityName", city.getName());
        result.put("cityNamePy", city.getName_py());
        result.put("code", weatherBean.getCode());
        result.put("codeInfo", weatherBean.getCodeInfo());
        result.put("curCode", weatherBean.getCurCode());
        result.put("curCodeInfo", weatherBean.getCurCodeInfo());
        result.put("curTemp", weatherBean.getCurTemp());
        result.put("high", weatherBean.getHigh());
        result.put("low", weatherBean.getLow());

        result.put("windLevel", weatherBean.getWindLevel());
        result.put("windLevelInfo", weatherBean.getWindLevelInfo());
        result.put("windDirection", weatherBean.getWindDirection());
        result.put("liveInfo", StringUtility.isNotEmpty(weatherBean.getLiveInfo()) ? JSONObject.parseObject(weatherBean.getLiveInfo()) : null);
        result.put("humidity", weatherBean.getHumidity());
        result.put("visibility", weatherBean.getVisibility());
        result.put("hourForcast", subHour(weatherBean.getHourForcast()));
        result.put("dayForcast", mergeDay(weatherBean.getDayForcast()));
        
        if (StringUtility.isNotEmpty(weatherBean.getTrafficControls())) {
            result.put("trafficControls", weatherBean.getTrafficControls()); //限行
        }
        //        result.put("trafficControls", trafficControlsTool.getNum(new Date(), city.getId()));    //限行
        int pm25 = weatherBean.getPm25();
        result.put("pm25", pm25);
        if (pm25 > 0) {
            //pm25排名
            result.put("pm25Ranking", weatherBean.getPmRanking());
            int pm25Level = PM25Tool.getLevel(pm25).getLevel();
            result.put("pm25Level", pm25Level);
            String pm25LevelInfo = PM25Tool.getPm25LevelInfo(pm25Level, lang);
            result.put("pm25LevelInfo", pm25LevelInfo);
            result.put("pm25Info", pm25LevelInfo); //暂时与 等级info相同
        }

        result.put("sunriseTime", AstronomyUtility.getSunrise(city.getLocationLat(), city.getLocationLng()).getTime() / 1000);
        result.put("sunsetTime", AstronomyUtility.getSunset(city.getLocationLat(), city.getLocationLng()).getTime() / 1000);
        result.put("unixTime", weatherBean.getUnixTime());
        result.put("tempRanking", weatherBean.getTempRanking());

        String extInfo = weatherBean.getExtInfo();
        if (StringUtility.isNotEmpty(extInfo)) {
            try {
                JSONArray toDayAlarms = new JSONArray();
                JSONArray alarms = JSONObject.parseObject(extInfo).getJSONArray("alarms");
                for (int i = 0; i < alarms.size(); i++) {
                    JSONObject alarm = alarms.getJSONObject(i);
                    int publicDate = alarm.getIntValue("publicDate");
                    if (!DateUtility.isToday(publicDate)) {
                        continue;
                    }
                    toDayAlarms.add(alarm);
                }
                result.put("alarms", toDayAlarms); //预警信息

            } catch (Exception e) {

            }
        }
        return result;
    }

    public int getDayMerge() {
        return dayMerge;
    }

    public void setDayMerge(int dayMerge) {
        this.dayMerge = dayMerge;
    }

    public JSONObject mergeDay(String dayForcast) {
        if (StringUtility.isEmpty(dayForcast)) { return null; }
        JSONObject dayData = JSONObject.parseObject(dayForcast);
        if (dayMerge > 0) { return dayData; }
        JSONArray days = dayData.getJSONArray("dayForcasts");
        if (days.size() == 0 || !days.getJSONObject(0).containsKey("codeNight")) { return dayData; }

        JSONObject newDayData = new JSONObject();
        JSONArray newDays = new JSONArray();

        for (int i = 0; i < days.size(); i++) {
            JSONObject day = days.getJSONObject(i);
            String startDate = day.getString("startDate");
            Calendar c = Calendar.getInstance();
            c.setTime(DateUtility.parseDate(startDate, "yyyy-MM-dd"));
            c.set(Calendar.HOUR_OF_DAY, 8);
            String date1 = DateUtility.format(c);
            c.add(Calendar.HOUR_OF_DAY, 12);
            String date2 = DateUtility.format(c);
            c.add(Calendar.HOUR_OF_DAY, 12);
            String date3 = DateUtility.format(c);

            JSONObject dayInfo = new JSONObject();
            JSONObject nightInfo = new JSONObject();
            dayInfo.put("startDate", date1);
            dayInfo.put("entDate", date2);
            dayInfo.put("code", day.getString("code"));
            dayInfo.put("codeInfo", day.getString("codeInfo"));
            dayInfo.put("highTemp", day.getString("highTemp"));
            dayInfo.put("lowTemp", "");
            dayInfo.put("wind", day.getString("windDirection"));
            dayInfo.put("windLevl", day.getString("windLevel"));
            dayInfo.put("windLevelInfo", day.getString("windLevelInfo"));
            dayInfo.put("windDirection", day.getString("windDirection"));

            nightInfo.put("startDate", date2);
            nightInfo.put("entDate", date3);
            nightInfo.put("code", day.getString("codeNight"));
            nightInfo.put("codeInfo", day.getString("codeInfoNight"));
            nightInfo.put("highTemp", "");
            nightInfo.put("lowTemp", day.getString("lowTemp"));
            nightInfo.put("wind", day.getString("windDirectionNight"));
            nightInfo.put("windLevl", day.getString("windLevelNight"));
            nightInfo.put("windLevelInfo", day.getString("windLevelInfoNight"));
            nightInfo.put("windDirection", day.getString("windDirectionNight"));

            newDays.add(dayInfo);
            newDays.add(nightInfo);
        }
        newDayData.put("dayForcasts", newDays);
        return newDayData;
    }

    /**
     * 按小时区间切割
     * 
     * @param hourForcast
     * @return
     * 
     */
    public JSONObject subHour(String hourForcastStr) {
        if (StringUtility.isEmpty(hourForcastStr)) { return null; }
        JSONObject hourForcast = JSONObject.parseObject(hourForcastStr);
        if (hourInterval <= 1 || StringUtility.isEmpty(hourForcast.getString("hourForcasts"))) { return hourForcast; }
        JSONArray hourForcast_ = new JSONArray();
        JSONArray hours = hourForcast.getJSONArray("hourForcasts");
        if (hours.size() == 0 || !hours.getJSONObject(0).containsKey("date")) { return hourForcast; }
        int count = (hours.size() / hourInterval) + (hours.size() % hourInterval);
        for (int i = 0; i < count; i++) {
            int fromIndex = i * hourInterval;
            int toIndex = (i + 1) * hourInterval;
            if (fromIndex > hours.size()) {
                break;
            }
            List<JSONObject> subList = hours.subList(fromIndex, toIndex > hours.size() ? hours.size() : toIndex);
            if (subList == null || subList.size() == 0) {
                break;
            }
            JSONObject first = subList.get(0);
            JSONObject last = subList.get(subList.size() - 1);
            first.put("startDate", first.getString("date"));
            first.put("endDate", last.getString("date"));
            first.remove("date");
            hourForcast_.add(first);
        }
        hourForcast.put("hourForcasts", hourForcast_);
        return hourForcast;
    }

}
