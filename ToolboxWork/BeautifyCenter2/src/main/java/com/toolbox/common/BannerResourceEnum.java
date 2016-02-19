package com.toolbox.common;

/**
* @author E-mail:86yc@sina.com
* 
*/
public enum BannerResourceEnum {
    H5("h5", "H5运营"), //
    Subject("subject", "专题"), //
    BannerGroup("bannerGroup", "合辑"),//
    ;
    private String type;
    private String name;

    BannerResourceEnum(String type, String name) {
        this.name = name;
        this.type = type;
    }

    public static BannerResourceEnum byType(String type) {
        BannerResourceEnum[] banners = values();
        for (BannerResourceEnum banner : banners) {
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
