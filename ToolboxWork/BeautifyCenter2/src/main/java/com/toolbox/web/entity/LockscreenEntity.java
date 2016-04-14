package com.toolbox.web.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.alibaba.fastjson.JSONObject;

/**
* @author E-mail:86yc@sina.com
* 
*/
@Document(collection = "lockscreen")
public class LockscreenEntity {
    @Id
    private String        id;
    private String        elementId;
    private String        packageName;
    private long          fileSize;
    private String        md5;
    private String        sha1;
    private String        iconUrl;         //ICON
    private String        previewImageUrl; //封面图
    private String[]      detailUrl;       //详情图
    private JSONObject    name;
    private JSONObject    description;
    //{zh_CN:src,en_US:https://play.google.com/store/apps/details?id=com.dotool.flashlockscreen.theme.hounian }
    private JSONObject    actionUrl;       //下载地址
    private String[]      market;          //上架范围
    private InActionCount actionCount;     //下载量
    private String[]      tags;
    private long          createDate;

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

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getSha1() {
        return sha1;
    }

    public void setSha1(String sha1) {
        this.sha1 = sha1;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getPreviewImageUrl() {
        return previewImageUrl;
    }

    public void setPreviewImageUrl(String previewImageUrl) {
        this.previewImageUrl = previewImageUrl;
    }

    public JSONObject getName() {
        return name;
    }

    public void setName(JSONObject name) {
        this.name = name;
    }

    public JSONObject getDescription() {
        return description;
    }

    public void setDescription(JSONObject description) {
        this.description = description;
    }

    public JSONObject getActionUrl() {
        return actionUrl;
    }

    public void setActionUrl(JSONObject actionUrl) {
        this.actionUrl = actionUrl;
    }

    public String[] getDetailUrl() {
        return detailUrl;
    }

    public void setDetailUrl(String[] detailUrl) {
        this.detailUrl = detailUrl;
    }

    public String[] getMarket() {
        return market;
    }

    public void setMarket(String[] market) {
        this.market = market;
    }

    public InActionCount getActionCount() {
        return actionCount;
    }

    public void setActionCount(InActionCount actionCount) {
        this.actionCount = actionCount;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

}
