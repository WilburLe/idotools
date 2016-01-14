package com.toolbox.entity;

/**
* @author E-mail:86yc@sina.com
*/
public class MenuEntity {
    private int    id;
    private int    parentId;
    private String name;
    private int    indexNu;
    private String info;
    private long   createDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIndexNu() {
        return indexNu;
    }

    public void setIndexNu(int indexNu) {
        this.indexNu = indexNu;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

}
