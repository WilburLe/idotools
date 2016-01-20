package com.toolbox.service;

import com.toolbox.entity.SharehistoryEntity;

/**
* @author E-mail:86yc@sina.com
* 
*/
public interface SharehistoryService {

    public SharehistoryEntity findByUsername(String username);

    public long save(SharehistoryEntity sharehistory);
}
