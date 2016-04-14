package com.toolbox.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toolbox.common.SystemConfigEnum;
import com.toolbox.framework.utils.StringUtility;
import com.toolbox.schedule.SchedulerJobService;
import com.toolbox.schedule.job.Hot2RedisScheduleJob;
import com.toolbox.web.entity.SystemConfigEmtity;
import com.toolbox.web.service.SystemConfigService;

/**
* @author E-mail:86yc@sina.com
* 
*/
@Controller
@RequestMapping("config")
public class ConfigHotController {
    @Autowired
    private SystemConfigService systemConfigService;
    @Autowired
    private SchedulerJobService schedulerJobService;

    @RequestMapping(value = "hot", method = RequestMethod.GET)
    public ModelAndView confighot() {
        SystemConfigEmtity hconfig = systemConfigService.findByConfigType(SystemConfigEnum.config_hot.getType());

        return new ModelAndView("config/hot").addObject("hconfig", hconfig);
    }

    @RequestMapping(value = "hot/updateNu", method = RequestMethod.POST)
    public @ResponseBody JSON updateNu(String code, int nu) {
        SystemConfigEmtity hconfig = systemConfigService.findByConfigType(SystemConfigEnum.config_hot.getType());
        JSONObject hc = hconfig.getConfig();
        JSONObject hcc = hc.getJSONObject(code);
        hcc.put("nu", nu);
        hc.put(code, hcc);
        hconfig.setConfig(hc);
        systemConfigService.save(hconfig);
        return null;
    }

    @RequestMapping(value = "hot/updateCycle", method = RequestMethod.POST)
    public @ResponseBody JSON updateCycle(int cycle) {
        SystemConfigEmtity hconfig = systemConfigService.findByConfigType(SystemConfigEnum.config_hot.getType());
        JSONObject config = hconfig.getConfig();
        config.put("cycle", cycle);
        hconfig.setConfig(config);
        systemConfigService.save(hconfig);
        //重新启动任务
        String day = cycle / 24 > 0 ? "*/" + cycle / 24 : "*";
        int hour = cycle % 24;
        //0 0 15 0/3 * ?
        String cron = null;
        if ("*".equals(day)) {
            cron = "0 0 */" + hour + " *  * ?";
//            cron = "0 */" + hour + " * *  * ?";
        } else {
            cron = "0 0 " + hour + " " + day + "  * ?";
        }
        //        System.out.println("code>" + code + ", cron>" + cron);
        schedulerJobService.addJob(Hot2RedisScheduleJob.class, "Hot2RedisJob", "Hot2RedisJobGroup", cron, null);
        return null;
    }

    @RequestMapping(value = "hot/updateApps", method = RequestMethod.POST)
    public @ResponseBody JSON updateApps(String code, String apps) {
        if (StringUtility.isEmpty(apps)) {
            apps = "[]";
        }
        SystemConfigEmtity hconfig = systemConfigService.findByConfigType(SystemConfigEnum.config_hot.getType());
        JSONObject hc = hconfig.getConfig();
        JSONObject hcc = hc.getJSONObject(code);
        hcc.put("apps", JSONArray.parseArray(apps));
        hc.put(code, hcc);
        hconfig.setConfig(hc);
        systemConfigService.save(hconfig);
        return null;
    }

}
