package com.toolbox.common;

/**
* @author E-mail:86yc@sina.com
* 
*/
public enum TabEnum {
    wallpaper("wallpaper", "wallpaper", "壁纸", "每日壁纸 iDO Daily Wallpaper"), //
    lockscreen("lockscreen", "lockscreen", "锁屏", "闪电锁屏 iDO Lockscreen"), //
    weather("weather", "weather", "天气", "即时天气 iDO Weather"), //
    dtclock("dtclock", "dtclock", "闹钟", "闹钟"), //
    dtFlashLigh("dtFlashLight", "dtFlashLight", "手电筒", "手电筒"),
    dtEasysoftkey("dtEasysoftkey", "dtEasysoftkey", "小白点", "小白点"),
    dtWeather("dtWeather", "dtWeather", "天气", "天气"),
    dtCalculator("dtCalculator", "dtCalculator", "计算器", "计算器"),
    dtCompass("dtCompass", "dtCompass", "指南针", "指南针"),
    dtqrcode("dtqrcode", "dtqrcode", "二维码", "二维码"),
    ;

    private String collection;
    private String enName;
    private String cnName;
    private String appName;

    TabEnum(String collection, String enName, String cnName, String appName) {
        this.cnName = cnName;
        this.enName = enName;
        this.collection = collection;
        this.appName = appName;
    }

    public static TabEnum byCollection(String collection) {
        TabEnum[] appEnums = TabEnum.values();
        for (TabEnum appEnum : appEnums) {
            if (appEnum.getCollection().equals(collection)) {
                return appEnum;
            }
        }
        return null;
    }

    public String getCollection() {
        return collection;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }

    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }

    public String getCnName() {
        return cnName;
    }

    public void setCnName(String cnName) {
        this.cnName = cnName;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

}
