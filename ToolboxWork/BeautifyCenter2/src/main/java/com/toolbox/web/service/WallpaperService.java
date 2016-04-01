package com.toolbox.web.service;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.toolbox.framework.spring.mongo.MongoBaseDao;
import com.toolbox.framework.utils.StringUtility;
import com.toolbox.web.entity.WallpaperEntity;

/**
* @author E-mail:86yc@sina.com
* 
*/
@Service
public class WallpaperService extends MongoBaseDao<WallpaperEntity> {
    @Override
    protected String getCollection() {
        return null;
    }

    @Override
    protected Class<WallpaperEntity> getEntityClass() {
        return WallpaperEntity.class;
    }

    public WallpaperEntity findByElementId(String elementId) {
        return this.queryOne(new Query(Criteria.where("elementId").is(elementId)));
    }

    public int count(String tag) {
        Query query = new Query();
        if (StringUtility.isNotEmpty(tag) && !"all".equals(tag)) {
            Criteria criteria = Criteria.where("tags").is(tag);
            query.addCriteria(criteria);
        }
        Long count = this.getPageCount(query);
        return count != null ? count.intValue() : 0;
    }

    public List<WallpaperEntity> findByPage(String tag, int start, int size) {
        Query query = new Query();
        if (StringUtility.isNotEmpty(tag) && !"all".equals(tag)) {
            Criteria criteria = Criteria.where("tags").is(tag);
            query.addCriteria(criteria);
        }
        query.with(new Sort(Direction.DESC, "createDate"));
        return this.getPage(query, start * size, size);
    }

    public void saveList(List<WallpaperEntity> wallpapers) {
        for (WallpaperEntity wallpaper : wallpapers) {
            this.save(wallpaper);
        }
    }

    public void updateTags(String elementId, String tags) {
        if (StringUtility.isEmpty(tags)) {
            tags = "[]";
        }
        Update update = new Update();
        update.set("tags", JSONArray.parseArray(tags));
        this.updateFirst(new Query(Criteria.where("elementId").is(elementId)), update);
    }

    public void deleteByElementId(String elementId) {
        this.delete(new Query(Criteria.where("elementId").is(elementId)));
    }

    /**
     * 
     * @param size
     * @param downloadSource 
     *          1 actionCount.china
     *          2 actionCount.google
     * @return
     *
     */
    public List<WallpaperEntity> findsShort(String filed, int size, boolean desc) {
        Query query = new Query();
        //        query.addCriteria(Criteria.where("tags.0").exists(false));
        query.with(new Sort(new Order(desc ? Direction.DESC : Direction.ASC, filed)));
        return this.getPage(query, 0, size);
    }
}
