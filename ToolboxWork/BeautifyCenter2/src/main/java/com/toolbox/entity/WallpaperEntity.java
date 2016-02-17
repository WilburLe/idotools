package com.toolbox.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
* @author E-mail:86yc@sina.com
* 
*/
@Document(collection = "wallpaper")
public class WallpaperEntity {
    @Id
    private String      id;
    private String      elementId;
    private String      src;
    private String      previewImageUrl;
    private InActionCount actionCount;
    private long        createDate;
    private JSONObject  fileSize;
    private JSONObject  actionUrl;
    private JSONArray   tags;


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

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getPreviewImageUrl() {
        return previewImageUrl;
    }

    public void setPreviewImageUrl(String previewImageUrl) {
        this.previewImageUrl = previewImageUrl;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public JSONObject getFileSize() {
        return fileSize;
    }

    public void setFileSize(JSONObject fileSize) {
        this.fileSize = fileSize;
    }

    public JSONObject getActionUrl() {
        return actionUrl;
    }

    public void setActionUrl(JSONObject actionUrl) {
        this.actionUrl = actionUrl;
    }

    public JSONArray getTags() {
        return tags;
    }

    public void setTags(JSONArray tags) {
        this.tags = tags;
    }

    public InActionCount getActionCount() {
        return actionCount;
    }

    public void setActionCount(InActionCount actionCount) {
        this.actionCount = actionCount;
    }

}
