package com.toolbox.entity;

import java.util.Date;

/**
* @author E-mail:86yc@sina.com
* 
*/
public class SubscriptionEntity {
    private int    id;
    private String username;
    private String subscribetype;
    private Date   subscribedate;

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

    public String getSubscribetype() {
        return subscribetype;
    }

    public void setSubscribetype(String subscribetype) {
        this.subscribetype = subscribetype;
    }

    public Date getSubscribedate() {
        return subscribedate;
    }

    public void setSubscribedate(Date subscribedate) {
        this.subscribedate = subscribedate;
    }

}
