package com.toolbox.web.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.alibaba.fastjson.JSONObject;

/**
* @author E-mail:86yc@sina.com
* 
*/
@Document(collection = "banner")
public class BannerEntity {
    @Id
    private String     id;
    private String     elementId;
    private String     bannerType;     //H5/专题/合辑
    private JSONObject title;
    private String     shareUrl;
    private String     previewImageUrl;
    private String     enPreviewImageUrl;
    private JSONObject content;
    private long       createDate;

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

    public String getBannerType() {
        return bannerType;
    }

    public void setBannerType(String bannerType) {
        this.bannerType = bannerType;
    }

    public JSONObject getTitle() {
        return title;
    }

    public void setTitle(JSONObject title) {
        this.title = title;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public String getPreviewImageUrl() {
        return previewImageUrl;
    }

    public void setPreviewImageUrl(String previewImageUrl) {
        this.previewImageUrl = previewImageUrl;
    }

    public JSONObject getContent() {
        return content;
    }

    public void setContent(JSONObject content) {
        this.content = content;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public String getEnPreviewImageUrl() {
        return enPreviewImageUrl;
    }

    public void setEnPreviewImageUrl(String enPreviewImageUrl) {
        this.enPreviewImageUrl = enPreviewImageUrl;
    }

}
