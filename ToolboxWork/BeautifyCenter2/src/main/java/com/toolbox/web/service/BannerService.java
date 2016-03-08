package com.toolbox.web.service;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.CollectionCallback;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.MongoException;
import com.toolbox.framework.spring.mongo.MongoBaseDao;
import com.toolbox.framework.utils.StringUtility;
import com.toolbox.web.entity.BannerEntity;

/**
* @author E-mail:86yc@sina.com
* 
*/
@Service
public class BannerService extends MongoBaseDao<BannerEntity> {

    @Override
    protected String getCollection() {
        return null;
    }

    @Override
    protected Class<BannerEntity> getEntityClass() {
        return BannerEntity.class;
    }

    public BannerEntity findByElementId(String elementId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("elementId").is(elementId));
        return this.queryOne(query);
    }

    public List<BannerEntity> findAll() {
        return this.queryList(null);
    }

    public List<BannerEntity> findByBannerType(String bannerType) {
        Query query = new Query();
        if (StringUtility.isNotEmpty(bannerType) && !"all".equals(bannerType)) {
            query.addCriteria(Criteria.where("bannerType").is(bannerType));
        }
        query.with(new Sort(Direction.DESC, "createDate"));
        return this.queryList(query);
    }

    public void upsertBanner(BannerEntity banner) {
        final BasicDBObject dbDoc = new BasicDBObject();
        this.mongoTemplate.getConverter().write(banner, dbDoc);
        this.mongoTemplate.execute(BannerEntity.class, new CollectionCallback<BannerEntity>() {
            @Override
            public BannerEntity doInCollection(DBCollection collection) throws MongoException, DataAccessException {
                Query query = new Query();
                query.addCriteria(Criteria.where("elementId").is(banner.getElementId()));
                collection.update(query.getQueryObject(), dbDoc, true, false);
                return null;
            }
        });
    }

    public void delBanner(String elementId) {
        this.delete(new Query(Criteria.where("elementId").is(elementId)));
    }

}
