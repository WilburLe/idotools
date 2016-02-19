package com.toolbox.common;

/**
* @author E-mail:86yc@sina.com
* 
*/
public enum SystemConfigEnum {
    config_hot("hot", "热门"), //
    config_banner("banner", "Banner"),//
    ;
    private String type;
    private String name;

    SystemConfigEnum(String type, String name) {
        this.name = name;
        this.type = type;
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
