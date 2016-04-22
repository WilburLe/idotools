package com.toolbox.weather.enums;

/**
 * 
 * @version
 * @since Mar 22, 2013
 * 
 */
public class GlobalWeather {
    public static String[] wp = { "微风", "微风", "3-4级", "4-5级", "5-6级", "6-7级", "7-8级", "8-9级", "9-10级", "10-11级", "11-12级" };

    public static int getWindLevel(int kph) {
        int level = 0;
        if (kph < 1) {
            level = 0;
        } else if (kph >= 1 && kph <= 5) {
            level = 1;
        } else if (kph >= 6 && kph <= 11) {
            level = 2;
        } else if (kph >= 12 && kph <= 19) {
            level = 3;
        } else if (kph >= 20 && kph <= 28) {
            level = 4;
        } else if (kph >= 29 && kph <= 38) {
            level = 5;
        } else if (kph >= 39 && kph <= 49) {
            level = 6;
        } else if (kph >= 50 && kph <= 61) {
            level = 7;
        } else if (kph >= 62 && kph <= 74) {
            level = 8;
        } else if (kph >= 75 && kph <= 88) {
            level = 9;
        } else if (kph >= 89 && kph <= 102) {
            level = 10;
        } else if (kph >= 103 && kph <= 117) {
            level = 11;
        } else if (kph > 117) {
            level = 12;
        }
        return level;
    }

    public static int getCode(String icon) {
        int code = 32;
        if (icon.equals("chanceflurries")) {
            code = 13;
        } else if (icon.equals("chancerain")) {
            code = 11;
        } else if (icon.equals("chancesleet")) {
            code = 13;
        } else if (icon.equals("chancesnow")) {
            code = 13;
        } else if (icon.equals("chancetstorms")) {
            code = 3;
        } else if (icon.equals("clear")) {
            code = 32;
        } else if (icon.equals("cloudy")) {
            code = 30;
        } else if (icon.equals("flurries")) {
            code = 16;
        } else if (icon.equals("fog")) {
            code = 20;
        } else if (icon.equals("hazy")) {
            code = 21;
        } else if (icon.equals("mostlycloudy")) {
            code = 30;
        } else if (icon.equals("mostlysunny")) {
            code = 32;
        } else if (icon.equals("partlycloudy")) {
            code = 30;
        } else if (icon.equals("partlysunny")) {
            code = 32;
        } else if (icon.equals("sleet")) {
            code = 18;
        } else if (icon.equals("rain")) {
            code = 9;
        } else if (icon.equals("snow")) {
            code = 16;
        } else if (icon.equals("sunny")) {
            code = 32;
        } else if (icon.equals("tstorms")) {
            code = 3;
        }//
        else if (icon.equals("nt_chanceflurries")) {
            code = 13;
        } else if (icon.equals("nt_chancerain")) {
            code = 11;
        } else if (icon.equals("nt_chancesleet")) {
            code = 13;
        } else if (icon.equals("nt_chancesnow")) {
            code = 13;
        } else if (icon.equals("nt_chancetstorms")) {
            code = 3;
        } else if (icon.equals("nt_clear")) {
            code = 31;
        } else if (icon.equals("nt_cloudy")) {
            code = 29;
        } else if (icon.equals("nt_flurries")) {
            code = 16;
        } else if (icon.equals("nt_fog")) {
            code = 20;
        } else if (icon.equals("nt_hazy")) {
            code = 21;
        } else if (icon.equals("nt_mostlycloudy")) {
            code = 29;
        } else if (icon.equals("nt_mostlysunny")) {
            code = 31;
        } else if (icon.equals("nt_partlycloudy")) {
            code = 29;
        } else if (icon.equals("nt_partlysunny")) {
            code = 31;
        } else if (icon.equals("nt_sleet")) {
            code = 18;
        } else if (icon.equals("nt_rain")) {
            code = 9;
        } else if (icon.equals("nt_snow")) {
            code = 16;
        } else if (icon.equals("nt_sunny")) {
            code = 31;
        } else if (icon.equals("nt_tstorms")) {
            code = 3;
        }

        return code;
    }
}
