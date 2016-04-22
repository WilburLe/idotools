/*
 * Copyright (c) 2010-2014 www.pixshow.net All Rights Reserved
 *
 * File:WeatherAlarmAction.java Project: WeatherService
 * 
 * Creator:4399-lvtu-8 
 * Date:2014年1月10日 下午7:40:07
 * 
 */
package com.toolbox.weather.action;

import java.util.HashMap;
import java.util.Map;

import javax.xml.ws.Action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.toolbox.weather.service.WeatherService;

/**
 * 
 * 
 * 
 * @author 4399-lvtu-8
 * @author $Author:$
 * @version $Revision:$ $Date:$ 
 * @since 2014年1月10日
 */
@Controller
public class WeatherAlarmAction {
    @Autowired
    private WeatherService weatherService;

    private int cityId;

    private Map<String, Object> result = new HashMap<String, Object>();

    @RequestMapping(value = "todayAlarms")
    public JSON todayAlarms() {
        result.put("todayAlarms", weatherService.findTodayAlarm());
        return SUCCESS;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public Map<String, Object> getResult() {
        return result;
    }

    public void setResult(Map<String, Object> result) {
        this.result = result;
    }

}
