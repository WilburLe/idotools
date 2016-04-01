package com.toolbox.web.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
* @author E-mail:86yc@sina.com
* 
*/
@Document(collection = "hotrank")
public class HotRankEntity {
    @Id
    private String        id;
    private String        elementId;
    private String        previewImageUrl;
    private String        appType;
    private String[]      appTags;
    private String        market;         //上架范围
    private InActionCount actionCount;
    private float         weight;
    private int           sortNu;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getElementId() {
        return elementId;
    }

    public void setElementId(String elementId) {
        this.elementId = elementId;
    }

    public String getPreviewImageUrl() {
        return previewImageUrl;
    }

    public void setPreviewImageUrl(String previewImageUrl) {
        this.previewImageUrl = previewImageUrl;
    }

    public String getAppType() {
        return appType;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }

    public String[] getAppTags() {
        return appTags;
    }

    public void setAppTags(String[] appTags) {
        this.appTags = appTags;
    }

    public InActionCount getActionCount() {
        return actionCount;
    }

    public void setActionCount(InActionCount actionCount) {
        this.actionCount = actionCount;
    }

    public int getSortNu() {
        return sortNu;
    }

    public void setSortNu(int sortNu) {
        this.sortNu = sortNu;
    }

    public String getMarket() {
        return market;
    }

    public void setMarket(String market) {
        this.market = market;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

}
