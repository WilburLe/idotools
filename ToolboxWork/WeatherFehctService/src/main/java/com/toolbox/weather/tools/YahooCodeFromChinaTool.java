package com.toolbox.weather.tools;

import java.util.HashMap;
import java.util.Map;

public class YahooCodeFromChinaTool {
    private static Map<String, Integer> codeMap = new HashMap<String, Integer>();
    static {
        codeMap.put("白天晴", 32);
        codeMap.put("白天晴间多云，有轻度霾", 32);
        codeMap.put("白天多云", 30);
        codeMap.put("白天晴间多云", 30);
        codeMap.put("白天多云间晴", 30);
        codeMap.put("白天多云间阴", 30);
        codeMap.put("白天多云间阴", 30);
        codeMap.put("白天多云转晴", 30);
        codeMap.put("白天晴转多云", 30);
        codeMap.put("白天多云转晴早晨有轻雾 ", 30);
        codeMap.put("白天阴", 26);
        codeMap.put("白天阴转晴", 26);
        codeMap.put("白天多云转阴", 26);
        codeMap.put("白天阴天间多云", 26);
        codeMap.put("白天多云转阴，有霾", 26);
        codeMap.put("白天多云转阴，北部有阵雨", 26);
        codeMap.put("白天多云转阴，有零星小雨夹雪", 26);
        codeMap.put("白天阵雨", 11);
        codeMap.put("白天雷阵雨", 4);
        codeMap.put("白天雷阵雨伴有冰雹", 35);
        codeMap.put("白天雨夹雪", 18);
        codeMap.put("白天小雨", 9);
        codeMap.put("白天霾转小雨", 9);
        codeMap.put("白天多云转阴下午有小雨", 9);
        codeMap.put("白天多云转阴有小雨", 9);
        codeMap.put("白天中雨", 9);
        codeMap.put("白天大雨", 12);
        codeMap.put("白天暴雨", 3);
        codeMap.put("白天大暴雨", 3);
        codeMap.put("白天特大暴雨", 3);
        codeMap.put("白天阵雪", 13);
        codeMap.put("白天小雪", 16);
        codeMap.put("白天中雪", 16);
        codeMap.put("白天大雪", 41);
        codeMap.put("白天暴雪", 41);
        codeMap.put("白天雾", 20);
        codeMap.put("白天冻雨", 10);
        codeMap.put("白天沙尘暴", 19);
        codeMap.put("白天小到中雨", 9);
        codeMap.put("白天中到大雨", 12);
        codeMap.put("白天大到暴雨", 3);
        codeMap.put("白天暴雨到大暴雨", 3);
        codeMap.put("白天大暴雨到特大暴雨", 3);
        codeMap.put("白天小到中雪", 16);
        codeMap.put("白天中到大雪", 41);
        codeMap.put("白天大到暴雪", 43);
        codeMap.put("白天浮尘", 19);
        codeMap.put("白天扬沙", 19);
        codeMap.put("白天强沙尘暴", 19);
        codeMap.put("白天霾", 21);
        codeMap.put("白天多云，有霾", 21);
        codeMap.put("白天多云间晴，有霾", 21);
        codeMap.put("白天阴转多云，有霾", 21);
        codeMap.put("白天多云，有霾；早晨有雾", 21);

        codeMap.put("夜间晴", 31);
        codeMap.put("夜间多云", 29);
        codeMap.put("夜间多云转阴", 29);
        codeMap.put("夜间多云转晴", 29);
        codeMap.put("夜间多云间阴", 29);
        codeMap.put("夜间多云间晴", 29);
        codeMap.put("夜间晴间多云", 29);
        codeMap.put("夜间晴转多云", 29);
        codeMap.put("夜间晴转多云，有轻雾", 29);
        codeMap.put("夜间晴转多云，有霾 ", 29);
        codeMap.put("夜间晴间多云，有轻度霾", 29);
        codeMap.put("夜间阴", 27);
        codeMap.put("夜间阴转多云", 27);
        codeMap.put("夜间阴转晴", 27);
        codeMap.put("夜间阴有轻雾转多云", 27);
        codeMap.put("夜间阴，东部地区有小雨转晴", 27);
        codeMap.put("夜间阴，有雾", 27);
        codeMap.put("夜间多云转阴 ", 27);
        codeMap.put("夜间阴天间多云", 27);
        codeMap.put("夜间阵雨", 11);
        codeMap.put("夜间雷阵雨", 45);
        codeMap.put("夜间雷阵雨伴有冰雹", 35);
        codeMap.put("夜间雨夹雪", 18);
        codeMap.put("夜间阴东北部有小雨夹雪，转多云", 18);
        codeMap.put("夜间小雨", 9);
        codeMap.put("夜间小雨转晴", 9);
        codeMap.put("夜间中雨", 9);
        codeMap.put("夜间大雨", 12);
        codeMap.put("夜间暴雨", 3);
        codeMap.put("夜间大暴雨", 3);
        codeMap.put("夜间特大暴雨", 3);
        codeMap.put("夜间阵雪", 13);
        codeMap.put("夜间小雪", 16);
        codeMap.put("夜间阴、有小雪", 16);
        codeMap.put("夜间阴、有零星小雪", 16);
        codeMap.put("夜间中雪", 16);
        codeMap.put("夜间大雪", 41);
        codeMap.put("夜间暴雪", 41);
        codeMap.put("夜间雾", 20);
        codeMap.put("夜间多云间阴，有雾", 20);
        codeMap.put("夜间多云、有雾 ", 20);
        codeMap.put("夜间多云，有雾", 20);
        codeMap.put("夜间冻雨", 10);
        codeMap.put("夜间沙尘暴", 19);
        codeMap.put("夜间小到中雨", 9);
        codeMap.put("夜间中到大雨", 12);
        codeMap.put("夜间大到暴雨", 3);
        codeMap.put("夜间暴雨到大暴雨", 3);
        codeMap.put("夜间大暴雨到特大暴雨", 3);
        codeMap.put("夜间小到中雪", 16);
        codeMap.put("夜间中到大雪", 41);
        codeMap.put("夜间大到暴雪", 43);
        codeMap.put("夜间浮尘", 19);
        codeMap.put("夜间扬沙", 19);
        codeMap.put("夜间强沙尘暴", 19);
        codeMap.put("夜间霾", 21);
    }

    public static int getYahooCode4Title(String title) {
        try {
            return codeMap.get(title);
        } catch (Exception e) {
            return -1;
        }
    }

    public static int getTempYahooCode4Title(String title) {
        int code = -1;
        if (title.startsWith("白天")) {
            code = 32;
            if (title.contains("阴")) {
                code = 26;
            } else if (title.contains("雨")) {
                code = 9;
            } else if (title.contains("雪")) {
                code = 16;
            } else if (title.contains("雾")) {
                code = 20;
            }
        } else if (title.startsWith("夜间")) {
            code = 31;
            if (title.contains("阴")) {
                code = 27;
            } else if (title.contains("雨")) {
                code = 9;
            } else if (title.contains("雪")) {
                code = 16;
            } else if (title.contains("雾")) {
                code = 20;
            }
        }
        return code;
    }

    public static void main(String[] args) {
        System.out.println(YahooCodeFromChinaTool.getTempYahooCode4Title("白天晴天卡 "));
    }
}
