package com.toolbox.web.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
* @author E-mail:86yc@sina.com
* 
*/
@Document(collection = "bannercontent")
public class BannerContentEntity {
    @Id
    private String id;
    private String bannerId;
    private String appType;
    private String appId;
    private int    sortNu;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBannerId() {
        return bannerId;
    }

    public void setBannerId(String bannerId) {
        this.bannerId = bannerId;
    }

    public String getAppType() {
        return appType;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public int getSortNu() {
        return sortNu;
    }

    public void setSortNu(int sortNu) {
        this.sortNu = sortNu;
    }

}
