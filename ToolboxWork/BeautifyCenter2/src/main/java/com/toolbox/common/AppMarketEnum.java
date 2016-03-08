package com.toolbox.common;

/**
* @author E-mail:86yc@sina.com
* 
*/
public enum AppMarketEnum {
    China("china", "国内"), //
    GooglePlay("googlePlay", "googlePlay"),//
    ;
    private String code;
    private String name;

    AppMarketEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static AppMarketEnum byCode(String code) {
        AppMarketEnum[] nums = AppMarketEnum.values();
        for (AppMarketEnum num : nums) {
            if (num.getCode().equals(code)) {
                return num;
            }
        }
        return null;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
