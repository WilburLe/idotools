package com.toolbox.controller.server;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toolbox.entity.SpeedreportEntity;
import com.toolbox.service.SpeedreportService;

/**
* @author E-mail:86yc@sina.com
* 
*/
@Controller
public class SpeedtestController {
    @Autowired
    private SpeedreportService speedreportService;

    @RequestMapping(value = "speedtestview", method = RequestMethod.GET)
    public ModelAndView speedtestview() {
        
        
        return new ModelAndView("/speedtestview");
    }
    
    @RequestMapping(value = "speedtest", method = RequestMethod.POST)
    public @ResponseBody JSON speedtest(@RequestBody String data) {
        try {
            JSONObject json = JSONObject.parseObject(data);
            String username = json.getString("username");
            JSONArray speedtest = json.getJSONArray("speedtest");
            Date date = new Date();
            for (int i = 0; i < speedtest.size(); i++) {
                try {
                    JSONObject speed = speedtest.getJSONObject(i);
                    SpeedreportEntity speedreport = new SpeedreportEntity();
                    speedreport.setReportdate(date);
                    speedreport.setAveragetime(speed.getFloatValue("averageTime"));
                    speedreport.setCountrycode(speed.getString("countryCode"));
                    speedreport.setFailrate(speed.getFloatValue("failRate"));
                    speedreport.setServername(speed.getString("serverName"));
                    speedreport.setUsername(username);
                    speedreportService.save(speedreport);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
