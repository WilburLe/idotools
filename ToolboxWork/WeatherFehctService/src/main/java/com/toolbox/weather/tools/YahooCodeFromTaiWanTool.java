package com.toolbox.weather.tools;

import java.util.HashMap;
import java.util.Map;

public class YahooCodeFromTaiWanTool {
    private static Map<String, Integer> map = new HashMap<String, Integer>();

    static {
        map.put("多雲", 29);
        map.put("多雲時陰", 27);
        map.put("多雲時晴", 29);
        map.put("多雲有霾", 21);
        map.put("多雲有靄", 21);
        map.put("多雲小雨", 9);
        map.put("多雲有雨", 11);
        map.put("多雲有閃電", 44);
        map.put("多雲有雷雨", 45);
        map.put("多雲有雷聲", 44);
        map.put("多雲有陣雨", 11);
        map.put("多雲短暫雨", 11);
        map.put("多雲短暫陣雨", 11);
        map.put("多雲時陰短暫雨", 12);
        map.put("多雲時陰短暫雨", 12);
        map.put("多雲午後短暫陣雨", 12);
        map.put("多雲時陰短暫陣雨", 11);
        map.put("多雲午後短暫雷陣雨", 45);
        map.put("多雲短暫陣雨或雷雨", 45);
        map.put("多雲時陰陣雨或雷雨", 45);
        map.put("多雲時陰短暫陣雨或雷雨", 45);

        map.put("晴", 32);
        map.put("晴天", 32);
        map.put("晴有靄", 21);
        map.put("晴有霾", 21);
        map.put("晴有霧", 20);
        map.put("晴有閃電", 32);
        map.put("晴有雷聲", 32);
        map.put("晴時多雲", 32);
        map.put("晴午後短暫雷陣雨", 45);

        map.put("陰", 26);
        map.put("陰天", 26);
        map.put("陰有霧", 20);
        map.put("陰有雨", 11);
        map.put("陰有靄", 21);
        map.put("陰有霾", 21);
        map.put("陰小雨", 9);
        map.put("陰短暫雨", 11);
        map.put("陰大雷雨", 3);
        map.put("陰有雷雨", 4);
        map.put("陰有雷聲", 26);
        map.put("陰陣雨", 11);
        map.put("陰有陣雨", 11);
        map.put("陰有大雨", 12);
        map.put("陰有閃電", 26);
        map.put("陰時多雲", 26);
        map.put("陰時多雲", 26);
        map.put("陰有閃電", 26);
        map.put("陰短暫陣雨", 11);
        map.put("陰時多雲陣雨", 12);
        map.put("多雲時陰陣雨", 11);
        map.put("陰陣雨或雷雨", 45);
        map.put("陰時多雲短暫雨", 11);
        map.put("陰時多雲短暫雨", 11);
        map.put("陰短暫陣雨或雷雨", 45);
        map.put("陰時多雲短暫陣雨", 11);
        map.put("陰時多雲短暫陣雨或雷雨", 45);
        map.put("陰時多雲陣雨或雷雨", 45);

        map.put("有霧", 20);
        map.put("多雲有霧", 20);
        map.put("有靄", 20);
        map.put("有霾", 21);
        map.put("有雨", 11);
        map.put("有陣雨", 11);
        map.put("有雷雨", 4);

        map.put("小雨", 9);
    }

    public static int getYahooCode(String twWeather) {
        if (map.containsKey(twWeather)) return map.get(twWeather);
        else {
            return -1;
        }
    }

    public static void main(String[] args) {
        System.out.println(getYahooCode("多雲短暫雨"));
    }
}
