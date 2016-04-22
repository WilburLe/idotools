package com.toolbox.weather.action;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.toolbox.framework.utils.StringUtility;
import com.toolbox.weather.bean.CityBean;
import com.toolbox.weather.bean.CityWeatherBean;
import com.toolbox.weather.service.FindCityService;
import com.toolbox.weather.service.WeatherService;
import com.toolbox.weather.tools.ChinaWeatherAnalyze;
import com.toolbox.weather.tools.LanguageTool;
import com.toolbox.weather.tools.PM25Tool;

@Controller
@Scope("prototype")
public class WeatherDetailAction {
    @Autowired
    private FindCityService     cityService;
    @Autowired
    private WeatherService      weatherService;
    //////////////////////////////////////////////////////////////////
    private String              latlng;
    private String              lang;                                        //语言
    private String              mac;
    private int                 hourInterval = 3;                            //小时间距,默认3
    private int                 dayMerge;                                    //天是否合并,默认0 不合并，大于 0 合并
    ////////////////////////////////////////////////////////////////
    private Map<String, Object> result       = new HashMap<String, Object>();

    /**
     * 根据经纬度获取当前所在城市的天气信息
     * 
     * @return
     * 
     */
    @RequestMapping(value = "detail")
    public JSON getWeather() {
        String[] locs = latlng.split(",");
        double lat = Double.valueOf(locs[0]);
        double lng = Double.valueOf(locs[1]);

        lang = StringUtility.isEmpty(lang) ? "zh-hans" : lang;
        String[] langs = lang.split(",");
        lang = LanguageTool.get(langs[0].toLowerCase());
        CityBean city = cityService.findCity(lat, lng, lang);
        if (city == null) { //对应坐标城市为空
            result.put("state", "-1");
            return SUCCESS;
        }

        CityWeatherBean weatherBean = weatherService.getWeather(city, false, lang);
        result = new ChinaWeatherAnalyze(hourInterval, dayMerge).getCn(city, weatherBean, lang);
        //增加多个本地化处理
        Map<String, Object> localizations = new HashMap<String, Object>();
        for (int i = 1; i < langs.length; i++) {
            localizations.put(langs[i], localization(langs[i], result));
        }
        result.put("localization", localizations);

        return SUCCESS;
    }

    /**
     * 处理本地化信息
     * 
     * @param lang
     * @param result
     * @return
     * 
     */
    public Map<String, Object> localization(String lang, Map<String, Object> result) {
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
