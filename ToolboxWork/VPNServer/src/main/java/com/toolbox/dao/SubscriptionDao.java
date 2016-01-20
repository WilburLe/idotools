package com.toolbox.dao;

import org.springframework.stereotype.Repository;

import com.toolbox.entity.SubscriptionEntity;
import com.toolbox.framework.spring.support.BaseDao;

/**
* @author E-mail:86yc@sina.com
* 
*/
@Repository
public class SubscriptionDao extends BaseDao {
    public void save(SubscriptionEntity subscription) {
        insertBean("subscription", subscription);
    }

    public void update(SubscriptionEntity subscription) {
        updateBean("subscription", SubscriptionEntity.class, "username=" + subscription.getUsername());
    }

    public SubscriptionEntity find(String username) {
        return queryForBean("select * from subscription where username=?", SubscriptionEntity.class, username);
    }

}
