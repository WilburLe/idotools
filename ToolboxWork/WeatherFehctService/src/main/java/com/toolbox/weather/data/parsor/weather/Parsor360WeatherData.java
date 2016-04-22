package com.toolbox.weather.data.parsor.weather;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toolbox.framework.utils.DateUtility;
import com.toolbox.framework.utils.HttpUtility;
import com.toolbox.framework.utils.StringUtility;
import com.toolbox.weather.bean.CityWeatherBean;
import com.toolbox.weather.enums.WeatherState;

public class Parsor360WeatherData {

    private CityWeatherBean  cityWeather;
    private JSONObject       docData;
    public List<String>      exceptionList = new ArrayList<String>();

    public List<String> getExceptionList() {
        return exceptionList;
    }

    public Parsor360WeatherData(CityWeatherBean cityWeather) {
        this.cityWeather = cityWeather;
    }

    public void parse() {
        Map<String, String> head = new HashMap<String, String>();
        head.put("Referer", "http://tq.360.cn/");
        head.put("Host", "tq.360.cn");
        head.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/40.0.2214.111 Safari/537.36");
        head.put("Cookie", "citycode=" + cityWeather.getWeatherCityId());
        String data = HttpUtility.get("http://tq.360.cn/api/weatherquery/query?app=tq360&code=" + cityWeather.getWeatherCityId(), null, head, "UTF-8", 10000);
        if (StringUtility.isEmpty(data)) {
            exceptionList.add(cityWeather.getId() + " > " + cityWeather.getWeatherCityId() + " 无数据！");
            return;
        }
        data = data.replace("callback(", "");
        data = data.substring(0, data.lastIndexOf(")"));
        docData = JSONObject.parseObject(data);
        cityWeather.setSource("360");

        curCode();
        try {
            weekInfo();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            hourInfo();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            liveInfo();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void curCode() {
        JSONObject curData = docData.getJSONObject("realtime");
        JSONObject weather = curData.getJSONObject("weather");
        JSONObject wind = curData.getJSONObject("wind");
        String feelslike = curData.getString("feelslike");
        String dataUptime = curData.getString("dataUptime");
        String time = curData.getString("time");
        int dayHour = Integer.parseInt(time.replace(":00:00", ""));

        WeatherState curWeather = WeatherState.getObject(((dayHour >= 18 && dayHour <= 8) ? "n" : "d") + weather.getString("img"));
        int curCode = curWeather != null ? NumberUtils.toInt(curWeather.getYahooCode(), -1) : -1;
        cityWeather.setCurCode(curCode);
        cityWeather.setCurTemp(weather.getDouble("temperature"));
        cityWeather.setCurCodeInfo(weather.getString("info"));
        cityWeather.setHumidity(weather.getIntValue("humidity"));
        cityWeather.setWindDirection(wind.getString("direct"));
        cityWeather.setWindLevel(Integer.parseInt(wind.getString("power").replace("级", "")));
        cityWeather.setWindLevelInfo(wind.getString("power"));
        cityWeather.setUnixTime(Integer.parseInt(dataUptime));
        if(curData.containsKey("trafficalert") && !curData.getJSONArray("trafficalert").isEmpty()) {
            cityWeather.setTrafficControls(curData.getJSONArray("trafficalert").toString());
        }
    }

    private void weekInfo() {
        JSONArray weekData = docData.getJSONArray("weather");
        JSONArray dayForcasts = new JSONArray();
        for (int i = 0; i < weekData.size(); i++) {
            JSONObject week = weekData.getJSONObject(i);
            String date = week.getString("date");
            JSONObject info = week.getJSONObject("info");
            if(info == null) {
                continue;
            }
            JSONArray day = info.getJSONArray("day");
            JSONArray night = info.getJSONArray("night");

            JSONObject data = new JSONObject();
            data.put("startDate", date);
            data.put("entDate", date);
            data.put("highTemp", day.get(2)); //高温
            data.put("lowTemp", night.get(2)); //低温
            WeatherState weather = WeatherState.getObject("d" + day.getString(0));
            int code = weather != null ? NumberUtils.toInt(weather.getYahooCode(), -1) : -1;
            WeatherState nWeather = WeatherState.getObject("n" + night.getString(0));
            int nCode = nWeather != null ? NumberUtils.toInt(nWeather.getYahooCode(), -1) : -1;
            data.put("code", code); //天气状态
            data.put("codeNight", nCode);
            data.put("codeInfo", day.get(1)); //天气状态
            data.put("codeInfoNight", night.get(1));
            data.put("windDirection", day.get(3)); //风向
            data.put("windDirectionNight", night.get(3));
            data.put("windLevel", day.get(4)); //风力
            data.put("windLevelNight", night.get(4));
            data.put("windLevelInfo", day.get(4)); //风力
            data.put("windLevelInfoNight", night.get(4));

            dayForcasts.add(data);
        }
        JSONObject result = new JSONObject();
        result.put("publicDate", DateUtility.format((int) (cityWeather.getUnixTime()), "yyyy-MM-dd HH:mm:ss"));
        result.put("dayForcasts", dayForcasts);
        cityWeather.setDayForcast(result.toString());
    }

    private void hourInfo() {
        JSONArray hourForcasts = new JSONArray();

        JSONArray hourly = docData.getJSONArray("hourly_forecast");
        boolean isToday = true;
        for (int i = 0; i < hourly.size(); i++) {
            JSONObject data = hourly.getJSONObject(i);
            String hour = data.getString("hour");

            // //////////--时间Start--///////////
            if (Integer.parseInt(hour) == 0) {
                isToday = false;
            }
            Calendar c = Calendar.getInstance();
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MINUTE, 0);
            String date = null;
            if (isToday) {
                c.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour));
                date = DateUtility.format(c, "yyyy-MM-dd HH:mm:ss");
            } else {
                c.add(Calendar.DAY_OF_MONTH, 1);
                c.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour));
                date = DateUtility.format(c, "yyyy-MM-dd HH:mm:ss");
            }
            // //////////--时间End--///////////

            JSONObject hourJson = new JSONObject();
            hourJson.put("date", date);
            hourJson.put("temp", data.getString("temperature"));// 温度
            WeatherState weather = null;
            if (isToday && Integer.parseInt(hour) <= 20) { // 白天
                weather = WeatherState.getObject("d" + data.getString("img"));
            } else { // 夜间
                weather = WeatherState.getObject("n" + data.getString("img"));
            }
            int code = weather != null ? NumberUtils.toInt(weather.getYahooCode(), -1) : -1;
            hourJson.put("code", code);
            hourJson.put("codeInfo", data.getString("info"));
            hourForcasts.add(hourJson);

        }
        JSONObject result = new JSONObject();
        result.put("publicDate", DateUtility.format(new Date()));
        result.put("hourForcasts", hourForcasts);
        cityWeather.setHourForcast(result.toString());
    }

    private void liveInfo() {
        JSONObject life = docData.getJSONObject("life");
        JSONObject info = life.getJSONObject("info");

        JSONObject result = new JSONObject();
        for (int i = 0; i < LiveName.values().length; i++) {
            LiveName liveName = LiveName.values()[i];
            JSONArray arr = info.getJSONArray(liveName.name());

            JSONObject json = new JSONObject();
            json.put("name", liveName.name);
            json.put("level", arr.getString(0));
            json.put("dict", arr.getString(1));
            result.put(liveName.code, json);
        }
        cityWeather.setLiveInfo(result.toString());
    }

    private enum LiveName {
        kongtiao("空调指数", "zs_kt"), //
        yundong("运动指数", "zs_yd"), //
        ziwaixian("紫外线指数", "zs_zwx"), //
        ganmao("感冒指数", "zs_gm"), //
        xiche("洗车指数", "zs_xc"), //
        wuran("污染指数", "zs_wr"), //
        chuanyi("穿衣指数", "zs_cy");

        String name;
        String code;

        LiveName(String name, String code) {
            this.name = name;
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }

    public static void main(String[] args) {

        CityWeatherBean cityWeather = new CityWeatherBean();
        //                cityWeather.setWeatherCityId(101320101);//香港
        cityWeather.setWeatherCityId(101010100);//北京
        //                cityWeather.setWeatherCityId(101120710);
        new Parsor360WeatherData(cityWeather).parse();

//        System.out.println(JSONObject.(cityWeather));

    }
}
