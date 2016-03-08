package com.toolbox.common;

/**
* @author E-mail:86yc@sina.com
* 
*/
public enum LanguageEnum {
    zh_CN("zh_CN", "中文简体"), //
    zh_TW("zh_TW", "中文台湾"), //
    zh_HK("zh_HK", "中文香港"), //
    en_US("en_US", "英文"),//
    ;
    private String code;
    private String name;

    LanguageEnum(String code, String name) {
        this.code = code;
        this.name = name;
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
