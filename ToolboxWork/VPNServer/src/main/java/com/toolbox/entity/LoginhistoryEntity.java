package com.toolbox.entity;

import java.util.Date;

/**
* @author E-mail:86yc@sina.com
* 
*/
public class LoginhistoryEntity {
    private int    id;
    private String username;
    private String usertype;
    private Date   logindate;

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

    public String getUsertype() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }

    public Date getLogindate() {
        return logindate;
    }

    public void setLogindate(Date logindate) {
        this.logindate = logindate;
    }

}
