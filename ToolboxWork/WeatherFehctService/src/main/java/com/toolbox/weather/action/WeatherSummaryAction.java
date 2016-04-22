package com.toolbox.weather.action;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.toolbox.framework.utils.StringUtility;
import com.toolbox.weather.bean.CityBean;
import com.toolbox.weather.bean.CityWeatherBean;
import com.toolbox.weather.service.FindCityService;
import com.toolbox.weather.service.WeatherService;
import com.toolbox.weather.tools.LanguageTool;
import com.toolbox.weather.tools.PM25Tool;

@Controller
public class WeatherSummaryAction {

    @Autowired
    private FindCityService     cityService;
    @Autowired
    private WeatherService      weatherService;
    //////////////////////////////////////////////////////////////////
    private String              latlng;
    private String              lang;                                  //语言
    private String              mac;
    private String              cityId;
    ////////////////////////////////////////////////////////////////
    private Map<String, Object> result = new HashMap<String, Object>();

    /**
     * 根据经纬度获取当前所在城市的天气信息
     * @return
     *
     */
    @RequestMapping(value = "summary")
    public String getWeather() {
        CityBean city = null;
        if (StringUtility.isEmpty(lang)) {
            lang = "zh-hans";
        }
        String[] langs = lang.split(",");
        lang = LanguageTool.get(langs[0].toLowerCase());

        if (StringUtility.isNotEmpty(cityId)) {
            city = cityService.findCity(Integer.valueOf(cityId), lang);
        } else {
            String[] locs = latlng.split(",");
            double lat = Double.valueOf(locs[0]);
            double lng = Double.valueOf(locs[1]);

            city = cityService.findCity(lat, lng, lang);
        }

        if (city == null) { //对应坐标城市为空
            result.put("state", "-1");
            return SUCCESS;
        }

        CityWeatherBean weatherBean = weatherService.getWeather(city, true, lang);
        if (weatherBean == null || weatherBean.getCurCode() == -1) {
            result.put("state", "-1");
            return SUCCESS;
        }

        int nowDate = DateUtility.currentUnixTime();
        if (nowDate - weatherBean.getCreateDate() > Integer.parseInt(Config.getInstance().getString("weather.overtime"))) {
            result.put("state", "-1");
            return SUCCESS;
        }

        result.put("cityId", city.getId());
        result.put("cityName", city.getName());
        result.put("code", weatherBean.getCode());
        result.put("codeInfo", weatherBean.getCodeInfo());
        result.put("curCode", weatherBean.getCurCode());
        result.put("curCodeInfo", weatherBean.getCurCodeInfo());
        result.put("curTemp", weatherBean.getCurTemp());
        result.put("high", weatherBean.getHigh());
        result.put("low", weatherBean.getLow());

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

        //*******增加多个本地化处理（2013-08-16）
        Map<String, Object> localizations = new HashMap<String, Object>();
        for (int i = 1; i < langs.length; i++) {
            localizations.put(langs[i], localization(langs[i], result));
        }
        result.put("localization", localizations);

        return SUCCESS;
    }

    /**
     * 处理本地化信息
     * @param lang
     * @param result
     * @return
     *
     */
    private Map<String, Object> localization(String lang, Map<String, Object> result) {
        Map<String, Object> tmp = new HashMap<String, Object>();
        lang = LanguageTool.get(lang);
        String file_prefix = "weather_";
        String code_prefix = "conditionDesc.";
        tmp.put("codeInfo", TextManager.getString(file_prefix + lang, code_prefix + result.get("code")));
        tmp.put("curCodeInfo", TextManager.getString(file_prefix + lang, code_prefix + result.get("curCode")));
        tmp.put("cityName", cityService.findCity(Integer.parseInt(result.get("cityId").toString()), lang).getName());

        if (result.containsKey("pm25Level"))
            tmp.put("pm25Level", PM25Tool.getPm25Level(Integer.parseInt(result.get("pm25").toString())));
        if (result.containsKey("pm25LevelInfo"))
            tmp.put("pm25LevelInfo", PM25Tool.getPm25LevelInfo(Integer.parseInt(tmp.get("pm25Level").toString()), lang));
        if (result.containsKey("pm25Info"))
            tmp.put("pm25Info", PM25Tool.getPm25LevelInfo(Integer.parseInt(tmp.get("pm25Level").toString()), lang)); //暂时与 等级info相同

        return tmp;
    }

    public String getLatlng() {
        return latlng;
    }

    public void setLatlng(String latlng) {
        this.latlng = latlng;
    }

    public Map<String, Object> getResult() {
        return result;
    }

    public void setResult(Map<String, Object> result) {
        this.result = result;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

}
