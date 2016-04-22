package com.toolbox.weather.enums;

/**
    * 中国天气网 天气定义(含与yahoo对应) 
    * @author yangjunshuai
    * @version 
    * @since Mar 22, 2013
    *
    */
public enum WeatherState {

    d0("d0", "32", "晴", "sunny"), //
    d1("d1", "30", "多云", "mostly cloudy (day)"), //
    d2("d2", "26", "阴", "cloudy"), //
    d3("d3", "11", "阵雨", "showers"), //
    d4("d4", "4", "雷阵雨", "thundershowers"), //
    d5("d5", "35", "雷阵雨伴有冰雹", "mixed rain and hail"), //
    d6("d6", "18", "雨夹雪", "sleet"), //
    d7("d7", "9", "小雨", "drizzle"), //
    d8("d8", "9", "中雨", "showers"), //
    d9("d9", "12", "大雨", "showers"), //
    d00("d00", "32", "晴", "sunny"), //
    d01("d01", "30", "多云", "mostly cloudy (day)"), //
    d02("d02", "26", "阴", "cloudy"), //
    d03("d03", "11", "阵雨", "showers"), //
    d04("d04", "4", "雷阵雨", "thundershowers"), //
    d05("d05", "35", "雷阵雨伴有冰雹", "mixed rain and hail"), //
    d06("d06", "18", "雨夹雪", "sleet"), //
    d07("d07", "9", "小雨", "drizzle"), //
    d08("d08", "9", "中雨", "showers"), //
    d09("d09", "12", "大雨", "showers"), //
    d10("d10", "3", "暴雨", "tropical storm"), //
    d11("d11", "3", "大暴雨", "tropical storm"), //
    d12("d12", "3", "特大暴雨", "tropical storm"), //
    d13("d13", "13", "阵雪", "snow flurries"), //
    d14("d14", "16", "小雪", "light snow showers"), //
    d15("d15", "16", "中雪", "snow"), //
    d16("d16", "41", "大雪", "heavy snow"), //
    d17("d17", "41", "暴雪", "heavy snow"), //
    d18("d18", "20", "雾", "foggy"), //
    d19("d19", "10", "冻雨", "freezing rain"), //
    d20("d20", "19", "沙尘暴", "dust"), //
    d21("d21", "9", "小雨-中雨", "showers"), //
    d22("d22", "12", "中雨-大雨", "showers"), //
    d23("d23", "3", "大雨-暴雨", "tropical storm"), //
    d24("d24", "3", "暴雨-大暴雨", "tropical storm"), //
    d25("d25", "3", "大暴雨-特大暴雨", "tropical storm"), //
    d26("d26", "16", "小雪-中雪", "snow"), //
    d27("d27", "41", "中雪-大雪", "heavy snow"), //
    d28("d28", "43", "大雪-暴雪", "heavy snow"), //
    d29("d29", "19", "浮尘", "dust"), //
    d30("d30", "19", "扬沙", "dust"), //
    d31("d31", "19", "强沙尘暴", "dust"), //
    d32("d32", "21", "霾", "haze"), //
    d53("d53", "21", "霾", "haze"),

    n0("n0", "31", "晴", "clear (night)"), //
    n1("n1", "29", "多云", "mostly cloudy (neight)"), //
    n2("n2", "27", "阴", "cloudy(neight)"), //
    n3("n3", "11", "阵雨", "showers"), //
    n4("n4", "45", "雷阵雨", "thundershowers"), //
    n5("n5", "35", "雷阵雨伴有冰雹", "mixed rain and hail"), //
    n6("n6", "18", "雨夹雪", "sleet"), //
    n7("n7", "9", "小雨", "drizzle"), //
    n8("n8", "9", "中雨", "showers"), //
    n9("n9", "12", "大雨", "showers"), //
    n00("n00", "31", "晴", "clear (night)"), //
    n01("n01", "29", "多云", "mostly cloudy (neight)"), //
    n02("n02", "27", "阴", "cloudy(neight)"), //
    n03("n03", "11", "阵雨", "showers"), //
    n04("n04", "45", "雷阵雨", "thundershowers"), //
    n05("n05", "35", "雷阵雨伴有冰雹", "mixed rain and hail"), //
    n06("n06", "18", "雨夹雪", "sleet"), //
    n07("n07", "9", "小雨", "drizzle"), //
    n08("n08", "9", "中雨", "showers"), //
    n09("n09", "12", "大雨", "showers"), //
    n10("n10", "3", "暴雨", "tropical storm"), //
    n11("n11", "3", "大暴雨", "tropical storm"), //
    n12("n12", "3", "特大暴雨", "tropical storm"), //
    n13("n13", "13", "阵雪", "snow flurries"), //
    n14("n14", "16", "小雪", "light snow showers"), //
    n15("n15", "16", "中雪", "snow"), //
    n16("n16", "41", "大雪", "heavy snow"), //
    n17("n17", "41", "暴雪", "heavy snow"), //
    n18("n18", "20", "雾", "foggy"), //
    n19("n19", "10", "冻雨", "freezing rain"), //
    n20("n20", "19", "沙尘暴", "dust"), //
    n21("n21", "9", "小雨-中雨", "showers"), //
    n22("n22", "12", "中雨-大雨", "showers"), //
    n23("n23", "3", "大雨-暴雨", "tropical storm"), //
    n24("n24", "3", "暴雨-大暴雨", "tropical storm"), //
    n25("n25", "3", "大暴雨-特大暴雨", "tropical storm"), //
    n26("n26", "16", "小雪-中雪", "snow"), //
    n27("n27", "41", "中雪-大雪", "heavy snow"), //
    n28("n28", "43", "大雪-暴雪", "heavy snow"), //
    n29("n29", "19", "浮尘", "dust"), //
    n30("n30", "19", "扬沙", "dust"), //
    n31("n31", "19", "强沙尘暴", "dust"), //
    n32("n32", "21", "霾", "haze"), //
    n53("n53", "21", "霾", "haze"); //
    String weatherCode;
    String yahooCode;
    String description;
    String endescription;

    WeatherState(String weatherCode, String yahooCode, String description, String endescription) {
        this.weatherCode = weatherCode;
        this.yahooCode = yahooCode;
        this.description = description;
        this.endescription = endescription;
    }

    public String getWeatherCode() {
        return weatherCode;
    }

    public void setWeatherCode(String weatherCode) {
        this.weatherCode = weatherCode;
    }

    public String getYahooCode() {
        return yahooCode;
    }

    public void setYahooCode(String yahooCode) {
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

    public static WeatherState getObject(String str) {
        for (WeatherState we : WeatherState.values()) {
            if (we.name().equals(str)) { return we; }
        }
        return null;
    }
}
