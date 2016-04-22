/*
 * Copyright (c) 2010-2013 www.pixshow.net All Rights Reserved
 *
 * File:QqWithYahooCode.java Project: LvWeatherService
 * 
 * Creator:4399-lvtu-8 
 * Date:Dec 3, 2013 12:13:46 PM
 * 
 */
package com.toolbox.weather.enums;

/**
 * 
 * 
 * 
 * @author 4399-lvtu-8
 * @author $Author:$
 * @version $Revision:$ $Date:$ 
 * @since Dec 3, 2013
 * 
 */

public enum QqWithYahooCode {
    a0("0", 32, "晴", ""), //
    a1("1", 30, "多云", ""), //
    a2("2", 26, "阴", ""), //
    a3("3", 11, "阵雨", ""), //
    a4("4", 4, "雷阵雨", ""), //
    a5("5", 35, "雷阵雨并伴有冰雹", ""), //
    a6("6", 18, "雨夹雪", ""), //
    a7("7", 9, "小雨", ""), //
    a8("8", 9, "中雨", ""), //
    a9("9", 12, "大雨", ""), //
    a10("10", 3, "暴雨", ""), //
    a11("11", 3, "大暴雪", ""), //
    a12("12", 3, "特大暴雪", ""), //
    a13("13", 13, "阵雪", ""), //
    a14("14", 16, "小雪", ""), //
    a15("15", 16, "中雪", ""), //
    a16("16", 41, "大雪", ""), //
    a17("17", 41, "暴雪", ""), //
    a18("18", 20, "雾", ""), //
    a19("19", 10, "冻雨", ""), //
    a20("20", 19, "沙尘暴", ""), //
    a21("21", 9, "小雨-中雨", ""), //
    a22("22", 12, "中雨-大雨", ""), //
    a23("23", 3, "大雨-暴雨", ""), //
    a24("24", 3, "暴雨-大暴雨", ""), //
    a25("25", 3, "大暴雨-特大暴雨", ""), //
    a26("26", 16, "小雪-中雪", ""), //
    a27("27", 41, "中雪-大雪", ""), //
    a28("28", 43, "大雪-暴雪", ""), //
    a29("29", 19, "浮尘", ""), //
    a30("30", 19, "扬沙", ""), //
    a31("31", 19, "强沙尘暴", ""), //
    a32("32", 0, "飑", ""), //
    a33("33", 0, "龙卷风", ""), //
    a34("34", 0, "弱高吹雪", ""), //
    a35("35", 0, "轻雾", ""), //
    a36("36", 0, "", ""), //
    a37("37", 0, "", ""), //
    a38("38", 0, "", ""), //
    a39("39", 0, "", ""), //
    a40("40", 0, "", ""), //
    a41("41", 0, "", ""), //
    a42("42", 0, "", ""), //
    a43("43", 0, "", ""), //
    a44("44", 0, "", ""), //
    a45("45", 0, "", ""), //
    a46("46", 0, "", ""), //
    a47("47", 0, "", ""), //
    a48("48", 0, "", ""), //
    a49("49", 0, "", ""), //
    a50("50", 0, "", ""), //
    a51("51", 0, "", ""), //
    a52("52", 0, "", ""), //
    a53("53", 21, "霾", "");

    String qqCode;
    int    yahooCode;
    String description;
    String endescription;

    QqWithYahooCode(String qqCode, int yahooCode, String description, String endescription) {
        this.qqCode = qqCode;
        this.yahooCode = yahooCode;
        this.description = description;
        this.endescription = endescription;
    }

    public String getQqCode() {
        return qqCode;
    }

    public void setQqCode(String qqCode) {
        this.qqCode = qqCode;
    }

    public int getYahooCode() {
        return yahooCode;
    }

    public void setYahooCode(int yahooCode) {
        this.yahooCode = yahooCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEndescription() {
        return endescription;
    }

    public void setEndescription(String endescription) {
        this.endescription = endescription;
    }

    public static QqWithYahooCode getObject(String str) {
        for (QqWithYahooCode qq : QqWithYahooCode.values()) {
            if (qq.name().equals(str)) {
                return qq;
            }
        }
        return null;
    }
}
