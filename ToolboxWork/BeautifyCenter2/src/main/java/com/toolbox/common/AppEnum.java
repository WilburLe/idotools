package com.toolbox.common;

/**
* @author E-mail:86yc@sina.com
* 
*/
public enum AppEnum {
    wallpaper("wallpaper", "wallpaper", "壁纸"), //
    lockscreen("lockscreen", "lockscreen", "锁屏"), //
    weather("weather", "weather", "天气"), //
    ;

    private String collection;
    private String enName;
    private String cnName;

    AppEnum(String collection, String enName, String cnName) {
        this.cnName = cnName;
        this.enName = enName;
        this.collection = collection;
    }

    public static AppEnum byCollection(String collection) {
        AppEnum[] appEnums = AppEnum.values();
        for (AppEnum appEnum : appEnums) {
            if(appEnum.getCollection().equals(collection)) {
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

}
