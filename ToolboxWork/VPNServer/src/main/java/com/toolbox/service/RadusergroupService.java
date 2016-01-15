package com.toolbox.service;

import java.util.List;

import com.toolbox.entity.RadusergroupEntity;

/**
* @author E-mail:86yc@sina.com
* 
*/
public interface RadusergroupService {
    public List<RadusergroupEntity> findsByUsernames(List<String> usernames);
}
