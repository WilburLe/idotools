/*
 * Copyright (c) 2010-2013 www.pixshow.net All Rights Reserved
 *
 * File:WeatherRankingAction.java Project: LvWeatherService
 * 
 * Creator:4399-lvtu-8 
 * Date:Dec 10, 2013 10:19:48 AM
 * 
 */
package com.toolbox.weather.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.toolbox.weather.bean.CityWeatherRankBean;
import com.toolbox.weather.service.WeatherService;

/**
 * 
 * 
 * 
 * @author 4399-lvtu-8
 * @author $Author:$
 * @version $Revision:$ $Date:$ 
 * @since Dec 10, 2013
 * 
 */
@Controller
public class WeatherRankingListAction {
    @Autowired
    private WeatherService weatherService;

    private int limitNum = 20;
    private int isTw;         //1 台湾
    private int seq;          //1 倒序

    private Map<String, Object> result = new HashMap<String, Object>();

    @RequestMapping(value = "tempRanking")
    public String tempRankings() {
        List<Map<String, Object>> ranking = new ArrayList<Map<String, Object>>();
        boolean isTwSql = false;
        if (isTw == 1) {
            isTwSql = true;
        }
        String seqSql = "ASC";
        if (seq == 1) {
            seqSql = "DESC";
        }
        List<CityWeatherRankBean> weathers = weatherService.tempAllRanking(isTwSql, seqSql, limitNum);
        for (CityWeatherRankBean weather : weathers) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("cityName", weather.getCityName());
            map.put("cityId", weather.getCityId());
            map.put("curTemp", weather.getCurTemp());
            if (isTwSql) {
                map.put("rank", weather.getTempRankingTaiwan());
            } else {
                map.put("rank", weather.getTempRanking());
            }
            ranking.add(map);
        }
        result.put("ranking", ranking);
        return SUCCESS;
    }

    @RequestMapping(value = "pmRanking")
    public JSON pmRanking() {
        List<Map<String, Object>> ranking = new ArrayList<Map<String, Object>>();
        boolean isTwSql = false;
        if (isTw == 1) {
            isTwSql = true;
        }
        String seqSql = "ASC";
        if (seq == 1) {
            seqSql = "DESC";
        }
        List<CityWeatherRankBean> weathers = weatherService.pmAllRanking(isTwSql, seqSql, limitNum);
        for (CityWeatherRankBean weather : weathers) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("cityName", weather.getCityName());
            map.put("cityId", weather.getCityId());
            map.put("curPm", weather.getPm25());
            if (isTwSql) {
                map.put("rank", weather.getPmRankingTaiwan());
            } else {
                map.put("rank", weather.getPmRanking());
            }
            ranking.add(map);
        }
        result.put("ranking", ranking);
        return SUCCESS;
    }

    public int getIsTw() {
        return isTw;
    }

    public void setIsTw(int isTw) {
        this.isTw = isTw;
    }

    public Map<String, Object> getResult() {
        return result;
    }

    public void setResult(Map<String, Object> result) {
        this.result = result;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public int getLimitNum() {
        return limitNum;
    }

    public void setLimitNum(int limitNum) {
        this.limitNum = limitNum;
    }

}
