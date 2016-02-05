package com.toolbox.entity.tag;

/**
* @author E-mail:86yc@sina.com
* 
*/
public class AppTagEntity {

    private AppTagName   name;
    private String uuid;
    private int    sortNu;

    public int getSortNu() {
        return sortNu;
    }

    public void setSortNu(int sortNu) {
        this.sortNu = sortNu;
    }

    public AppTagName getName() {
        return name;
    }

    public void setName(AppTagName name) {
        this.name = name;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

}
