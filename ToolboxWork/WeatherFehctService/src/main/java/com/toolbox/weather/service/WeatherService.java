package com.toolbox.weather.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.toolbox.framework.i18n.api.TextManager;
import com.toolbox.framework.utils.ConfigUtility;
import com.toolbox.framework.utils.DateUtility;
import com.toolbox.weather.bean.CityBean;
import com.toolbox.weather.bean.CityWeatherAlarmBean;
import com.toolbox.weather.bean.CityWeatherBean;
import com.toolbox.weather.bean.CityWeatherRankBean;
import com.toolbox.weather.dao.CityWeatherAlarmDao;
import com.toolbox.weather.dao.CityWeatherDao;
import com.toolbox.weather.tools.CityTool;
import com.toolbox.weather.tools.LanguageTool;

@Service
public class WeatherService {
    @Autowired
    private FindCityService     cityService;
    @Autowired
    private CityWeatherDao      cityWeatherDao;
    @Autowired
    private CityWeatherAlarmDao cityWeatherAlarmDao;

    /**
     * 查找天气信息
     * 
     * @param city
     * @param lat
     * @param lng
     * @param lang
     * @return
     * 
     */
    public CityWeatherBean getWeather(CityBean city, boolean simple, String lang) {
        try {
            if (city == null) {
                return null;
            }
            CityWeatherBean weatherBean = getWeatherBean(city, simple);//查找信息，并构建WeatherBean的具体过程

            lang = LanguageTool.get(lang);
            if ("zh_hans".equals(lang)) {
                return weatherBean;
            }
            if (CityTool.isTWCity(city.getId()) && "zh_hant".equals(lang)) {
                return weatherBean;
            }
            return formatLang(weatherBean, LanguageTool.get(lang));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将天气信息根据语言转换
     * 
     * @param bean
     * @param lang
     * @return
     * 
     */
    private CityWeatherBean formatLang(CityWeatherBean bean, String lang) {
        if (bean == null) {
            return bean;
        }
        String file_prefix = "weather_";
        String code_prefix = "conditionDesc.";
        bean.setCodeInfo(TextManager.getString(file_prefix + lang, code_prefix + bean.getCode()));
        bean.setCurCodeInfo(TextManager.getString(file_prefix + lang, code_prefix + bean.getCurCode()));
        return bean;
    }

    /**
     * 查找并构建天气bean
     * 
     * @param city
     * @param lat
     * @param lng
     * @return
     * 
     */
    private CityWeatherBean getWeatherBean(CityBean city, boolean simple) {
        if (city == null) {
            return null;
        }
        //获取城市天气对应信息
        CityWeatherBean cityWeather = findCityWeather(city, simple); //从当前县，往上到最上层，也没有找到天气网对应的城市
        if (cityWeather != null) {
            if (DateUtility.currentUnixTime() - cityWeather.getCreateDate() < ConfigUtility.getInstance().getInteger("weather.overtime")) {
                return cityWeather;
            } else { //超过间隔时间，数据没有重新抓取
                //                if ("china".equalsIgnoreCase(cityWeather.getSource())) {
                //                    //                    new ParsorChinaWeatherData(cityWeather, city).parse();
                //                    new ParsorChinaWeatherData2(cityWeather).parse();
                //                    updateCityWeather(cityWeather);
                //                }
            }
        }
        return cityWeather;
    }

    /**
     * 重要 如果所查城市不是中国天气记录城市，则查找上级城市，直到 parentId ==0
     * 
     * @param cityId
     *            城市id
     * @return WeatherCityBean （城市id 与 中国天气城市 的对应Bean）
     * 
     */
    private CityWeatherBean findCityWeather(CityBean city, boolean simple) {
        if (city == null) {
            return null;
        }
        CityWeatherBean cityWeather = null;
        if (simple) {
            cityWeather = findSimpleCityWeather(city.getId());
        } else {
            cityWeather = findCityWeather(city.getId());
        }
        if (cityWeather == null && city.getParentId() != 0) {
            CityBean parent = cityService.findCity(city.getParentId());
            if (parent == null) {
                return null;
            }
            cityWeather = findCityWeather(parent, simple);
        }
        return cityWeather;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void addFetchCount(long id) {
        cityWeatherDao.addFetchCount(id);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public int updateCityWeather(CityWeatherBean cityWeather) {
        return cityWeatherDao.updateCityWeather(cityWeather);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<CityBean> findAllChinaCity() {
        return cityWeatherDao.findAllChinaCity();
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public CityWeatherBean findCityWeather(int cityId) {
        return cityWeatherDao.findCityWeather(cityId);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public CityWeatherBean findSimpleCityWeather(int cityId) {
        return cityWeatherDao.findSimpleCityWeather(cityId);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<CityWeatherRankBean> tempAllRanking(boolean isTw, String seq, int limitNum) {
        return cityWeatherDao.tempAllRanking(isTw, seq, limitNum);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<CityWeatherRankBean> pmAllRanking(boolean isTw, String seq, int limitNum) {
        return cityWeatherDao.pmAllRanking(isTw, seq, limitNum);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public CityWeatherBean findCityWeatherByWeatherCityId(String weatherCityId) {
        return cityWeatherDao.find("where weatherCityId=?", weatherCityId);
    }

    ////////////////////////////////// 天气预警  START //////////////////////////////////////////
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void saveAlarm(CityWeatherAlarmBean bean) {
        cityWeatherAlarmDao.save(bean);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public CityWeatherAlarmBean findAlarmByCityIdAndPublicDate(String weatherCityId, Date publicDate) {
        return cityWeatherAlarmDao.find("where weatherCityId=? and publicDate=?", weatherCityId, publicDate);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<CityWeatherAlarmBean> findTodayAlarm() {
        Date today_min = DateUtility.getToday();
        Calendar c = Calendar.getInstance();
        c.setTime(today_min);
        c.add(Calendar.HOUR_OF_DAY, 24);
        return cityWeatherAlarmDao.finds("where publicDate between ? and ? order by publicDate", today_min, c.getTime());
    }

    ////////////////////////////////// 天气预警  END //////////////////////////////////////////

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void backupCityWeatherData() {
        cityWeatherDao.backupCityWeatherData();
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public CityWeatherBean findHistoryCityWeather(int cityId, int historyDate) {
        return cityWeatherDao.findHistoryCityWeather(cityId, historyDate);
    }
}
