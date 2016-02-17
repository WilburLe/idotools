package com.toolbox.schedule;

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
import com.toolbox.redis.AbstractRedisService;
import com.toolbox.web.entity.AppTagEntity;
import com.toolbox.web.entity.WallpaperEntity;
import com.toolbox.web.service.AppTagService;
import com.toolbox.web.service.WallpaperService;

/**
* @author E-mail:86yc@sina.com
* 
*/
@Service
public class WallpaperScheduled extends AbstractRedisService<String, String> {
    private final static Log log = LogFactory.getLog(WallpaperScheduled.class);
    @Autowired
    private WallpaperService wallpaperService;
    @Autowired
    private AppTagService    appTagService;

    //壁纸
    @Scheduled(fixedRate = 1000 * 60 * 2)
    public void wallpaper() {
        //分类
        List<AppTagEntity> tags = appTagService.findTagByAppType(AppEnum.wallpaper.getCollection());
        AppTagEntity all = new AppTagEntity();
        JSONObject allName = new JSONObject();
        allName.put("cnName", "全部");
        allName.put("enName", "all");
        all.setElementId("all");
        all.setName(allName);
        tags.add(all);
        this.set("wallpaper@tags", JSON.toJSONString(tags));

        //列表
        for (int j = 0; j < tags.size(); j++) {
            AppTagEntity tag = tags.get(j);
            String uuid = tag.getElementId();

            int count = wallpaperService.count(uuid);
            List<WallpaperEntity> wallpapers = wallpaperService.findByPage(uuid, 0, count);
            List<List<WallpaperEntity>> list = ListUtiltiy.split(wallpapers, 4);
            for (int k = 0; k < list.size(); k++) {
                List<WallpaperEntity> plist = list.get(k);
                JSONArray data = new JSONArray();
                for (int l = 0; l < plist.size(); l++) {
                    WallpaperEntity wallpaper = plist.get(l);
                    data.add(wallpaper);
                }
                this.set("wallpaper@" + uuid + "@p" + k, data.toJSONString());
            }
        }
        log.info("redis >>> wallpaper cache success ~");
    }
    //热门
    @Scheduled(fixedRate = 1000 * 60 * 3)
    public void hotrank() {

        log.info("redis >>> hotrank cache success ~");
    }
}
