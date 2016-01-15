package com.toolbox.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.toolbox.dao.RadgroupcheckDao;
import com.toolbox.entity.RadgroupcheckEntity;
import com.toolbox.framework.utils.SqlUtility;
import com.toolbox.service.RadgroupcheckService;

/**
* @author E-mail:86yc@sina.com
* 
*/
@Service("RadgroupcheckService")
public class RadgroupcheckServiceImpl implements RadgroupcheckService {
    @Autowired
    private RadgroupcheckDao radgroupcheckDao;

    public List<RadgroupcheckEntity> findsByGroupnames(List<String> groupnames) {
        return radgroupcheckDao.findsByGroupnames(groupnames);
    }
}
