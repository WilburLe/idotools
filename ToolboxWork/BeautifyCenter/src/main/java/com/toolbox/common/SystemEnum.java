package com.toolbox.common;

/**
* @author E-mail:86yc@sina.com
* 
*/
public enum SystemEnum {
    wallpaper("wallpaper", "壁纸"), //
    theme("theme", "主题"), //
    lockscreen("lockscreen", "锁屏"), //
    widget("widget", "小部件"),//
    ;

    private String tablename;
    private String cname;

    SystemEnum(String tablename, String cname) {
        this.tablename = tablename;
        this.cname = cname;
    }

    public String getTablename() {
        return tablename;
    }

    public void setTablename(String tablename) {
        this.tablename = tablename;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

}
