package com.toolbox.redis;

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
import com.toolbox.common.AppEnum;
import com.toolbox.framework.utils.ListUtiltiy;
import com.toolbox.web.entity.HotRankEntity;
import com.toolbox.web.service.CommonJSONService;
import com.toolbox.web.service.HotRankService;

/**
* @author E-mail:86yc@sina.com
* 
*/
@Service
public class RedisHotRankScheduled extends AbstractRedisService<String, String> {
    private final static Log  log = LogFactory.getLog(RedisHotRankScheduled.class);
    @Autowired
    private HotRankService    hotRankService;
    @Autowired
    private CommonJSONService commonJSONService;

//    @Scheduled(fixedRate = 1000 * 60 * 5)
    public void hotrank() {
        AppEnum[] apps = AppEnum.values();
        for (AppEnum app : apps) {
            List<HotRankEntity> hotRanks = hotRankService.findAllByAppType(app.getCollection());

            List<List<HotRankEntity>> list = ListUtiltiy.split(hotRanks, 8);
            for (int m = 0; m < list.size(); m++) {
                List<HotRankEntity> pages = list.get(m);

                JSONArray arr = new JSONArray();
                for (int i = 0; i < pages.size(); i++) {
                    HotRankEntity hotrank = pages.get(i);
                    String appId = hotrank.getElementId();
                    String appType = hotrank.getAppType();
                    JSONObject json = getApp(appId, appType);
                    arr.add(json);
                }
                this.set("zh_CN_hot_" + app.getCollection() + "_" + m, arr.toJSONString());
            }
        }
        log.info("redis >>> hotrank cache success ~");
    }

    private JSONObject getApp(String appId, String collectionName) {
        String str = commonJSONService.findOne(new Query(Criteria.where("elementId").is(appId)), collectionName);
        JSONObject json = JSON.parseObject(str);
        json.put("type", "idotools_wallpaper");
        return json;
    }
}
