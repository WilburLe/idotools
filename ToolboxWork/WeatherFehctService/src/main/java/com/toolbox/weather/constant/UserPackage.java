package com.toolbox.weather.constant;

public enum UserPackage {
    zh_1(1, "zh_1", "zh_1"), //
    shiShang(2, "1", "时尚模板包"), //
    WPSimple(3, "WP_Simple", "WP_Simple"), //
    HideLOGO(4, "2", "Hide LOGO"),
    ColourfulMagazine(5, "3", "Colourful Magazine");

    private int    id;
    private String code;
    private String name;

    UserPackage(int id, String code, String name) {
        this.id = id;
        this.name = name;
        this.code = code;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
