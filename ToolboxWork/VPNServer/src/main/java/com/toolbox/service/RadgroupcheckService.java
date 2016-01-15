package com.toolbox.service;

import java.util.List;

import com.toolbox.entity.RadgroupcheckEntity;

/**
* @author E-mail:86yc@sina.com
* 
*/
public interface RadgroupcheckService {
    public List<RadgroupcheckEntity> findsByGroupnames(List<String> groupnames);
}
