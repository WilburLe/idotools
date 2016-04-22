package com.toolbox.weather.action;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toolbox.framework.utils.DateUtility;
import com.toolbox.framework.utils.HttpUtility;
import com.toolbox.framework.utils.StringUtility;
import com.toolbox.weather.bean.CityBean;
import com.toolbox.weather.bean.CityWeatherBean;
import com.toolbox.weather.enums.GlobalWeather;
import com.toolbox.weather.service.FindCityService;
import com.toolbox.weather.service.WeatherService;
import com.toolbox.weather.tools.ChinaWeatherAnalyze;
import com.toolbox.weather.tools.LanguageTool;

@Controller
public class GlobalWeatherAction {
    @Autowired
    private FindCityService cityService;
    @Autowired
    private WeatherService  weatherService;

    /////////////////////////////////////////////////////////////
    private String              latlng;
    private String              lang;
    private int                 hourInterval = 1;                            //小时间距,默认1
    private int                 dayMerge     = 1;                            //天是否合并,默认0 不合并，大于 0 合并
    /////////////////////////////////////////////////////////////
    private Map<String, Object> result       = new HashMap<String, Object>();

    @RequestMapping(value = "globalWeather")
    public JSON globalWeather() {

        String[] locs = latlng.split(",");
        double lat = Double.valueOf(locs[0]);
        double lng = Double.valueOf(locs[1]);

        lang = StringUtility.isEmpty(lang) ? "zh-hans" : lang;
        String[] langs = lang.split(",");
        lang = LanguageTool.get(langs[0].toLowerCase());
        CityBean city = cityService.findCity(lat, lng, lang);
        if (city == null || StringUtility.isEmpty(city.getTreePath()) || city.getTreePath().indexOf("[1]") < 0) {
            //        if (city == null) {
            //国外
            foreign(LanguageTool.globalMap.get(lang));
        } else {
            //国内
            CityWeatherBean weatherBean = weatherService.getWeather(city, false, lang);
            result = new ChinaWeatherAnalyze(hourInterval, dayMerge).getCn(city, weatherBean, lang);
        }

        return SUCCESS;
    }

    //geolookup 地理信息
    //forecast10day 十天天气预测信息
    //hourly 三天小时天气预测信息
    //conditions 实时天气信息
    //astronomy 预警信息
    //http://api.wunderground.com/api/c382274be922547e/forecast10day/hourly/conditions/lang:CN/q/39.9,116.45.json

    private void foreign(String lang) {
        String url = "http://api.wunderground.com/api/c382274be922547e/forecast10day/conditions/hourly/lang:" + lang + "/q/" + latlng + ".json";
        String res = HttpUtility.get(url);
        if (StringUtility.isEmpty(res)) {
            return;
        }
        JSONObject json = JSONObject.fromObject(res);
        //返回信息描述
        JSONObject response = json.optJSONObject("response");
        if (response.containsKey("error")) {
            return;
        }
        //实时天气信息
        JSONObject current_observation = json.optJSONObject("current_observation");
        curr(current_observation);
        //十天 预测信息
        JSONObject forecast = json.optJSONObject("forecast");
        dayForecast(forecast);
        //36小时预测信息
        JSONArray hourly_forecast = json.optJSONArray("hourly_forecast");
        hourlyForecast(hourly_forecast);
        //日出  日落
        //JSONObject sun_phase = json.optJSONObject("sun_phase");
        //sunphase(sun_phase);
        //月出 月落
        //JSONObject moon_phase = json.optJSONObject("moon_phase");
        //预警信息
        //JSONObject astronomy = json.optJSONObject("astronomy");

    }

    private void curr(JSONObject current) {
        //地里位置
        JSONObject location = current.optJSONObject("display_location");
        String city = location.optString("city");
        String country = location.optString("state_name");
        result.put("cityName", city);

        //观察时间【天气信息发布时间】（1418961616）
        String observation_epoch = current.optString("observation_epoch");
        result.put("unixTime", observation_epoch);

        //当前时间（1418975901）
        String local_epoch = current.optString("local_epoch");
        //天气描述（晴）
        String weather = current.optString("weather");
        //天气图标代码（clear）
        String icon = current.optString("icon");
        int code = GlobalWeather.getCode(icon);
        result.put("code", code);
        result.put("codeInfo", weather);
        result.put("curCode", code);
        result.put("curCodeInfo", weather);

        //华氏温度（37.2）
        String temp_f = current.optString("temp_f");
        //摄氏温度（2.9）
        String temp_c = current.optString("temp_c");
        result.put("curTemp", temp_c);
        //result.put("high", temp_c);
        //result.put("low", weatherBean.getLow());

        //相对湿度 （23%）
        String relative_humidity = current.optString("relative_humidity");
        result.put("humidity", relative_humidity);

        //风向描述（From the 西北 at 12.0 MPH Gusting to 15.0 MPH）
        String wind_string = current.optString("wind_string");
        //风向（西北风）
        String wind_dir = current.optString("wind_dir");
        //风向角度（317）
        String wind_degrees = current.optString("wind_degrees");
        //风速（千米每小时）
        int wind_kph = current.optInt("wind_kph");
        int windLevel = GlobalWeather.getWindLevel(wind_kph);
        result.put("windLevel", windLevel);
        result.put("windLevelInfo", GlobalWeather.wp[windLevel]);
        result.put("windDirection", wind_dir);

        //可见度
        String visibility_mi = current.optString("visibility_mi");
        result.put("visibility", visibility_mi);
        //一天降水量
        String precip_today_in = current.optString("precip_today_in");

    }

