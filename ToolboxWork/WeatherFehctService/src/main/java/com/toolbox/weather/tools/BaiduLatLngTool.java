package com.toolbox.weather.tools;

import com.toolbox.framework.utils.HttpUtility;

public class BaiduLatLngTool {
    private static final String BAIDU_CITY_URL = "http://api.map.baidu.com/geocoder?location=%s,%s&output=json&key=b72307eceed5b86dd8870afbbc9b0745";

    public static String getBaiduCityInfo(double lat, double lng) {

        return HttpUtility.get(String.format(BAIDU_CITY_URL, lat, lng), null, "UTF-8");
    }

    public static void main(String[] args) {
        System.out.println(getBaiduCityInfo(39.88726,116.427002));
    }

}
