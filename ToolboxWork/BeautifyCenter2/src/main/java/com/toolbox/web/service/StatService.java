package com.toolbox.web.service;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.toolbox.common.AppEnum;
import com.toolbox.common.LanguageEnum;
import com.toolbox.common.StatKeyEnum;
import com.toolbox.framework.utils.StringUtility;
import com.toolbox.redis.tools.RedisPoolFactory;

/**
* @author E-mail:86yc@sina.com
* 
*/
@Service("StatService")
public class StatService {
    private static Log        log = LogFactory.getLog(StatService.class);
    @Autowired
    private CommonJSONService commonJSONService;
    @Autowired
    private RedisService      redisService;

    private final static int dbIndex = 5;

    public void statWallpaper() {
        statWallpaper(RedisPoolFactory.poolName.china.name());
        statWallpaper(RedisPoolFactory.poolName.abroad.name());
    }

    public void statLockscreen() {
        statLockscreen(RedisPoolFactory.poolName.china.name());
        statLockscreen(RedisPoolFactory.poolName.abroad.name());
    }

    public void statWallpaper(String poolName) {
        String pattern = StatKeyEnum.wallpaper.getStatCode() + "*";
        List<String> keys = RedisPoolFactory.getInstance().keys(poolName, pattern, dbIndex);

        for (String key : keys) {
            String count = redisService.get(key, dbIndex);
            String elementId = key.replace(pattern, "");
            String data = commonJSONService.findOne(new Query(Criteria.where("elementId").is(elementId)), AppEnum.wallpaper.getCollection());
            if (StringUtility.isEmpty(data)) {
                continue;
            }
            JSONObject redisData = JSONObject.parseObject(count);
            int cnCount = redisData.containsKey(LanguageEnum.zh_CN.getCode()) ? redisData.getIntValue(LanguageEnum.zh_CN.getCode()) : 0;
            int enCount = redisData.containsKey(LanguageEnum.en_US.getCode()) ? redisData.getIntValue(LanguageEnum.en_US.getCode()) : 0;

            JSONObject json = JSONObject.parseObject(data);
            JSONObject actionCount = json.getJSONObject("actionCount");
            int china = actionCount.containsKey("china") ? actionCount.getIntValue("china") : 0;
            int googlePlay = actionCount.containsKey("googlePlay") ? actionCount.getIntValue("googlePlay") : 0;
            actionCount.put("china", china + cnCount);
            actionCount.put("googlePlay", googlePlay + enCount);
            commonJSONService.updateMulti(AppEnum.wallpaper.getCollection(), //
                    new Query(Criteria.where("elementId").is(elementId)), // 
                    new Update().set("actionCount", actionCount));

            redisData.put(LanguageEnum.zh_CN.getCode(), 0);
            redisData.put(LanguageEnum.en_US.getCode(), 0);
            redisService.set(key, redisData.toJSONString(), dbIndex);
        }

    }

    public void statLockscreen(String poolName) {
        String pattern = StatKeyEnum.lockscreen.getStatCode() + "*";
        List<String> keys = RedisPoolFactory.getInstance().keys(poolName, pattern, dbIndex);

        for (String key : keys) {
            String packageName = key.replace(pattern, "").replaceAll("_", ".");
            String count = redisService.get(key, dbIndex);
            String data = commonJSONService.findOne(new Query(Criteria.where("packageName").is(packageName)), AppEnum.lockscreen.getCollection());
            if (StringUtility.isEmpty(data)) {
                continue;
            }
            JSONObject redisData = JSONObject.parseObject(count);
            int cnCount = redisData.containsKey(LanguageEnum.zh_CN.getCode()) ? redisData.getIntValue(LanguageEnum.zh_CN.getCode()) : 0;
            int enCount = redisData.containsKey(LanguageEnum.en_US.getCode()) ? redisData.getIntValue(LanguageEnum.en_US.getCode()) : 0;

            JSONObject json = JSONObject.parseObject(data);
            JSONObject actionCount = json.getJSONObject("actionCount");
            int china = actionCount.containsKey("china") ? actionCount.getIntValue("china") : 0;
            int googlePlay = actionCount.containsKey("googlePlay") ? actionCount.getIntValue("googlePlay") : 0;
            actionCount.put("china", china + cnCount);
            actionCount.put("googlePlay", googlePlay + enCount);
            commonJSONService.updateMulti(AppEnum.lockscreen.getCollection(), //
                    new Query(Criteria.where("packageName").is(packageName)), // 
                    new Update().set("actionCount", actionCount));

            redisData.put(LanguageEnum.zh_CN.getCode(), 0);
            redisData.put(LanguageEnum.en_US.getCode(), 0);
            redisService.set(key, redisData.toJSONString(), dbIndex);
        }
    }

}
