package com.toolbox.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.toolbox.common.SystemConfigEnum;
import com.toolbox.framework.utils.StringUtility;
import com.toolbox.framework.utils.UUIDUtility;
import com.toolbox.web.entity.BannerResourceEntity;
import com.toolbox.web.service.BannerResourceService;
import com.toolbox.web.service.CommonJSONService;

/**
* @author E-mail:86yc@sina.com
* 
*/
@Controller
@RequestMapping("banner")
public class BannerResourceController {
    @Autowired
    private BannerResourceService bannerResourceService;
    @Autowired
    private CommonJSONService     commonJSONService;

    @RequestMapping(value = "resource/view/{resourceType}", method = RequestMethod.GET)
    public ModelAndView bannerview(@PathVariable("resourceType") String resourceType) {
        List<BannerResourceEntity> resources = bannerResourceService.findByResourceType(resourceType);
        return new ModelAndView("banner/resource").addObject("resources", resources).addObject("resourceType", resourceType);
    }

    @RequestMapping(value = "resource/upsert", method = RequestMethod.POST)
    public ModelAndView resourceUpsert(String elementId, String resourceType, String content) {
        BannerResourceEntity resource = bannerResourceService.findByElementId(elementId);
        if (resource == null) {
            resource = new BannerResourceEntity();
            resource.setElementId(UUIDUtility.uuid32());
            resource.setCreateDate(System.currentTimeMillis());
        }
        resource.setResourceType(resourceType);
        resource.setContent(JSONObject.parseObject(content));
        if (resource.getId() == null) {
            bannerResourceService.save(resource);
        } else {
            bannerResourceService.upsertResource(resource);
        }
        return new ModelAndView("redirect:view/"+resourceType);
    }

    @RequestMapping(value = "resource/edit/{elementId}", method = RequestMethod.GET)
    public ModelAndView resourceEdit(@PathVariable("elementId") String elementId) {
        BannerResourceEntity resource = bannerResourceService.findByElementId(elementId);
        return new ModelAndView("banner/resourceedit").addObject("resource", resource);
    }

    @RequestMapping(value = "resource/del/{elementId}", method = RequestMethod.GET)
    public @ResponseBody JSON resourceDel(@PathVariable("elementId") String elementId) {
        bannerResourceService.delResource(elementId);
        return null;
    }

    @RequestMapping(value = "resource/search", method = RequestMethod.POST)
    public @ResponseBody String resourceSearch(String appType, String elementId) {
        if (StringUtility.isEmpty(appType)) {
            return null;
        }
        Query query = new Query();
        query.addCriteria(Criteria.where("elementId").is(elementId));
        String app = commonJSONService.findOne(query, appType);
        return app;
    }

}