    private void dayForecast(JSONObject days) {
        JSONArray arr = new JSONArray();
        JSONObject simpleforecast = days.optJSONObject("simpleforecast");
        JSONArray forecastday = simpleforecast.optJSONArray("forecastday");
        for (int i = 0; i < forecastday.size(); i++) {
            JSONObject json = new JSONObject();

            JSONObject day = forecastday.getJSONObject(i);
            //日期
            JSONObject date = day.optJSONObject("date");
            //时间（1418986800）
            String epoch = date.optString("epoch");
            json.put("startDate", DateUtility.format(Integer.parseInt(epoch), "yyyy-MM-dd"));
            json.put("endDate", DateUtility.format(Integer.parseInt(epoch), "yyyy-MM-dd"));

            //高低温度
            JSONObject high = day.optJSONObject("high");
            String high_info = high.optString("celsius");
            JSONObject low = day.optJSONObject("low");
            String low_info = low.optString("celsius");
            json.put("highTemp", high_info);
            json.put("lowTemp", low_info);

            //当天的最高 最低温度
            if (i == 0) {
                result.put("high", high_info);
                result.put("low", low_info);
            }

            //天气描述（晴）
            String conditions = day.optString("conditions");
            String icon = day.optString("icon");
            json.put("code", GlobalWeather.getCode(icon));
            json.put("codeInfo", conditions);
            json.put("codeNight", GlobalWeather.getCode(icon));
            json.put("codeInfoNight", conditions);

            //全天降水量预测
            JSONObject qpf_allday = day.optJSONObject("qpf_allday");
            //全天下雪量预测
            JSONObject snow_allday = day.optJSONObject("snow_allday");

            //平均风力
            JSONObject avewind = day.optJSONObject("avewind");
            String wind_dir = avewind.optString("dir");
            int wind_kph = avewind.optInt("kph");
            int windLevel = GlobalWeather.getWindLevel(wind_kph);
            json.put("windLevel", windLevel);
            json.put("windDirection", wind_dir);
            json.put("windLevelInfo", GlobalWeather.wp[windLevel]);
            json.put("windLevelNight", windLevel);
            json.put("windDirectionNight", wind_dir);
            json.put("windLevelInfoNight", GlobalWeather.wp[windLevel]);

            //平均湿度
            JSONObject avehumidity = day.optJSONObject("avehumidity");

            arr.add(json);
        }
        JSONObject data = new JSONObject();
        data.put("dayForcasts", arr);
        data.put("publicDate", DateUtility.format(new Date(), "yyyy-MM-dd HH:mm:ss"));

        result.put("dayForcast", data);
    }

    private void hourlyForecast(JSONArray hourlys) {
        JSONArray arr = new JSONArray();
        for (int i = 0; i < hourlys.size(); i++) {
            JSONObject json = new JSONObject();

            JSONObject hourly = hourlys.getJSONObject(i);
            //日期
            JSONObject fcttime = hourly.optJSONObject("FCTTIME");
            //年、月、日
            String year = fcttime.optString("year");
            String mon = fcttime.optString("mon");
            String mday = fcttime.optString("mday");
            //时间（17）
            String hour = fcttime.optString("hour");
            json.put("date", year + "-" + mon + "-" + mday + " " + hour + ":00:00");

            //高低温度
            JSONObject temp = hourly.optJSONObject("temp");
            String max_temp = temp.optString("metric");
            JSONObject dewpoint = hourly.optJSONObject("dewpoint");
            String min_temp = dewpoint.optString("metric");
            json.put("temp", max_temp);

            //天气描述（晴）
            String condition = hourly.optString("condition");
            //天气图标
            String icon = hourly.optString("icon");
            json.put("codeInfo", condition);
            json.put("code", GlobalWeather.getCode(icon));

            //风速
            JSONObject wspd = hourly.optJSONObject("wspd");
            int wspd_info = wspd.optInt("metric");
            int windLevel = GlobalWeather.getWindLevel(wspd_info);
            //风向描述
            JSONObject wdir = hourly.optJSONObject("wdir");
            String wdir_info = wdir.optString("metric");
            json.put("windDirection", wdir_info);
            json.put("windLevel", windLevel);
            json.put("windLevelInfo", GlobalWeather.wp[windLevel]);

            //湿度
            String humidity = hourly.optString("humidity");
            json.put("humidity", humidity);

            //降水量
            JSONObject qpf = hourly.optJSONObject("qpf");
            String qpf_info = qpf.optString("metric");
            json.put("precipitation", qpf_info);

            //下雪量
            JSONObject snow = hourly.optJSONObject("snow");
            String snow_info = snow.optString("metric");

            arr.add(json);
        }
        JSONObject data = new JSONObject();
        data.put("hourForcasts", arr);
        data.put("publicDate", DateUtility.format(new Date(), "yyyy-MM-dd HH:mm:ss"));

        result.put("hourForcast", data);
    }

    private void sunphase(JSONObject sun_phase) {
        //日出时间
        JSONObject sunrise = sun_phase.optJSONObject("sunrise");
        //日落时间
        JSONObject sunset = sun_phase.optJSONObject("sunset");
    }

    public String getLatlng() {
        return latlng;
    }

    public void setLatlng(String latlng) {
        this.latlng = latlng;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public Map<String, Object> getResult() {
        return result;
    }

    public void setResult(Map<String, Object> result) {
        this.result = result;
    }

    public int getHourInterval() {
        return hourInterval;
    }

    public void setHourInterval(int hourInterval) {
        this.hourInterval = hourInterval;
    }

    public int getDayMerge() {
        return dayMerge;
    }

    public void setDayMerge(int dayMerge) {
        this.dayMerge = dayMerge;
    }

}
