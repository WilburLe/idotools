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
import com.toolbox.common.AppMarketEnum;
import com.toolbox.framework.utils.ListUtiltiy;
import com.toolbox.web.service.CommonJSONService;

/**
* @author E-mail:86yc@sina.com
* 
*/
@Service
public class RedisLockscreenScheduled extends AbstractRedisService<String, String> {
    private final static Log  log = LogFactory.getLog(RedisLockscreenScheduled.class);
    @Autowired
    private CommonJSONService commonJSONService;

    @Scheduled(fixedRate = 1000 * 60 * 5)
    public void lockscenery() {
        dataRadis(AppMarketEnum.China.getCode());
        dataRadis(AppMarketEnum.GooglePlay.getCode());
    }

    public void dataRadis(String market) {
        String rKey = "";
        if (AppMarketEnum.China.getCode().equals(market)) {
            rKey = "zh_CN_lockscreen_";
        } else {
            rKey = "en_US_lockscreen_";
        }
        Query query = new Query(Criteria.where("market").in(market));
        List<String> datas = commonJSONService.findList(query, AppEnum.lockscreen.getCollection());
        List<List<String>> pageList = ListUtiltiy.split(datas, 8);

        for (int k = 0; k < pageList.size(); k++) {
            List<String> plist = pageList.get(k);
            JSONArray data = new JSONArray();
            for (int l = 0; l < plist.size(); l++) {
                String lockscreen = plist.get(l);
                JSONObject json = RedisAppUtil.getJSON(JSON.parseObject(lockscreen), AppEnum.lockscreen.getCollection());
                data.add(json);
            }
            this.set(rKey + (k + 1), data.toJSONString());
            log.info("redis >>> " + rKey + "cache success ~");

        }

    }
}
