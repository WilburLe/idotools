package com.toolbox.common;

/**
 * @author E-mail:86yc@sina.com
 */
public enum AppEnum {
    wallpaper("wallpaper", "wallpaper", "壁纸", "每日壁纸 iDO Daily Wallpaper"), //
    lockscreen("lockscreen", "lockscreen", "锁屏", "闪电锁屏 iDO Lockscreen"), //
    weather("weather", "weather", "天气", "即时天气 iDO Weather"), //
    ;

    private String collection;
    private String enName;
    private String cnName;
    private String appName;

    AppEnum(String collection, String enName, String cnName, String appName) {
        this.cnName = cnName;
        this.enName = enName;
        this.collection = collection;
        this.appName = appName;
    }

    public static AppEnum byCollection(String collection) {
        AppEnum[] appEnums = AppEnum.values();
        for (AppEnum appEnum : appEnums) {
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
