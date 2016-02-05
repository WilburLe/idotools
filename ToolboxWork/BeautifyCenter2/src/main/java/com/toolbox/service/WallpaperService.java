package com.toolbox.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.toolbox.entity.WallpaperEntity;
import com.toolbox.framework.spring.mongo.MongoBaseDao;
import com.toolbox.framework.utils.StringUtility;

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

    public List<WallpaperEntity> findByPage(String tag, int start, int size) {
        Query query = new Query();
        if (StringUtility.isNotEmpty(tag) && !"all".equals(tag)) {
            Criteria criteria = Criteria.where("tags").in(tag);
            query.addCriteria(criteria);
        }
        query.with(new Sort(Direction.DESC, "createDate"));
        return this.getPage(query, start * size, size);
    }

}
