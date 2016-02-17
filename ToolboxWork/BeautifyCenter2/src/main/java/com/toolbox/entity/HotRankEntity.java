package com.toolbox.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.alibaba.fastjson.JSONArray;

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
    private JSONArray     appTags;
    private InActionCount actionCount;
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

    public JSONArray getAppTags() {
        return appTags;
    }

    public void setAppTags(JSONArray appTags) {
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

}
