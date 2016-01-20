package com.toolbox.service.impl;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.toolbox.common.RadgroupTypeEnum;
import com.toolbox.dao.ExpirationDao;
import com.toolbox.dao.RadusergroupDao;
import com.toolbox.dao.SubscriptionDao;
import com.toolbox.entity.ExpirationEntity;
import com.toolbox.entity.RadusergroupEntity;
import com.toolbox.entity.SubscriptionEntity;
import com.toolbox.service.SubscriptionService;

/**
* @author E-mail:86yc@sina.com
* 
*/
@Service("SubscriptionService")
public class SubscriptionServiceImpl implements SubscriptionService {
    @Autowired
    private SubscriptionDao subscriptionDao;
    @Autowired
    private ExpirationDao   expirationDao;
    @Autowired
    private RadusergroupDao radusergroupDao;

    public void save(SubscriptionEntity subscription) {
        subscriptionDao.save(subscription);
    }

    public void update(SubscriptionEntity subscription) {
        subscriptionDao.update(subscription);
    }

    public SubscriptionEntity find(String username) {
        return subscriptionDao.find(username);
    }

    public long updateSubscribe(String username, String subscribetype, String subscribeno) {
        //计算本次订阅的过期时间
        Date date = new Date();
        //订阅历史表新增记录
        SubscriptionEntity subscription = new SubscriptionEntity();
        subscription.setUsername(username);
        subscription.setSubscribedate(date);
        subscription.setSubscribetype(subscribetype);
        subscription.setSubscribeno(subscribeno);
        subscriptionDao.save(subscription);
        //订阅表更新或新增
        ExpirationEntity expiration = expirationDao.findByUsername(username);
        if (expiration == null) {
            expiration = new ExpirationEntity();
        }

        /**
         * 上次的订阅是否过期
         * 如果没过期
         * 过期时间=上次订阅过期时间+本次订阅天数
         * 
         */
        if (expiration.getExpireddate().after(date)) {
            Calendar ce = Calendar.getInstance();
            ce.setTime(expiration.getExpireddate());
            ce.add(Calendar.DAY_OF_MONTH, RadgroupTypeEnum.byName(subscribetype).getDays());
            expiration.setExpireddate(ce.getTime());
        } else {
            Calendar cn = Calendar.getInstance();
            cn.add(Calendar.DAY_OF_MONTH, RadgroupTypeEnum.byName(subscribetype).getDays());
            Date expireddate = cn.getTime();

            expiration.setExpireddate(expireddate);
        }

        //根据过期时间计算当前VIP等级
        long expireddatel = expiration.getExpireddate().getTime();
        long mdate = expireddatel - date.getTime();
        RadgroupTypeEnum radgroupType = RadgroupTypeEnum.byDays((int) (mdate / 1000 / 60 / 60 / 24));

        expiration.setSubscribedate(date);
        expiration.setSubscribetype(radgroupType.getName());
        expiration.setUsername(username);
        if (expiration.getId() == 0) {
            expirationDao.save(expiration);
        } else {
            expirationDao.update(expiration);
        }
        //用户组表更新
        RadusergroupEntity radusergroup = radusergroupDao.findByUsername(username);
        radusergroup.setGroupname(radgroupType.getName());
        radusergroupDao.update(radusergroup);

        return expireddatel;
    }

}
