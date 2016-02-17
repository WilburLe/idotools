package com.toolbox.redis;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toolbox.common.CollectionEnum;
import com.toolbox.framework.utils.ListUtiltiy;
import com.toolbox.service.TagEditService;
import com.toolbox.service.WallpaperService;

/**
* @author E-mail:86yc@sina.com
* 
*/
@Service
public class WallpaperCache extends AbstractRedisService<String, String> {
    private final static Log log = LogFactory.getLog(WallpaperCache.class);
    @Autowired
    private WallpaperService wallpaperService;
    @Autowired
    private TagEditService   tagEditService;

    //壁纸
//    @Scheduled(fixedRate = 1000 * 60 * 2)
    public void wallpaper() {
        //分类
        JSONObject wallpapertags = tagEditService.findTagByName(CollectionEnum.wallpaper.getCollection());
        JSONArray tags = wallpapertags.getJSONArray("arr");
        JSONObject all = new JSONObject();
        all.put("uuid", "all");
        all.put("cnName", "全部");
        all.put("enName", "all");
        tags.add(all);
        this.set("wallpaper@tags", tags.toJSONString());
        //列表
        for (int i = 0; i < wallpapertags.size(); i++) {
            for (int j = 0; j < tags.size(); j++) {
                JSONObject tag = tags.getJSONObject(j);
                String uuid = tag.getString("uuid");

                int count = wallpaperService.count(uuid);
                List<JSONObject> wallpapers = wallpaperService.findByPage(uuid, 0, count);
                List<List<JSONObject>> list = ListUtiltiy.split(wallpapers, 4);
                for (int k = 0; k < list.size(); k++) {
                    List<JSONObject> plist = list.get(k);
                    JSONArray data = new JSONArray();
                    for (int l = 0; l < plist.size(); l++) {
                        JSONObject wallpaper = plist.get(l);
                        wallpaper.remove("_id");
                        wallpaper.remove("_class");
                        wallpaper.remove("tags");
                        data.add(wallpaper);
                    }
                    this.set("wallpaper@" + uuid + "@p" + k, data.toJSONString());
                }
            }
        }
        log.info("redis >>> wallpaper cache success ~");
    }
}
