package com.toolbox.web.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.toolbox.common.AppEnum;
import com.toolbox.common.AppMarketEnum;
import com.toolbox.common.SystemConfigEnum;
import com.toolbox.common.TabEnum;
import com.toolbox.framework.spring.mongo.MongoBaseDao;
import com.toolbox.framework.utils.ListUtiltiy;
import com.toolbox.framework.utils.StringUtility;
import com.toolbox.redis.RedisAppUtil;
import com.toolbox.web.entity.HotRankEntity;
import com.toolbox.web.entity.LockscreenEntity;
import com.toolbox.web.entity.SystemConfigEmtity;
import com.toolbox.web.entity.WallpaperEntity;

/**
* @author E-mail:86yc@sina.com
* 
*/
@Service("HotRankService")
public class HotRankService extends MongoBaseDao<HotRankEntity> {
    private final static Log log = LogFactory.getLog(HotRankService.class);

    @Autowired
    private WallpaperService    wallpaperService;
    @Autowired
    private SystemConfigService systemConfigService;
    @Autowired
    private LockScreenService   lockSceneryService;
    @Autowired
    private RedisService        redisService;
    @Autowired
    private CommonJSONService   commonJSONService;

    @Override
    protected String getCollection() {
        return null;
    }

    @Override
    protected Class<HotRankEntity> getEntityClass() {
        return HotRankEntity.class;
    }

    public int count(String appType) {
        Query query = new Query();
        if (StringUtility.isNotEmpty(appType) && !"all".equals(appType)) {
            Criteria criteria = Criteria.where("appType").is(appType);
            query.addCriteria(criteria);
        }
        Long count = this.getPageCount(query);
        return count != null ? count.intValue() : 0;
    }

    public List<HotRankEntity> findAllByAppType(String market, String appType) {
        Query query = new Query();
        if (StringUtility.isNotEmpty(appType) && !"all".equals(appType)) {
            Criteria criteria = Criteria.where("appType").is(appType);
            query.addCriteria(criteria);
        }
        if (StringUtility.isNotEmpty(market) && !"all".equals(market)) {
            Criteria criteria = Criteria.where("market").is(market);
            query.addCriteria(criteria);
        }
        query.with(new Sort(Direction.ASC, "sortNu"));
        return this.queryList(query);
    }

    public HotRankEntity findByElementId(String elementId) {
        return this.queryOne(new Query(Criteria.where("elementId").is(elementId)));
    }

    /////////////////////////// HOT RANK DB DATA START /////////////////////////////////////////////
    /**
     * 热门
     * @param appType
     *
     */
    public void hot2DB() {
        resetWallpaperHotRank();
        resetLocksceneryHotRank();
        //重新按照下载量排序
        hotRankAgain(AppMarketEnum.China.getCode());
        hotRankAgain(AppMarketEnum.GooglePlay.getCode());
    }

    private void hotRankAgain(String market) {
        Query query = new Query();
        query.addCriteria(Criteria.where("market").is(market));
        query.with(new Sort(new Order(Direction.DESC, "weight")));

        List<HotRankEntity> allRanks = this.getPage(query, 0, 100);
        this.delete(new Query(Criteria.where("market").is(market)));

        for (int i = 0; i < allRanks.size(); i++) {
            HotRankEntity hotRank = allRanks.get(i);
            hotRank.setSortNu(i);
            this.save(hotRank);
        }
    }

    /**
     * 壁纸的热门
     *
     */
    private void resetWallpaperHotRank() {
        String appType = AppEnum.wallpaper.getCollection();
        //删除原有的
        this.delete(new Query(Criteria.where("appType").is(appType)));
        int nu = 200;
        //获取下载量最多的壁纸
        List<WallpaperEntity> wallpapers = wallpaperService.findsShort("actionCount." + AppMarketEnum.China.getCode(), nu, true);

        List<WallpaperEntity> dateDesc = wallpaperService.findsShort("createDate", nu, true);
        wallpapers.addAll(dateDesc);

        for (int i = 0; i < wallpapers.size(); i++) {
            WallpaperEntity wallpaper = wallpapers.get(i);
            //权重值计算  下载量X时间流失
            long createdate = wallpaper.getCreateDate();
            long nodate = System.currentTimeMillis();
            int days = (int) ((nodate - createdate) / 1000 / 60 / 60 / 24);
            int d = 100 - days;
            d = d < 30 ? 30 : d;
            int dowanload = wallpaper.getActionCount().getChina();
            dowanload = dowanload <= 0 ? 1 : dowanload;
            float weight = dowanload * d / 100f;

            HotRankEntity hotRank = new HotRankEntity();
            hotRank.setElementId(wallpaper.getElementId());
            hotRank.setPreviewImageUrl(wallpaper.getPreviewImageUrl());
            hotRank.setAppType(appType);
            hotRank.setAppTags(wallpaper.getTags());
            hotRank.setActionCount((wallpaper.getActionCount()));
            hotRank.setWeight(weight);
            hotRank.setMarket(AppMarketEnum.China.getCode());
            this.save(hotRank);
            //GooglePlay 也要保存一份
            HotRankEntity hotRanken = new HotRankEntity();
            hotRanken.setElementId(wallpaper.getElementId());
            hotRanken.setPreviewImageUrl(wallpaper.getPreviewImageUrl());
            hotRanken.setAppType(appType);
            hotRanken.setAppTags(wallpaper.getTags());
            hotRanken.setActionCount((wallpaper.getActionCount()));
            hotRank.setWeight(weight);
            hotRanken.setMarket(AppMarketEnum.GooglePlay.getCode());
            this.save(hotRanken);
        }

    }

