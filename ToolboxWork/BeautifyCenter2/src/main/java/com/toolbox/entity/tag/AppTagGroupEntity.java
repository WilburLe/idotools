package com.toolbox.entity.tag;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
* @author E-mail:86yc@sina.com
*/
@Document(collection = "apptag")
public class AppTagGroupEntity {
    @Id
    private String             id;
    private String             appType;
    private List<AppTagEntity> tags = new ArrayList<AppTagEntity>();
    private int                sortNu;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAppType() {
        return appType;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }

    public List<AppTagEntity> getTags() {
        return tags;
    }

    public void setTags(List<AppTagEntity> tags) {
        this.tags = tags;
    }

    public int getSortNu() {
        return sortNu;
    }

    public void setSortNu(int sortNu) {
        this.sortNu = sortNu;
    }

}
