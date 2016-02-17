package com.toolbox.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.toolbox.common.SystemConfigEnum;
import com.toolbox.web.entity.HotRankEntity;
import com.toolbox.web.entity.SystemConfigEmtity;
import com.toolbox.web.service.HotRankService;
import com.toolbox.web.service.SystemConfigService;

/**
* @author E-mail:86yc@sina.com
* 
*/
@Controller
public class HotRankController {
    @Autowired
    private SystemConfigService systemConfigService;
    @Autowired
    private HotRankService      hotRankService;

    @RequestMapping(value = "hot/rank/{rankType}/{appType}", method = RequestMethod.GET)
    public ModelAndView hotrank(@PathVariable("rankType") String rankType, @PathVariable("appType") String appType) {
        SystemConfigEmtity hconfig = systemConfigService.findByConfigType(SystemConfigEnum.config_hot.getType());

        List<HotRankEntity> hotRanks = hotRankService.findAllByAppType(appType);
        return new ModelAndView("hot/rank").addObject("hconfig", hconfig).addObject("hotRanks", hotRanks).addObject("rankType", rankType);
    }

}
