package com.toolbox.weather.tools;

import java.util.Date;
import java.util.Locale;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toolbox.framework.utils.DateUtility;
import com.toolbox.weather.bean.CityWeatherBean;


public class YahooWeatherTool  {

    private static Log log = LogFactory.getLog(YahooWeatherTool.class);
    
    public static int code_0    = 0;   // tornado
    public static int code_1    = 1;   // tropical storm
    public static int code_2    = 2;   // hurricane
    public static int code_3    = 3;   // severe thunderstorms
    public static int code_4    = 4;   // thunderstorms
    public static int code_5    = 5;   // mixed rain and snow
    public static int code_6    = 6;   // mixed rain and sleet
    public static int code_7    = 7;   // mixed snow and sleet
    public static int code_8    = 8;   // freezing drizzle
    public static int code_9    = 9;   // drizzle
    public static int code_10   = 10;  // freezing rain
    public static int code_11   = 11;  // showers
    public static int code_12   = 12;  // showers
    public static int code_13   = 13;  // snow flurries
    public static int code_14   = 14;  // light snow showers
    public static int code_15   = 15;  // blowing snow
    public static int code_16   = 16;  // snow
    public static int code_17   = 17;  // hail
    public static int code_18   = 18;  // sleet
    public static int code_19   = 19;  // dust
    public static int code_20   = 20;  // foggy
    public static int code_21   = 21;  // haze
    public static int code_22   = 22;  // smoky
    public static int code_23   = 23;  // blustery
    public static int code_24   = 24;  // windy
    public static int code_25   = 25;  // cold
    public static int code_26   = 26;  // cloudy
    public static int code_27   = 27;  // mostly cloudy (night)
    public static int code_28   = 28;  // mostly cloudy (day)
    public static int code_29   = 29;  // partly cloudy (night)
    public static int code_30   = 30;  // partly cloudy (day)
    public static int code_31   = 31;  // clear (night)
    public static int code_32   = 32;  // sunny
    public static int code_33   = 33;  // fair (night)
    public static int code_34   = 34;  // fair (day)
    public static int code_35   = 35;  // mixed rain and hail
    public static int code_36   = 36;  // hot
    public static int code_37   = 37;  // isolated thunderstorms
    public static int code_38   = 38;  // scattered thunderstorms
    public static int code_39   = 39;  // scattered thunderstorms
    public static int code_40   = 40;  // scattered showers
    public static int code_41   = 41;  // heavy snow
    public static int code_42   = 42;  // scattered snow showers
    public static int code_43   = 43;  // heavy snow
    public static int code_44   = 44;  // partly cloudy
    public static int code_45   = 45;  // thundershowers
    public static int code_46   = 46;  // snow showers
    public static int code_47   = 47;  // isolated thundershowers
    public static int code_3200 = 3200; //   not available

    //    private static String appId     = "OId0n34o";

    private static String request(String url) {
        HttpClient client = new HttpClient();
        GetMethod method = null;
        try {
            method = new GetMethod(url);
            client.executeMethod(method);
            int code = method.getStatusCode();
            if (HttpStatus.SC_OK == code) {
                return method.getResponseBodyAsString();
            }
        } catch (Exception e) {
            log.error(e);
        } finally {
            if (method != null) {
                method.releaseConnection();
            }
        }
        return null;
    }

    private static String getWoeid(double latitude, double longitude) {
        String url = "http://query.yahooapis.com/v1/public/yql?q=%20select%20*%20from%20geo.placefinder%20where%20text%3D%22" + latitude + "%2C" + longitude + "%22%20and%20gflags%3D%22R%22&format=json";
        String response = request(url);
        if (response == null) {
            return null;
        }
        JSONObject json = JSONObject.parseObject(response);
        JSONObject query = json.getJSONObject("query");
        JSONObject results = query.getJSONObject("results");
        JSONObject result = results.getJSONObject("Result");
        return result.getString("woeid");
    }

    private static CityWeatherBean getWeather(String woeid) {
        String url = "http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20weather.forecast%20where%20woeid%3D" + woeid + "&format=json";
        String response = request(url);
        System.out.println(response);
        if (response == null) {
            return null;
        }
        JSONObject json = JSONObject.parseObject(response);
        JSONObject query = json.getJSONObject("query");
        JSONObject results = query.getJSONObject("results");
        JSONObject channel = results.getJSONObject("channel");
        JSONObject item = channel.getJSONObject("item");
        JSONArray forecasts = item.getJSONArray("forecast");
        if (!item.containsKey("forecast")) {
            return null;
        }
        CityWeatherBean weatherBean = new CityWeatherBean();
        if (forecasts.size() > 0) {
            JSONObject forecast = forecasts.getJSONObject(0);
            Date date = DateUtility.parseDate(forecast.getString("date"), Locale.ENGLISH, "dd MMM yyyy");//dd Mmm yyyy
            String low = forecast.getString("low");
            String high = forecast.getString("high");
            String code = forecast.getString("code");
            weatherBean.setCode(NumberUtils.toInt(code));
            weatherBean.setLow(f2c(NumberUtils.toInt(low)));
            weatherBean.setHigh(f2c(NumberUtils.toInt(high)));
            weatherBean.setCode(NumberUtils.toInt(code));
            weatherBean.setUnixTime((int) (date.getTime() / 1000));
        }
        JSONObject condition = item.getJSONObject("condition");
        String code = condition.getString("code");
        String temp = condition.getString("temp");
        weatherBean.setCurCode(NumberUtils.toInt(code));
        weatherBean.setCurTemp(f2c(NumberUtils.toInt(temp)));
        return weatherBean;
    }

    private static int f2c(int f) {
        return (f - 32) * 5 / 9;
    }

    public static CityWeatherBean getWeather(double longitude, double latitude) {
        return getWeather(getWoeid(longitude, latitude));
    }

}
