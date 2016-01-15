package com.toolbox.entity;

import java.util.Date;

/**
* @author E-mail:86yc@sina.com
* 
* 订阅时效表
* username Unique
*/
public class ExpirationEntity {
    private int    id;
    private String username;
    private Date   expireddate;  //订阅时间
    private Date   subscribedate;//失效时间
    private String subscribetype;//订阅类型 FREE/VIP1

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

    public Date getExpireddate() {
        return expireddate;
    }

    public void setExpireddate(Date expireddate) {
        this.expireddate = expireddate;
    }

    public Date getSubscribedate() {
        return subscribedate;
    }

    public void setSubscribedate(Date subscribedate) {
        this.subscribedate = subscribedate;
    }

    public String getSubscribetype() {
        return subscribetype;
    }

    public void setSubscribetype(String subscribetype) {
        this.subscribetype = subscribetype;
    }

}
