package com.toolbox.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.toolbox.dao.SpeedreportDao;
import com.toolbox.entity.SpeedreportEntity;
import com.toolbox.service.SpeedreportService;

/**
* @author E-mail:86yc@sina.com
* 
*/
@Service("SpeedreportService")
public class SpeedreportServiceImpl implements SpeedreportService {
    @Autowired
    private SpeedreportDao speedreportDao;

    @Override
    public void save(SpeedreportEntity speedreport) {
        speedreportDao.save(speedreport);
    }

}
