    package com.toolbox.redis;

import com.alibaba.fastjson.JSONObject;
import com.toolbox.common.AppEnum;
import com.toolbox.common.AppMarketEnum;
import com.toolbox.web.entity.LockscreenEntity;
import com.toolbox.web.entity.WallpaperEntity;

/**
* @author E-mail:86yc@sina.com
* 
*/
public class RedisAppUtil {

    public static JSONObject getLockscreen(LockscreenEntity app) {
        return getJSON(JSONObject.parseObject(JSONObject.toJSONString(app)), AppEnum.lockscreen.getCollection());
    }

    public static JSONObject getWallpaper(WallpaperEntity app) {
        return getJSON(JSONObject.parseObject(JSONObject.toJSONString(app)), AppEnum.wallpaper.getCollection());
    }

    public static JSONObject getJSON(JSONObject json, String appType) {
        JSONObject actionCount = json.containsKey("actionCount") ? json.getJSONObject("actionCount") : new JSONObject();
        JSONObject ac = new JSONObject();
        ac.put("zh_CN", actionCount.getIntValue(AppMarketEnum.China.getCode()));
        ac.put("en_US", actionCount.getIntValue(AppMarketEnum.GooglePlay.getCode()));
        json.put("actionCount", ac);
        if (AppEnum.wallpaper.getCollection().equals(appType)) {
            json.put("type", "idotools_wallpaper");
        } else {
            json.put("elementId", json.getString("packageName"));
            json.put("type", "idotools_" + appType + "Theme");
        }
        json.remove("_id");
        json.remove("_class");
        json.remove("createDate");
        json.remove("market");
        json.remove("description");
        json.remove("tags");
        json.remove("src");
        return json;
    }

}
