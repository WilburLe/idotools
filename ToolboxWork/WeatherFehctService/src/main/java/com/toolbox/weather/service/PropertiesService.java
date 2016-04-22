package com.toolbox.weather.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.toolbox.framework.utils.StringUtility;
import com.toolbox.weather.dao.PropertiesDao;

@Service
public class PropertiesService {

    @Autowired
    PropertiesDao propertyDao;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public String getValue(String key) {
        try {
            return propertyDao.getValue(key);
        } catch (Exception e) {
            return "";
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public String getValue(String key, String defaultValue) {
        String tmp = "";
        try {
            tmp = propertyDao.getValue(key);
        } catch (Exception e) {
            return defaultValue;
        }
        if (StringUtility.isNotEmpty(tmp))
            return tmp;
        return defaultValue;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void setValue(String key, String value) {
        propertyDao.setValue(key, value);
    }
}
