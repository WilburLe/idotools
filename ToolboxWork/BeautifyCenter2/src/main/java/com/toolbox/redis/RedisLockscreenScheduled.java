package com.toolbox.redis;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toolbox.common.AppEnum;
import com.toolbox.common.AppMarketEnum;
import com.toolbox.framework.utils.ListUtiltiy;
import com.toolbox.web.entity.LockscreenEntity;
import com.toolbox.web.service.CommonJSONService;
import com.toolbox.web.service.LockScreenService;
import com.toolbox.web.service.RedisService;

/**
* @author E-mail:86yc@sina.com
* 
*/
@Service
public class RedisLockscreenScheduled  {
    private final static Log  log = LogFactory.getLog(RedisLockscreenScheduled.class);
    @Autowired
    private CommonJSONService commonJSONService;
    @Autowired
    private LockScreenService lockscreenService;
    @Autowired
    private RedisService     redisService;
    
    @Scheduled(fixedRate = 1000 * 60 * 15)
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
        List<LockscreenEntity> entities = lockscreenService.findByPage(market, 0, -1);
        List<List<LockscreenEntity>> pageList = ListUtiltiy.split(entities, 8);

        //        Query query = new Query(Criteria.where("market").in(market));
        //        query.with(new Sort(Direction.DESC, "createDate"));
        //        List<String> datas = commonJSONService.findList(query, AppEnum.lockscreen.getCollection());
        //        List<List<String>> pageList = ListUtiltiy.split(datas, 8);

        for (int k = 0; k < pageList.size(); k++) {
            List<LockscreenEntity> plist = pageList.get(k);
            JSONArray data = new JSONArray();
            for (int l = 0; l < plist.size(); l++) {
                LockscreenEntity lockscreen = plist.get(l);
                JSONObject json = RedisAppUtil.getJSON(JSON.parseObject(JSON.toJSON(lockscreen).toString()), AppEnum.lockscreen.getCollection());
                data.add(json);
            }
            rKey = rKey + (k + 1);
            redisService.set(rKey, data.toJSONString());
            log.info("redis >>> " + rKey + "cache success ~");
        }

    }
}
