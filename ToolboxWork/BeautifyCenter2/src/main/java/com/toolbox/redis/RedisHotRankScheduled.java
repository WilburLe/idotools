package com.toolbox.redis;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.toolbox.common.AppEnum;
import com.toolbox.common.AppMarketEnum;
import com.toolbox.common.SystemConfigEnum;
import com.toolbox.framework.utils.ListUtiltiy;
import com.toolbox.framework.utils.StringUtility;
import com.toolbox.web.entity.HotRankEntity;
import com.toolbox.web.entity.SystemConfigEmtity;
import com.toolbox.web.service.CommonJSONService;
import com.toolbox.web.service.RedisService;
import com.toolbox.web.service.SystemConfigService;

/**
* @author E-mail:86yc@sina.com
* 
*/
@Service
public class RedisHotRankScheduled  {
    private final static Log    log = LogFactory.getLog(RedisHotRankScheduled.class);
    @Autowired
    private CommonJSONService   commonJSONService;
    @Autowired
    private SystemConfigService systemConfigService;
    @Autowired
    private RedisService     redisService;
    
    @Scheduled(fixedRate = 1000 * 60 * 30)
    public void hotrank() {
        hotrankData(AppMarketEnum.China.getCode());
        hotrankData(AppMarketEnum.GooglePlay.getCode());
    }

    public void hotrankData(String market) {
        String rKey = "";
        if (AppMarketEnum.China.getCode().equals(market)) {
            rKey = "zh_CN_hot_";
        } else {
            rKey = "en_US_hot_";
        }
        AppEnum[] apps = AppEnum.values();
        SystemConfigEmtity systemConfig = systemConfigService.findByConfigType(SystemConfigEnum.config_hot.getType());
        JSONObject config = systemConfig.getConfig();

        for (AppEnum app : apps) {
            if (!config.containsKey(app.getCollection())) {
                continue;
            }
            JSONObject appConfig = config.getJSONObject(app.getCollection());
            JSONArray capps = appConfig.getJSONArray("apps");
            if (capps.size() == 0) {
                continue;
            }
            //按照序号取数据
            List<DBObject> pipeline = new ArrayList<DBObject>();
            pipeline.add(new BasicDBObject("$sort", new BasicDBObject("sortNu", 1)));
            List<String> hotRankstr = commonJSONService.findLists(pipeline, "hotrank");
            List<HotRankEntity> ranks = new ArrayList<HotRankEntity>();
            for (int i = 0; i < hotRankstr.size(); i++) {
                JSONObject json = JSONObject.parseObject(hotRankstr.get(i));
                HotRankEntity hot = JSONObject.toJavaObject(json, HotRankEntity.class);
                if (AppEnum.wallpaper.getCollection().equals(hot.getAppType())) { //壁纸是不区分国内和海外的
                    ranks.add(hot);
                } else if (hot.getMarket().equals(market)) {
                    ranks.add(hot);
                }
            }
            //筛选符合的
            List<List<HotRankEntity>> list = ListUtiltiy.split(ranks, 8);
            for (int m = 0; m < list.size(); m++) {
                List<HotRankEntity> pages = list.get(m);
                JSONArray arr = new JSONArray();
                for (int i = 0; i < pages.size(); i++) {
                    HotRankEntity hotrank = pages.get(i);
                    JSONObject data = JSONObject.parseObject(JSONObject.toJSONString(hotrank));
                    String appId = data.getString("elementId");
                    String appType = data.getString("appType");
                    JSONObject json = getApp(appId, appType);
                    arr.add(json);
                }
                redisService.set(rKey + app.getCollection() + "_" + (m + 1), arr.toJSONString());
            }
            log.info("redis >>> " + rKey + app.getCollection() + " cache success ~");
        }
    }

    private JSONObject getApp(String appId, String collectionName) {
        String appStr = commonJSONService.findOne(new Query(Criteria.where("elementId").is(appId)), collectionName);
        if(StringUtility.isEmpty(appStr)) {
            log.info("#######APP NOT FIND START########//n"+appId+" > "+collectionName+"//n#######APP NOT FIND END########");
        }
        JSONObject json = RedisAppUtil.getJSON(JSON.parseObject(appStr), collectionName);
        return json;
    }
}
