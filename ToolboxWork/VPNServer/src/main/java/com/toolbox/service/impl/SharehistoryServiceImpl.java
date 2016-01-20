package com.toolbox.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.toolbox.common.RadgroupTypeEnum;
import com.toolbox.dao.SharehistoryDao;
import com.toolbox.entity.SharehistoryEntity;
import com.toolbox.service.SharehistoryService;
import com.toolbox.service.SubscriptionService;

/**
* @author E-mail:86yc@sina.com
* 
*/
@Service("SharehistoryService")
public class SharehistoryServiceImpl implements SharehistoryService {
    @Autowired
    private SharehistoryDao     sharehistoryDao;
    @Autowired
    private SubscriptionService subscriptionService;

    public SharehistoryEntity findByUsername(String username) {
        return sharehistoryDao.findByUsername(username);
    }

    public long save(SharehistoryEntity sharehistory) {
        long expireddate = subscriptionService.updateSubscribe(sharehistory.getUsername(), RadgroupTypeEnum.VIP1.getName(), "share");
        sharehistoryDao.save(sharehistory);
        return expireddate;
    }

}
