package com.toolbox.weather.data.parsor.weather;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toolbox.framework.thread.ThreadManager;
import com.toolbox.framework.utils.DateUtility;
import com.toolbox.framework.utils.HttpUtility;
import com.toolbox.framework.utils.RegexUtility;
import com.toolbox.framework.utils.StringUtility;
import com.toolbox.weather.bean.CityWeatherBean;
import com.toolbox.weather.enums.WeatherState;

public class ParsorChinaWeatherData2 {

    private CityWeatherBean  cityWeather;
    private Document         docData;
    public List<String>      exceptionList = new ArrayList<String>();
    private static final int erroe_temp    = -9999;

    public List<String> getExceptionList() {
        return exceptionList;
    }

    public ParsorChinaWeatherData2(CityWeatherBean cityWeather) {
        this.cityWeather = cityWeather;
    }

    public void parse() {
        int weatherCityId = cityWeather.getWeatherCityId();
        String data = HttpUtility.get("http://www.weather.com.cn/weather1d/" + weatherCityId + ".shtml?_" + DateUtility.currentUnixTime(), null, "UTF-8", 15000);
        if (StringUtility.isEmpty(data) || data.equals("<!-- empty -->")) {
            exceptionList.add(cityWeather.getId() + " > " + weatherCityId + " 无数据！");
            return;
        }
        docData = Jsoup.parse(data);

        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Host", "d1.weather.com.cn");
        headers.put("Referer", "http://www.weather.com.cn/alarm/newalarmlist.shtml?areaId=" + weatherCityId);
        String url = "http://d1.weather.com.cn/dingzhi/" + weatherCityId + ".html";
        String dataInfo = HttpUtility.get(url, null, headers, "UTF-8", 0);
        dataInfo = dataInfo.replaceAll(" ", "");

        //尾号限行
        trafficControls();
        //当前天气
        curInfo();
        //灾害预警
        weatherAlarms(dataInfo);
        //24小时天气预测
        hourInfo();
        //七天天气预测
        weekInfo();
        //生活指数
        liveInfo();
    }

