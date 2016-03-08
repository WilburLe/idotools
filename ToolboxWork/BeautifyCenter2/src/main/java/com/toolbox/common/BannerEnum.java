package com.toolbox.common;

/**
* @author E-mail:86yc@sina.com
* 
*/
public enum BannerEnum {
    H5("h5", "H5"), //
    Subject("subject", "专题"), //
    Group("group", "合辑"),//
    ;
    private String type;
    private String name;

    BannerEnum(String type, String name) {
        this.name = name;
        this.type = type;
    }

    public static BannerEnum byType(String type) {
        BannerEnum[] banners = values();
        for (BannerEnum banner : banners) {
            if(banner.type.equals(type)) {
                return banner;
            }
        }
        return null;
    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
