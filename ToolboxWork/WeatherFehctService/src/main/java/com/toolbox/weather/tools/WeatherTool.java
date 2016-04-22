package com.toolbox.weather.tools;

import java.util.HashMap;
import java.util.Map;

import com.toolbox.framework.utils.HttpUtility;

public class WeatherTool {

    /**
     * QQ 天气信息
     */
    public static String getQQFetchData(String qqCityId) {
        String url = "http://weather.gtimg.cn/city/" + qqCityId + ".js?ref=qqchannel";
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Host", "weather.gtimg.cn");
        headers.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:24.0) Gecko/20100101 Firefox/24.0");
        headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        headers.put("Accept-Language", "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
        headers.put("Accept-Encoding", " gzip, deflate");
        return HttpUtility.get(url, null, headers, "UTF-8", 30000);
    }

    /**
     * 当前天气
    {
    "weatherinfo": {
        "city": "北京",
        "cityid": "101010100",
        "temp": "10",       >当前温度
        "WD": "东北风",      >风向
        "WS": "4级",        >风力等级
        "SD": "56%",        >湿度
        "WSE": "4",         >风力等级
        "time": "16:40",    >数据更新时间
        "isRadar": "1",     >应该是雷达返回成功标识
        "Radar": "JC_RADAR_AZ9010_JB"   >雷达编号
    }
    }
     * @param weatherCityCode
     * @return
     *
     */
    public static String getCurrentWeather(String weatherCityCode) {
        return HttpUtility.get("http://www.weather.com.cn/data/sk/" + weatherCityCode + ".html", null);
    }

    /**
     * 当天天气详情
    {
    "weatherinfo": {
        "city": "北京",
        "cityid": "101010100",
        "temp1": "16℃",     >最高气温
        "temp2": "6℃",      >最低气温
        "weather": "小雨转晴",  >一天的天气状态
        "img1": "d7.gif",   >白天天气状态图片
        "img2": "n0.gif",   >夜间天气状态图片
        "ptime": "11:00"    >为系统最后更新时间
    }
    }
     * @param weatherCityCode
     * @return
     *
     */
    public static String getTodayWeather(String weatherCityCode) {
        return HttpUtility.get("http://www.weather.com.cn/data/cityinfo/" + weatherCityCode + ".html", null);
    }
    
    /**
     * 一天内小时数据
     * 温度、湿度、降水量、风向 风力
     * XML 格式
     * @param weatherCityCode
     * @return
     *
     */
    public static String getHourWeather(String weatherCityCode) {
        return HttpUtility.get("http://flash.weather.com.cn/sk2/" + weatherCityCode + ".xml", null);
    }

    /**
     * 预测天气
    {"weatherinfo":{
    //基本信息
    //fchh是系统更新时间
    "city":"北京","city_en":"beijing","date_y":"2011年10月23 日","date":"辛卯年","week":"星期 日","fchh":"11","cityid":"101010100",
    //6天内的温度
    "temp1":"16℃~6℃","temp2":"16℃~3℃","temp3":"16℃~6℃","temp4":"14℃~6℃",
    "temp5":"17℃~5℃","temp6":"18℃~8℃",
    //6天内华氏度
    "tempF1":"60.8℉~42.8℉","tempF2":"60.8℉~37.4℉","tempF3":"60.8℉~42.8℉",
    "tempF4":"57.2℉~42.8℉","tempF5":"62.6℉~41℉","tempF6":"64.4℉~46.4℉",
    //每日天气变化
    "weather1":" 小雨转晴","weather2":"晴","weather3":"晴转阴","weather4":"多云转阴","weather5":"阴转 晴","weather6":"晴转多 云",
    //天气描述（图片序号）
    "img1":"7","img2":"0",
    "img3":"0","img4":"99",
    "img5":"0","img6":"2",
    "img7":"1","img8":"2",
    "img9":"2","img10":"0",
    "img11":"0","img12":"1",
    "img_single":"7",
    //天气描述（文字描述）
    "img_title1":" 小雨","img_title2":"晴",
    "img_title3":"晴","img_title4":"晴",
    "img_title5":" 晴","img_title6":"阴",
    "img_title7":"多云","img_title8":"阴",
    "img_title9":" 阴","img_title10":"晴",
    "img_title11":"晴","img_title12":"多 云",
    "img_title_single":"小雨",
    //风向描述
    "wind1":"北风4-5级","wind2":"北风3-4级转微 风","wind3":"微风","wind4":"微风","wind5":"微风","wind6":"微风",
    "fx1":"北 风","fx2":"北风",
    //风力描述
    "fl1":"4-5级","fl2":"3-4级转小于3级","fl3":"小于3级","fl4":"小于3 级","fl5":"小于3级","fl6":"小于3级",
    //今天穿衣指数
    "index":"温凉","index_d":"较凉爽，建议着夹衣加薄羊毛衫等春秋服 装。体弱者宜着夹衣加羊毛衫。因昼夜温差较大，注意增减衣服。",
    //48小时内穿衣指数
    "index48":"温凉","index48_d":"较凉爽，建议着夹衣加薄羊毛 衫等春秋服装。体弱者宜着夹衣加羊毛衫。因昼夜温差较大，注意增减衣服。",
    //紫外线
    "index_uv":"最弱","index48_uv":"中 等",
    //洗车
    "index_xc":"不宜",
    //旅游
    "index_tr":"适宜",
    //人体舒适度
    "index_co":"较舒 适",
    //未知
    "st1":"14","st2":"3","st3":"14","st4":"5","st5":"15","st6":"5",
    //晨练
    "index_cl":" 较不宜",
    //晾衣
    "index_ls":"不宜",
    //过敏
    "index_ag":"极不易发"}}
     * @param weatherCityCode
     * @return
     *
     */
    public static String getForecastWeather(String weatherCityCode) {
        return HttpUtility.get("http://m.weather.com.cn/data/" + weatherCityCode + ".html", null);
    }

}
