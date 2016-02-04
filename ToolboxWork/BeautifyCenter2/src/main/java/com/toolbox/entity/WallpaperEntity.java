package com.toolbox.entity;

/**
* @author E-mail:86yc@sina.com
* 
*/
public class WallpaperEntity {
    private int    id;
    private int    menuId;
    private String name;
    private String viewImages;
    private String downloadUrl;
    private String info;
    private long   createDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMenuId() {
        return menuId;
    }

    public void setMenuId(int menuId) {
        this.menuId = menuId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getViewImages() {
        return viewImages;
    }

    public void setViewImages(String viewImages) {
        this.viewImages = viewImages;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
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
