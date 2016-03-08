package com.toolbox.common;

/**
* @author E-mail:86yc@sina.com
* 
*/
public enum StatKeyEnum {
    wallpaper("ds_idotools_wallpaper_", AppEnum.wallpaper.getCollection()), //
    lockscreen("ds_idotools_lockscreenTheme_", AppEnum.lockscreen.getCollection()), //
    weather("ds_idotools_weatherTheme_", AppEnum.weather.getCollection()),//
    ;
    private String statCode;
    private String appCode;

    StatKeyEnum(String statCode, String appCode) {
        this.statCode = statCode;
        this.appCode = appCode;
    }

    public String getStatCode() {
        return statCode;
    }

    public void setStatCode(String statCode) {
        this.statCode = statCode;
    }

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

}