    private void trafficControls() {
        try {
            String wh = docData.select("#today .t .sk .limit em").text();
            wh = StringUtility.isEmpty(wh) ? "-2" : wh;
            cityWeather.setTrafficControls(wh);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void curInfo() {
        //        int weatherCityId = cityWeather.getWeatherCityId();
        //        String cityDZ = RegexUtility.findGroup(dataInfo, "cityDZ" + weatherCityId + "=\\{.+?\\}\\]\\}", 0);
        //
        //        cityDZ = cityDZ.replaceAll("&quot;", "\"").replace("cityDZ" + weatherCityId + "=", "");
        //        JSONObject cityDZJson = JSONObject.fromObject(cityDZ);

        ////////////--白天--///////////////
        Element todatT = docData.select("#today ul[class=clearfix] li").get(0);
        String codeImage = todatT.select("big").attr("class"); //天气状态图片    jpg80 d04
        String codeInfo = todatT.select(".wea").text(); //天气描述
        String tempDay = todatT.select(".tem span").text(); //气温
        String windLevelDay = todatT.select(".win span").attr("title") + todatT.select(".win span").text(); //风力
        String sunUp = todatT.select(".sunUp").text();//日出时间

        ////////////--夜间--///////////////
        Element todatN = docData.select("#today ul[class=clearfix] li").get(1);
        String nightCodeImage = todatN.select("big").attr("class");
        String nightCodeInfo = todatN.select(".wea").text();
        String nightTempDay = todatN.select(".tem span").text();
        String nightWindLevelDay = todatN.select(".win span").attr("title") + todatN.select(".win span").text();
        String sunDown = todatN.select(".sunUp").text();

        ////////////--当前--///////////////
        String hidden_title = docData.select("#hidden_title").attr("value");
        String publishTime = docData.select("#fc_24h_internal_update_time").attr("value");

        Map<String, String> headers = new HashMap<String, String>();
        headers.put("User-Agent", " Mozilla/5.0 (Windows NT 6.1; rv:29.0) Gecko/20100101 Firefox/29.0");
        headers.put("Host", "d1.weather.com.cn");
        headers.put("Referer", "http://www.weather.com.cn/weather1d/" + cityWeather.getWeatherCityId() + ".shtml");//要和请求数据一致
        String html = HttpUtility.get("http://d1.weather.com.cn/sk_2d/" + cityWeather.getWeatherCityId() + ".html", null, headers, "UTF-8", 0);
        String info = RegexUtility.findGroup(html, "(\\{).*?(\\})", 0);
        JSONObject json = JSONObject.parseObject(info);
        int curTemp = json.containsKey("temp") ? json.getIntValue("temp") : erroe_temp;
        String windDirection = json.getString("WD");
        String windLevel = json.getString("WS");
        String humidity = json.getString("SD");
        String time = json.getString("time");
        String curCodeInfo = json.getString("weather");
        String weathercode = json.getString("weathercode"); //d02
        String aqi = json.getString("aqi");
        cityWeather.setPm25(NumberUtils.toInt(aqi, -1));
        cityWeather.setPmSource("中国天气网");
        cityWeather.setHumidity(NumberUtils.toInt(humidity.replace("%", ""), -1));
        if (NumberUtils.toDouble(tempDay) >= NumberUtils.toDouble(nightTempDay)) {
            cityWeather.setHigh(NumberUtils.toDouble(tempDay));
            cityWeather.setLow(NumberUtils.toDouble(nightTempDay));
        } else {
            cityWeather.setHigh(NumberUtils.toDouble(nightTempDay));
            cityWeather.setLow(NumberUtils.toDouble(tempDay));
        }
        //
        WeatherState curWeather = WeatherState.getObject(weathercode);
        int curCode = curWeather != null ? NumberUtils.toInt(curWeather.getYahooCode(), -1) : -1;
        cityWeather.setCurCode(curCode);
        curCodeInfo = (StringUtility.isEmpty(curCodeInfo) && curCode != -1) ? curWeather.getDescription() : curCodeInfo;
        cityWeather.setCurCodeInfo(curCodeInfo);

        cityWeather.setCurTemp(curTemp == erroe_temp ? NumberUtils.toInt(tempDay) : curTemp);

        cityWeather.setWindLevel(NumberUtils.toInt(windLevel.replace("级", "")));
        cityWeather.setWindDirection(windDirection);
        cityWeather.setWindLevelInfo(windLevel);

        WeatherState weatherState = WeatherState.getObject(codeImage.split(" ").length > 1 ? codeImage.split(" ")[1] : codeImage);
        int yahooCode = weatherState != null ? NumberUtils.toInt(weatherState.getYahooCode(), -1) : -1;
        cityWeather.setCode(yahooCode);
        codeInfo = (StringUtility.isEmpty(codeInfo) && yahooCode != -1) ? weatherState.getDescription() : codeInfo;
        cityWeather.setCodeInfo(codeInfo);

        WeatherState nWeather = WeatherState.getObject(nightCodeImage.split(" ").length > 1 ? nightCodeImage.split(" ")[1] : nightCodeImage);
        int nCode = nWeather != null ? NumberUtils.toInt(nWeather.getYahooCode(), -1) : -1;
        cityWeather.setNightCode(nCode);
        cityWeather.setNightCodeInfo(nightCodeInfo);

        //        cityWeather.setSunriseTime(sunriseTime);
        //        cityWeather.setSunsetTime(sunsetTime);
        if (time.matches("^[0-9]*$")) {
            boolean ut = false;
            try {
                cityWeather.setUnixTime(DateUtility.parseUnixTime(DateUtility.format(new Date(), "yyyy-MM-dd") + " " + time + ":00"));
                ut = true;
            } catch (Exception e) {
            } finally {
                if (!ut) {
                    cityWeather.setUnixTime(DateUtility.parseUnixTime(DateUtility.format(new Date(), "yyyy-MM-dd HH:00:00")));
                }
            }
        } else {
            cityWeather.setUnixTime(DateUtility.parseUnixTime(DateUtility.format(new Date(), "yyyy-MM-dd HH:00:00")));
        }
    }

    // 预警类型 
    // 01 台风预警, 02 暴雨预警, 03 暴雪预警, 04 寒潮预警, 05 大风预警, 06 沙尘暴预警, 07 高温预警,
    // 08 干旱预警, 09 雷电预警, 10 冰雹预警, 11 霜冻预警, 12 大雾预警, 13 霾预警, 14 道路结冰预警  
    // 预警级别 
    // 01 蓝色, 02 黄色, 03 橙色, 04 红色
    private void weatherAlarms(String dataInfo) {
        int weatherCityId = cityWeather.getWeatherCityId();
        if (StringUtility.isEmpty(dataInfo)) {
            return;
        }
        String alarmDZ = RegexUtility.findGroup(dataInfo, "alarmDZ" + weatherCityId + "=\\{.+?\\}\\]\\}", 0);
        if (StringUtility.isEmpty(alarmDZ)) {
            return;
        }
        alarmDZ = alarmDZ.replaceAll("&quot;", "\"").replace("alarmDZ" + weatherCityId + "=", "");
        JSONObject alarmDZJson = JSONObject.parseObject(alarmDZ);

        JSONArray alarms = new JSONArray();
        JSONArray wArr = alarmDZJson.getJSONArray("w");
        for (int i = 0; i < wArr.size(); i++) {
            JSONObject w = wArr.getJSONObject(i);
            String type = w.getString("w4");
            String typeInfo = w.getString("w5");
            String level = w.getString("w6");
            String levelInfo = w.getString("w7");
            String publicDate = w.getString("w8"); //2014-12-0410:24
            String title = w.getString("w9");

            JSONObject res = new JSONObject();
            res.put("type", type);
            res.put("typeInfo", typeInfo);
            res.put("level", level);
            res.put("levelInfo", levelInfo);
            res.put("publicDate", DateUtility.parseUnixTime(publicDate, "yyyy-MM-ddHH:mm"));
            res.put("title", title);
            alarms.add(res);
        }
        String extInfo = cityWeather.getExtInfo();
        JSONObject result = new JSONObject();
        if (StringUtility.isNotEmpty(extInfo)) {
            try {
                result = JSONObject.parseObject(extInfo);
            } catch (Exception e) {
            }
        }
        result.put("alarms", alarms);
        cityWeather.setExtInfo(result.toString());
    }

    // "od21": "09",   时间
    // "od22": "28",   温度
    // "od23": "181",  角度
    // "od24": "南风",   风向
    // "od25": "2",    风力
    // "od26": "0",    降雨
    // "od27": "66"    湿度
    private void hourInfo() {
        try {
            JSONObject result = new JSONObject();
            JSONArray hourForcasts = new JSONArray();

            String html = docData.select("#hour").html();
            String info = RegexUtility.findGroup(html, "(\\{).*?(\\};)", 0).replace(";", "");
            JSONObject json = JSONObject.parseObject(info.replace(";", ""));
            String publicDate = json.getJSONObject("od").getString("od0"); // 20140729090000
            boolean isToday = true;
            JSONArray hours = json.getJSONObject("od").getJSONArray("od2");
            for (int i = hours.size() - 1; i >= 0; i--) {
                JSONObject hourJson = new JSONObject();
                JSONObject hour = hours.getJSONObject(i);
                int time = hour.getIntValue("od21");
                int temp = hour.getIntValue("od22");
                // int angle = hour.getInt("od23"); //风向角度
                String windDirection = hour.getString("od24");
                int windLevel = hour.getIntValue("od25");
                int jiangYu = hour.getIntValue("od26");
                int humidity = hour.getIntValue("od27");

                // //////////--时间Start--///////////
                if (time == 0) {
                    isToday = false;
                }
                Calendar c = Calendar.getInstance();
                c.set(Calendar.SECOND, 0);
                c.set(Calendar.MINUTE, 0);
                String date = null;
                if (isToday) {
                    c.set(Calendar.HOUR_OF_DAY, time);
                    date = DateUtility.format(c, "yyyy-MM-dd HH:mm:ss");
                } else {
                    c.add(Calendar.DAY_OF_MONTH, 1);
                    c.set(Calendar.HOUR_OF_DAY, time);
                    date = DateUtility.format(c, "yyyy-MM-dd HH:mm:ss");
                }
                hourJson.put("date", date);
                // //////////--时间End--///////////
                hourJson.put("temp", temp);// 温度
                hourJson.put("windDirection", windDirection);
                hourJson.put("windLevel", windLevel);
                hourJson.put("windLevelInfo", windLevel + "级");
                hourJson.put("precipitation", jiangYu);// 降雨
                hourJson.put("humidity", humidity);// 湿度
                if (isToday && time <= 20) { // 白天
                    hourJson.put("code", cityWeather.getCode());
                    hourJson.put("codeInfo", cityWeather.getCodeInfo());
                } else { // 夜间
                    hourJson.put("code", cityWeather.getNightCode());
                    hourJson.put("codeInfo", cityWeather.getNightCodeInfo());
                }
                hourForcasts.add(hourJson);
            }
            result.put("publicDate", StringUtility.isNotEmpty(publicDate) ? DateUtility.format(DateUtility.parseDate(publicDate, "yyyyMMddHHmmss")) : DateUtility.format(new Date()));
            result.put("hourForcasts", hourForcasts);
            cityWeather.setHourForcast(result.toString());
        } catch (Exception e) {
            JSONObject result = new JSONObject();
            result.put("publicDate", DateUtility.format(new Date()));
            result.put("hourForcasts", new JSONArray());
            cityWeather.setHourForcast(result.toString());
        }
    }

    private void weekInfo() {
        int weatherCityId = cityWeather.getWeatherCityId();
        String html = HttpUtility.get("http://www.weather.com.cn/weather/" + weatherCityId + ".shtml", null, "UTF-8", 15000);
        if (StringUtility.isEmpty(html) || html.equals("<!-- empty -->")) {
            exceptionList.add(cityWeather.getId() + " > " + weatherCityId + " 无数据！");
            return;
        }

        Document docData = Jsoup.parse(html);

        JSONObject result = new JSONObject();
        //要改为取天气网
        result.put("publicDate", DateUtility.format((int) (cityWeather.getUnixTime()), "yyyy-MM-dd HH:mm:ss"));
        JSONArray dayForcasts = new JSONArray();
        Elements eles = docData.select("div[id=7d] ul").get(0).select("li");

        //为了兼容客户端，去掉今天的数据
        for (int i = 0; i < eles.size(); i++) {
            JSONObject day = new JSONObject();
            Element ele = eles.get(i);
            String date = ele.getElementsByTag("h1").text();
            date = RegexUtility.findGroup(date, "([0-9]+)", 1);

            String dCodeImage = ele.getElementsByTag("big").get(0).attr("class");
            String nCodeImage = ele.getElementsByTag("big").get(1).attr("class");
            String[] codeInfo = ele.getElementsByTag("p").select("[class=wea]").text().split("转");

            String highTemp = ele.select("p[class=tem] span").text();
            String lowTemp = ele.select("p[class=tem] i").text();
            String windDirection = ele.getElementsByTag("p").select("[class=win]").select("em span").get(0).attr("title");
            String windLevelInfo = ele.getElementsByTag("p").select("[class=win]").select("i").text();
            String dWindLevelInfo = windLevelInfo.contains("转") ? windLevelInfo.split("转")[0] : windLevelInfo;
            String nWindLevelInfo = windLevelInfo.contains("转") ? windLevelInfo.split("转")[1] : windLevelInfo;

            Calendar c = Calendar.getInstance();
            int nowMonthDay = c.get(Calendar.DAY_OF_MONTH);
            if (nowMonthDay > Integer.parseInt(date)) {
                c.add(Calendar.MONTH, 1);
            }
            c.set(Calendar.DAY_OF_MONTH, Integer.parseInt(date));
            day.put("startDate", DateUtility.format(c, "yyyy-MM-dd"));
            day.put("entDate", DateUtility.format(c, "yyyy-MM-dd"));

            day.put("highTemp", StringUtility.isNotEmpty(highTemp) ? highTemp.replace("℃", "") : highTemp); //高温
            day.put("lowTemp", StringUtility.isNotEmpty(lowTemp) ? lowTemp.replace("℃", "") : lowTemp); //低温
            day.put("windDirection", windDirection); //风向
            day.put("windLevel", getWindLevelByInfo(dWindLevelInfo)); //风力
            day.put("windLevelInfo", dWindLevelInfo); //风力
            WeatherState weather = WeatherState.getObject(dCodeImage.split(" ").length > 1 ? dCodeImage.split(" ")[1] : dCodeImage);
            int code = weather != null ? NumberUtils.toInt(weather.getYahooCode(), -1) : -1;
            WeatherState nWeather = WeatherState.getObject(nCodeImage.split(" ").length > 1 ? nCodeImage.split(" ")[1] : nCodeImage);
            int nCode = nWeather != null ? NumberUtils.toInt(nWeather.getYahooCode(), -1) : -1;
            code = code == -1 ? nCode : code;
            nCode = nCode == -1 ? code : nCode;

            day.put("code", code); //天气状态
            day.put("codeInfo", codeInfo[0]); //天气状态

            day.put("windDirectionNight", windDirection);
            day.put("windLevelNight", getWindLevelByInfo(nWindLevelInfo));
            day.put("windLevelInfoNight", nWindLevelInfo);
            day.put("codeNight", nCode);
            day.put("codeInfoNight", codeInfo.length > 1 ? codeInfo[1] : codeInfo[0]);

            dayForcasts.add(day);
        }
        result.put("dayForcasts", dayForcasts);
        cityWeather.setDayForcast(result.toString());
    }

    private void liveInfo() {
        JSONObject result = new JSONObject();
        Elements eles = docData.select("div[class=zs] ul li");
        Elements eles_ = docData.select("div[class=zs yinc] ul li");
        eles.addAll(eles_);
        for (int i = 0; i < eles.size(); i++) {
            Element ele = eles.get(i);
            String name = ele.select("section[class=imgArea] a").attr("title");
            String level = ele.select("section[class=detail] aside b").text();
            String dict = ele.select("section[class=detail] aside:not(b)").text();
            dict = StringUtility.isNotEmpty(dict) ? dict.replace(level, "") : dict;

            JSONObject json = new JSONObject();
            json.put("name", name);
            json.put("level", level);
            json.put("dict", dict);
            if (liveMap.containsKey(name)) {
                result.put(liveMap.get(name), json);
            }
        }
        cityWeather.setLiveInfo(result.toString());
    }

    private static int getWindLevelByInfo(String info) {
        info = info.contains("转") ? info.split("转")[0] : info;
        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("微风", 2);
        map.put("3-4级", 3);
        map.put("4-5级", 4);
        map.put("5-6级", 5);
        map.put("6-7级", 6);
        map.put("7-8级", 7);
        map.put("8-9级", 8);
        map.put("9-10级", 9);
        map.put("10-11级", 10);
        map.put("11-12级", 11);
        return map.containsKey(info) ? map.get(info) : -1;
    }

    private static Map<String, String> liveMap = new HashMap<String, String>();

    static {
        liveMap.put("太阳镜指数", "zs_tyj");
        liveMap.put("洗车指数", "zs_xc");
        liveMap.put("穿衣指数", "zs_cy");
        liveMap.put("中暑指数", "zs_zs");
        liveMap.put("污染扩散指数", "zs_kqwr");
        liveMap.put("逛街指数", "zs_gj");
        liveMap.put("心情指数", "zs_xq");
        liveMap.put("感冒指数", "zs_gm");
        liveMap.put("晨练指数", "zs_cl");
        liveMap.put("夜生活指数", "zs_ysh");
        liveMap.put("空调指数", "zs_kt");
        liveMap.put("钓鱼指数", "zs_dy");
        liveMap.put("美发指数", "zs_mf");
        liveMap.put("运动指数", "zs_yd");
        liveMap.put("辐射指数", "zs_fs");
        liveMap.put("约会指数", "zs_yh");
        liveMap.put("紫外线指数", "zs_zwx");
        liveMap.put("雨伞指数", "zs_ys");
        liveMap.put("防寒指数", "zs_fh");
        liveMap.put("旅游指数", "zs_ly");
        liveMap.put("放风筝指数", "zs_ffz");
        liveMap.put("晾晒指数", "zs_ls");
        liveMap.put("交通指数", "zs_jt");
        liveMap.put("化妆指数", "zs_hz");
        liveMap.put("划船指数", "zs_hc");
        liveMap.put("舒适度指数", "zs_ssd");
        liveMap.put("过敏指数", "zs_gmz");
        liveMap.put("路况指数", "zs_lk");
        liveMap.put("防晒指数", "zs_fsz");
    }

    public static void main(String[] args) {
        CityWeatherBean cityWeather = new CityWeatherBean();
        //                cityWeather.setWeatherCityId(101320101);//香港
        cityWeather.setWeatherCityId(101010100);//北京
        //                cityWeather.setWeatherCityId(101120710);
        new ParsorChinaWeatherData2(cityWeather).parse();

        System.out.println(JSONObject.toJSONString(cityWeather));
    }

    public static void main3(String[] args) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("city", HttpUtility.urlEncode("朝阳区", "UTF-8"));
        //                        params.put("cityid", "101010100");
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("apikey", "04c36c547c9fa053d178d880a47b343e");
        String res = HttpUtility.get("http://apis.baidu.com/apistore/weatherservice/citylist", params, headers, "UTF-8", 0);
        try {
            System.out.println(new String(res.getBytes(), "utf8"));
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        //        for (int i = 0; i < 10; i++) {
        //            final int m = i;
        //            ThreadManager.getInstance().execute(new Runnable() {
        //                @Override
        //                public void run() {
        //                    for (int j = 0; j < 1100; j++) {
        //                        Map<String, String> params = new HashMap<String, String>();
        //                        params.put("city", HttpUtility.urlEncode("朝阳区", "UTF-8"));
        //                        //                        params.put("cityid", "101010100");
        //                        Map<String, String> headers = new HashMap<String, String>();
        //                        headers.put("apikey", "04c36c547c9fa053d178d880a47b343e");
        //                        String res = HttpUtility.get("http://apis.baidu.com/apistore/weatherservice/citylist", params, headers, "UTF-8", 0);
        //                        try {
        //                            System.out.println(m + " > " + j + " > " + new String(res.getBytes(), "utf8"));
        //                        } catch (UnsupportedEncodingException e) {
        //                            // TODO Auto-generated catch block
        //                            e.printStackTrace();
        //                        }
        //                    }
        //                }
        //            });
        //        }

    }

}
