package com.toolbox.web.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.toolbox.common.AppEnum;
import com.toolbox.common.AppMarketEnum;
import com.toolbox.common.SystemConfigEnum;
import com.toolbox.framework.spring.mongo.MongoBaseDao;
import com.toolbox.framework.utils.StringUtility;
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
    @Autowired
    private WallpaperService    wallpaperService;
    @Autowired
    private SystemConfigService configService;
    @Autowired
    private LockScreenService   lockSceneryService;

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

    /**
     * 热门
     * @param appType
     *
     */
    public void resetHotRank(String appType) {
        if (AppEnum.wallpaper.getCollection().equals(appType)) {
            resetWallpaperHotRank();
        }
        if (AppEnum.lockscreen.getCollection().equals(appType)) {
            resetLocksceneryHotRank();
        }

        //重新按照下载量排序
        List<HotRankEntity> allRanks = this.queryList(new Query());
        //
    }

    /**
     * 壁纸的热门
     *
     */
    private void resetWallpaperHotRank() {
        String appType = AppEnum.wallpaper.getCollection();
        //删除原有的
        this.delete(new Query(Criteria.where("appType").is(appType)));
        //获取配置
        SystemConfigEmtity config = configService.findByConfigType(SystemConfigEnum.config_hot.getType());
        JSONObject hcj = config.getConfig();
        JSONObject appCon = hcj.getJSONObject(appType);
        int nu = appCon.getIntValue("nu");
        //获取下载量最多的壁纸
        List<WallpaperEntity> wallpapers = wallpaperService.findOrderByDownload(nu, "actionCount." + AppMarketEnum.China.getCode());
        for (int i = 0; i < wallpapers.size(); i++) {
            WallpaperEntity wallpaper = wallpapers.get(i);
            HotRankEntity hotRank = new HotRankEntity();
            hotRank.setElementId(wallpaper.getElementId());
            hotRank.setPreviewImageUrl(wallpaper.getPreviewImageUrl());
            hotRank.setAppType(appType);
            hotRank.setAppTags(wallpaper.getTags());
            hotRank.setActionCount((wallpaper.getActionCount()));
            hotRank.setSortNu(i);
            hotRank.setMarket(AppMarketEnum.China.getCode());
            this.save(hotRank);
        }

    }

    /**
     * 锁屏的热门
     */
    private void resetLocksceneryHotRank() {
        String appType = AppEnum.lockscreen.getCollection();
        //删除原有的
        this.delete(new Query(Criteria.where("appType").is(appType)));
        //获取配置
        SystemConfigEmtity config = configService.findByConfigType(SystemConfigEnum.config_hot.getType());
        JSONObject hcj = config.getConfig();
        JSONObject appCon = hcj.getJSONObject(appType);
        int nu = appCon.getIntValue("nu");
        //China
        List<LockscreenEntity> lockscenerys_china = lockSceneryService.findOrderByDownload(nu, "actionCount." + AppMarketEnum.China.getCode());
        for (int i = 0; i < lockscenerys_china.size(); i++) {
            LockscreenEntity lockscenery = lockscenerys_china.get(i);
            boolean cs = false;
            String[] mas = lockscenery.getMarket();
            for (String ms : mas) {
                if (AppMarketEnum.China.getCode().equals(ms)) {
                    cs = true;
                    break;
                }
            }
            if (!cs) {
                continue;
            }
            HotRankEntity hotRank = new HotRankEntity();
            hotRank.setElementId(lockscenery.getElementId());
            hotRank.setPreviewImageUrl(lockscenery.getPreviewImageUrl());
            hotRank.setAppType(appType);
            hotRank.setAppTags(lockscenery.getTags());
            hotRank.setActionCount((lockscenery.getActionCount()));
            hotRank.setSortNu(i);
            hotRank.setMarket(AppMarketEnum.China.getCode());
            this.save(hotRank);
        }
        //GooglePlay
        List<LockscreenEntity> lockscenerys_google = lockSceneryService.findOrderByDownload(nu, "actionCount." + AppMarketEnum.GooglePlay.getCode());
        for (int i = 0; i < lockscenerys_google.size(); i++) {
            LockscreenEntity lockscenery = lockscenerys_google.get(i);
            boolean cs = false;
            String[] mas = lockscenery.getMarket();
            for (String ms : mas) {
                if (AppMarketEnum.GooglePlay.getCode().equals(ms)) {
                    cs = true;
                    break;
                }
            }
            if (!cs) {
                continue;
            }

            HotRankEntity hotRank = new HotRankEntity();
            hotRank.setElementId(lockscenery.getElementId());
            hotRank.setPreviewImageUrl(lockscenery.getPreviewImageUrl());
            hotRank.setAppType(appType);
            hotRank.setAppTags(lockscenery.getTags());
            hotRank.setActionCount((lockscenery.getActionCount()));
            hotRank.setSortNu(i);
            hotRank.setMarket(AppMarketEnum.GooglePlay.getCode());
            this.save(hotRank);
        }
    }

}
