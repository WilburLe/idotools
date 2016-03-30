package com.toolbox.redis;

import java.util.ArrayList;
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
import com.toolbox.framework.utils.ListUtiltiy;
import com.toolbox.web.entity.AppTagEntity;
import com.toolbox.web.entity.WallpaperEntity;
import com.toolbox.web.service.AppTagService;
import com.toolbox.web.service.RedisService;
import com.toolbox.web.service.WallpaperService;

/**
* @author E-mail:86yc@sina.com
* 
*/
@Service
public class RedisWallpaperScheduled {
    private final static Log log = LogFactory.getLog(RedisWallpaperScheduled.class);
    @Autowired
    private WallpaperService wallpaperService;
    @Autowired
    private AppTagService    appTagService;
    @Autowired
    private RedisService     redisService;

    //壁纸
    @Scheduled(fixedRate = 1000 * 60 * 15)
    public void wallpaper() {
        //分类
        List<AppTagEntity> tags = new ArrayList<AppTagEntity>();
        AppTagEntity all = new AppTagEntity();
        JSONObject allName = new JSONObject();
        allName.put("zh_CN", "全部");
        allName.put("en_US", "All");
        all.setElementId("all");
        all.setName(allName);
        tags.add(all);
        tags.addAll(appTagService.findTagByAppType(AppEnum.wallpaper.getCollection(), 0));

        redisService.set("zh_CN_wallpaper_tags", JSON.toJSONString(tags));

        //列表
        for (int j = 0; j < tags.size(); j++) {
            AppTagEntity tag = tags.get(j);
            String uuid = tag.getElementId();

            int count = wallpaperService.count(uuid);
            List<WallpaperEntity> wallpapers = wallpaperService.findByPage(uuid, 0, count);
            List<List<WallpaperEntity>> pageList = ListUtiltiy.split(wallpapers, 8);
            for (int k = 0; k < pageList.size(); k++) {
                List<WallpaperEntity> plist = pageList.get(k);
                JSONArray data = new JSONArray();
                for (int l = 0; l < plist.size(); l++) {
                    WallpaperEntity wallpaper = plist.get(l);
                    data.add(RedisAppUtil.getWallpaper(wallpaper));
                }
                redisService.set("zh_CN_wallpaper_" + uuid + "_" + (k + 1), data.toJSONString());
            }
        }
        log.info("redis >>> wallpaper cache success ~");
    }

}
