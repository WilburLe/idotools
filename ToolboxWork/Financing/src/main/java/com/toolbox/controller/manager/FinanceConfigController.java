package com.toolbox.controller.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.toolbox.entity.ToolboxConfigEntity;
import com.toolbox.service.RedisStatService;
import com.toolbox.service.ToolboxConfigService;

@Controller
@RequestMapping("/mgr")
public class FinanceConfigController {
    @Autowired
    private ToolboxConfigService toolboxConfigService;
    @Autowired
    private RedisStatService     redisStatService;
    private final static String  code = "finance";

    @RequestMapping(value = "/finance", method = RequestMethod.GET)
    public ModelAndView financingConfig() {
        ToolboxConfigEntity config = toolboxConfigService.findByCode(code);

        return new ModelAndView("/mgr/financeConfig").addObject("config", config);
    }

    @RequestMapping(value = "/finance", method = RequestMethod.POST)
    public ModelAndView updateConfig(boolean content_open, String content_adurl) {
        ToolboxConfigEntity entity = toolboxConfigService.findByCode(code);
        JSONObject content = new JSONObject();
        content.put("open", content_open);
        content.put("adurl", content_adurl);
        entity.setContent(content.toJSONString());
        toolboxConfigService.updateByCode(entity);

        return new ModelAndView("redirect:/mgr/finance/");
    }

    @RequestMapping(value = "/stat/{days}", method = RequestMethod.GET)
    public ModelAndView viwstat(//
            @PathVariable(value = "days") int days//
    ) {
        JSON stats = redisStatService.viewstat(code, days);
        return new ModelAndView("/mgr/financeStat").addObject("stats", stats);
    }

}
