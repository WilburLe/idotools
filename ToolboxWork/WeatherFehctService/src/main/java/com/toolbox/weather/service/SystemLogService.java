package com.toolbox.weather.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.toolbox.weather.dao.SystemLogDao;

@Service
public class SystemLogService {

    @Autowired
    private SystemLogDao logDao;

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void log(String name, String message) {
        logDao.log(name, message);
    }
}
