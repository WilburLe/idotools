package com.toolbox.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.toolbox.dao.RadcheckDao;
import com.toolbox.entity.RadcheckEntity;
import com.toolbox.service.RadcheckService;

/**
* @author E-mail:86yc@sina.com
* 
*/
@Service("RadcheckService")
public class RadcheckServiceImpl implements RadcheckService {
    @Autowired
    private RadcheckDao radcheckDao;

    public int save(RadcheckEntity radcheck) {
        return radcheckDao.save(radcheck);
    }

}
