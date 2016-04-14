package com.toolbox.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.toolbox.common.SystemConfigEnum;
import com.toolbox.framework.utils.StringUtility;
import com.toolbox.web.entity.HotRankEntity;
import com.toolbox.web.entity.InActionCount;
import com.toolbox.web.entity.SystemConfigEmtity;
import com.toolbox.web.service.CommonJSONService;
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
    private CommonJSONService   commonJSONService;
    @Autowired
    private HotRankService      hotRankService;

    @RequestMapping(value = "hot/rank/{market}/{appType}", method = RequestMethod.GET)
    public ModelAndView hotrank(@PathVariable("market") String market, @PathVariable("appType") String appType) {
        SystemConfigEmtity hconfig = systemConfigService.findByConfigType(SystemConfigEnum.config_hot.getType());
        JSONObject config = hconfig.getConfig();
        JSONObject appConfig = config.getJSONObject("appConfig");

        JSONObject appCg = appConfig.getJSONObject(appType);
        JSONArray capps = appCg.getJSONArray("apps");
        List<HotRankEntity> hotRanks = new ArrayList<HotRankEntity>();
        if (capps.size() == 0) {
            return new ModelAndView("hot/rank").addObject("hconfig", hconfig).addObject("hotRanks", hotRanks).addObject("market", market).addObject("appType", appType);
        }

        List<DBObject> pipeline = new ArrayList<DBObject>();
        pipeline.add(new BasicDBObject("$sort", new BasicDBObject("sortNu", 1)));
        List<String> hotRankstr = commonJSONService.findLists(pipeline, "hotrank");

        List<HotRankEntity> ranks = new ArrayList<HotRankEntity>();
        for (int i = 0; i < hotRankstr.size(); i++) {
            JSONObject json = JSONObject.parseObject(hotRankstr.get(i));
            HotRankEntity hot = JSONObject.toJavaObject(json, HotRankEntity.class);
            if (hot.getMarket().equals(market)) {
                ranks.add(hot);
            }
        }
        return new ModelAndView("hot/rank").addObject("hconfig", hconfig).addObject("hotRanks", ranks).addObject("market", market).addObject("appType", appType);
    }

    @RequestMapping(value = "hot/rank/edit", method = RequestMethod.POST)
    public @ResponseBody JSON hotrankedit(String elementId, String sea_elementId, String sea_appType, int sortNuOld, int sortNuNew, String market) {
        HotRankEntity hotRank = hotRankService.findByElementId(elementId, sortNuOld);
        hotRank.setSortNu(sortNuOld);

        String app = commonJSONService.findOne(new Query(Criteria.where("elementId").is(sea_elementId)), sea_appType);
        if (StringUtility.isNotEmpty(sea_elementId) && StringUtility.isNotEmpty(app)) {
            JSONObject appJSON = JSONObject.parseObject(app);
            hotRank.setAppType(sea_appType);
            hotRank.setElementId(sea_elementId);
            hotRank.setPreviewImageUrl(appJSON.getString("previewImageUrl"));
            JSONObject actionCountj = appJSON.getJSONObject("actionCount");
            InActionCount actionCount = JSONObject.toJavaObject(actionCountj, InActionCount.class);
            hotRank.setActionCount(actionCount);

            JSONArray tagsj = appJSON.getJSONArray("tags");
            if (tagsj != null && tagsj.size() > 0) {
                String[] tags = new String[tagsj.size()];
                for (int i = 0; i < tagsj.size(); i++) {
                    tags[i] = tagsj.getString(i);
                }
                hotRank.setAppTags(tags);
            }
            hotRank.setMarket(market);
//            hotRankService.delete(new Query(Criteria.where("elementId").is(elementId).andOperator(Criteria.where("market").is(market))));
            hotRankService.save(hotRank);
        } else {
            Update update = new Update();
            update.set("sortNu", sortNuNew);
            hotRankService.updateFirst(new Query(Criteria.where("elementId").is(elementId)), update);
        }
        
        hotRankService.hot2redis();
        return null;
    }

}
