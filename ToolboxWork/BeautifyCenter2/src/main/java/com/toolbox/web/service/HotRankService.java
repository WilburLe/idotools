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
import com.toolbox.common.SystemConfigEnum;
import com.toolbox.framework.spring.mongo.MongoBaseDao;
import com.toolbox.framework.utils.StringUtility;
import com.toolbox.web.entity.HotRankEntity;
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

    public List<HotRankEntity> findAllByAppType(String appType) {
        Query query = new Query();
        if (StringUtility.isNotEmpty(appType) && !"all".equals(appType)) {
            Criteria criteria = Criteria.where("appType").is(appType);
            query.addCriteria(criteria);
        }
        query.with(new Sort(Direction.ASC, "sortNu"));
        return this.queryList(query);
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
    }

    /**
     * 壁纸的热门
     *
     */
    public void resetWallpaperHotRank() {
        String appType = AppEnum.wallpaper.getCollection();
        //删除原有的
        this.delete(new Query(Criteria.where("appType").is(appType)));
        //获取配置
        SystemConfigEmtity config = configService.findByConfigType(SystemConfigEnum.config_hot.getType());
        JSONObject hcj = config.getConfig();
        JSONObject appCon = hcj.getJSONObject(appType);
        int nu = appCon.getIntValue("nu");
        //获取下载量最多的壁纸
        List<WallpaperEntity> wallpapers = wallpaperService.findOrderByDownload(nu, "actionCount.china");
        for (int i = 0; i < wallpapers.size(); i++) {
            WallpaperEntity wallpaper = wallpapers.get(i);
            HotRankEntity hotRank = new HotRankEntity();
            hotRank.setElementId(wallpaper.getElementId());
            hotRank.setPreviewImageUrl(wallpaper.getPreviewImageUrl());
            hotRank.setAppType(appType);
            hotRank.setAppTags(wallpaper.getTags());
            hotRank.setActionCount((wallpaper.getActionCount()));
            hotRank.setSortNu(i);

            this.save(hotRank);
        }

    }
}