    /**
     * 锁屏的热门
     */
    private void resetLocksceneryHotRank() {
        String appType = AppEnum.lockscreen.getCollection();
        //删除原有的
        this.delete(new Query(Criteria.where("appType").is(appType)));
        int nu = 200;
        //China
        List<LockscreenEntity> lockscenerys_china = lockSceneryService.findsShort(AppMarketEnum.China.getCode(), "actionCount." + AppMarketEnum.China.getCode(), nu, true);
        for (int i = 0; i < lockscenerys_china.size(); i++) {
            LockscreenEntity lockscenery = lockscenerys_china.get(i);
            //权重值计算  下载量X时间流失
            long createdate = lockscenery.getCreateDate();
            long nodate = System.currentTimeMillis();
            int days = (int) ((nodate - createdate) / 1000 / 60 / 60 / 24);
            int d = 100 - days;
            d = d < 30 ? 30 : d;
            int dowanload = lockscenery.getActionCount().getChina();
            dowanload = dowanload <= 0 ? 1 : dowanload;
            float weight = dowanload * d / 100f;

            HotRankEntity hotRank = new HotRankEntity();
            hotRank.setElementId(lockscenery.getElementId());
            hotRank.setPreviewImageUrl(lockscenery.getPreviewImageUrl());
            hotRank.setAppType(appType);
            hotRank.setAppTags(lockscenery.getTags());
            hotRank.setActionCount((lockscenery.getActionCount()));
            hotRank.setWeight(weight);
            hotRank.setMarket(AppMarketEnum.China.getCode());
            this.save(hotRank);
        }
        //GooglePlay
        List<LockscreenEntity> lockscenerys_google = lockSceneryService.findsShort(AppMarketEnum.GooglePlay.getCode(), "actionCount." + AppMarketEnum.GooglePlay.getCode(), nu, true);
        for (int i = 0; i < lockscenerys_google.size(); i++) {
            LockscreenEntity lockscenery = lockscenerys_google.get(i);
            //权重值计算  下载量X时间流失
            long createdate = lockscenery.getCreateDate();
            long nodate = System.currentTimeMillis();
            int days = (int) ((nodate - createdate) / 1000 / 60 / 60 / 24);
            int d = 100 - days;
            d = d < 30 ? 30 : d;
            int dowanload = lockscenery.getActionCount().getChina();
            dowanload = dowanload <= 0 ? 1 : dowanload;
            float weight = dowanload * d / 100f;

            HotRankEntity hotRank = new HotRankEntity();
            hotRank.setElementId(lockscenery.getElementId());
            hotRank.setPreviewImageUrl(lockscenery.getPreviewImageUrl());
            hotRank.setAppType(appType);
            hotRank.setAppTags(lockscenery.getTags());
            hotRank.setActionCount((lockscenery.getActionCount()));
            hotRank.setWeight(weight);
            hotRank.setMarket(AppMarketEnum.GooglePlay.getCode());
            this.save(hotRank);
        }
    }
    /////////////////////////// HOT RANK DB DATA END /////////////////////////////////////////////

    ///////////////////////////////// HOT 2 REDIS START/////////////////////////////////////////////
    public void hot2redis() {
        hotrankData(AppMarketEnum.China.getCode());
        hotrankData(AppMarketEnum.GooglePlay.getCode());
    }

    private void hotrankData(String market) {
        String rKey = "";
        if (AppMarketEnum.China.getCode().equals(market)) {
            rKey = "zh_CN_hot_";
        } else {
            rKey = "en_US_hot_";
        }
        SystemConfigEmtity systemConfig = systemConfigService.findByConfigType(SystemConfigEnum.config_hot.getType());
        JSONObject config = systemConfig.getConfig();
        JSONObject appConfig = config.getJSONObject("appConfig");

        for (TabEnum tab : TabEnum.values()) {
            if (!appConfig.containsKey(tab.getCollection())) {
                continue;
            }
            JSONObject appCg = appConfig.getJSONObject(tab.getCollection());
            JSONArray capps = appCg.getJSONArray("apps");
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
                if (hot.getMarket().equals(market)) {
                    ranks.add(hot);
                }
            }
            //分页后放入redis
            List<List<HotRankEntity>> pageList = ListUtiltiy.split(ranks, 8);
            for (int m = 0; m < pageList.size(); m++) {
                List<HotRankEntity> pages = pageList.get(m);
                JSONArray arr = new JSONArray();
                for (int i = 0; i < pages.size(); i++) {
                    HotRankEntity hotrank = pages.get(i);
                    JSONObject data = JSONObject.parseObject(JSONObject.toJSONString(hotrank));
                    String appId = data.getString("elementId");
                    String appType = data.getString("appType");
                    JSONObject json = getApp(appId, appType);
                    arr.add(json);
                }
                redisService.set(rKey + tab.getCollection() + "_" + (m + 1), arr.toJSONString());
            }
            log.info("redis >>> " + rKey + tab.getCollection() + " cache success ~");
        }
    }

    private JSONObject getApp(String appId, String collectionName) {
        String appStr = commonJSONService.findOne(new Query(Criteria.where("elementId").is(appId)), collectionName);
        if (StringUtility.isEmpty(appStr)) {
            log.info("#######APP NOT FIND START########//n" + appId + " > " + collectionName + "//n#######APP NOT FIND END########");
        }
        JSONObject json = RedisAppUtil.getJSON(JSON.parseObject(appStr), collectionName);
        return json;
    }
    /////////////////////////////// HOT 2 REDIS END/////////////////////////////////////////////
}
