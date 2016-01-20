package com.toolbox.service;

import com.toolbox.entity.SubscriptionEntity;

/**
* @author E-mail:86yc@sina.com
* 
*/
public interface SubscriptionService {
    public void save(SubscriptionEntity subscription);

    public void update(SubscriptionEntity subscription);

    public SubscriptionEntity find(String username);
    
    public long updateSubscribe(String username, String subscribetype, String subscribeno);
}
