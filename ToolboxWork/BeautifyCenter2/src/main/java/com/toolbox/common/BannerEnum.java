package com.toolbox.common;

/**
* @author E-mail:86yc@sina.com
* 
*/
public enum BannerEnum {
    h5("h5", "H5运营"), //
    Subject("subject", "专题"), //
    SubjectGroup("subjectGroup", "专题合辑"),//
    ;
    private String type;
    private String name;

    BannerEnum(String type, String name) {
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
