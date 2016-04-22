/*
 * Copyright (c) 2010-2013 www.pixshow.net All Rights Reserved
 *
 * File:FetchWeatherDataWorker.java Project: LvWeatherService
 * 
 * Creator:4399-lvtu-8 
 * Date:Dec 2, 2013 6:53:53 PM
 * 
 */
package com.toolbox.weather.dtask;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toolbox.weather.data.WeatherDataFetcher;

/**
 * 
 * 
 * 
 * @author 4399-lvtu-8
 * @author $Author:$
 * @version $Revision:$ $Date:$ 
 * @since Dec 2, 2013
 * 
 */

@Plugin(name = "WeatherService.FetchWeatherData", type = DWorker.class)
public class FetchWeatherDataWorker implements DWorker {

    @Autowired
    private WeatherDataFetcher weatherDataFetcher;

    @Override
    public String execute(String data) {
        JSONArray cityIds = JSONObject.fromObject(data).getJSONArray("cityIds");
        for (int i = 0; i < cityIds.size(); i++) {
            int cityId = cityIds.getInt(i);
            try {
                weatherDataFetcher.fetchDataByCityId(cityId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
