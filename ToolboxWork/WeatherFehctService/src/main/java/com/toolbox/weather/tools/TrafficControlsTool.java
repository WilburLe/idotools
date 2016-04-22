/*
 * Copyright (c) 2010-2013 www.pixshow.net All Rights Reserved
 *
 * File:TrafficControlsTool.java Project: LvWeatherService
 * 
 * Creator:4399-lvtu-8 
 * Date:Dec 30, 2013 6:19:38 PM
 * 
 */
package com.toolbox.weather.tools;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.toolbox.framework.utils.DateUtility;
import com.toolbox.framework.utils.StringUtility;
import com.toolbox.weather.service.PropertiesService;

/**
 * 
 * 
 * 
 * @author 4399-lvtu-8
 * @author $Author:$
 * @version $Revision:$ $Date:$
 * @since Dec 30, 2013
 * 
 */
@Component
public class TrafficControlsTool {
    @Autowired
    private PropertiesService propertiesService;

    public String getNum(Date date, int cityId) {
        String data = propertiesService.getValue("xianxing_" + cityId);
        if (StringUtility.isEmpty(data)) {// 
            return "-2"; //该城市不限行
        }
        JSONObject json = JSONObject.parseObject(data);
        String str = DateUtility.format(date, "yyyyMMdd");
        if (json.containsKey(str)) {
            return json.getString(str);
        }
        return "-1"; //该城市今日不限行
    }

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:/config/framework/spring.xml");
        TrafficControlsTool tool = context.getBean(TrafficControlsTool.class);
        System.out.println("今日限行：" + tool.getNum(new Date(), 12166));

    }
}
