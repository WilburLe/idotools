package com.toolbox.web.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.alibaba.fastjson.JSONObject;

/**
* @author E-mail:86yc@sina.com
* 
*/
@Document(collection = "apptab")
public class AppTabEntity {
    @Id
    private String     id;
    private String     elementId;
    private String     code;
    private String[]   apps;
    private JSONObject name;
    private int        sortNu;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public JSONObject getName() {
        return name;
    }

    public void setName(JSONObject name) {
        this.name = name;
    }

    public String[] getApps() {
        return apps;
    }

    public void setApps(String[] apps) {
        this.apps = apps;
    }

    public int getSortNu() {
        return sortNu;
    }

    public void setSortNu(int sortNu) {
        this.sortNu = sortNu;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

}
