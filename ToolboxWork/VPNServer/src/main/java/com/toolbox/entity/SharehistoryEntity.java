package com.toolbox.entity;

import java.util.Date;

/**
* @author E-mail:86yc@sina.com
* 
*/
public class SharehistoryEntity {
    private int    id;
    private String username;
    private Date   sharedate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getSharedate() {
        return sharedate;
    }

    public void setSharedate(Date sharedate) {
        this.sharedate = sharedate;
    }

}
