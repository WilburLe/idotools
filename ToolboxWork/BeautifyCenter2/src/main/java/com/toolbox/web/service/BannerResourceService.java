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
import com.toolbox.web.entity.BannerResourceEntity;

/**
* @author E-mail:86yc@sina.com
* 
*/
@Service
public class BannerResourceService extends MongoBaseDao<BannerResourceEntity> {

    @Override
    protected String getCollection() {
        return null;
    }

    @Override
    protected Class<BannerResourceEntity> getEntityClass() {
        return BannerResourceEntity.class;
    }

    public BannerResourceEntity findByElementId(String elementId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("elementId").is(elementId));
        return this.queryOne(query);
    }

    public List<BannerResourceEntity> findAll() {
        return this.queryList(null);
    }

    public List<BannerResourceEntity> findByResourceType(String resourceType) {
        Query query = new Query();
        if (StringUtility.isNotEmpty(resourceType) && !"all".equals(resourceType)) {
            query.addCriteria(Criteria.where("resourceType").is(resourceType));
        }
        query.with(new Sort(Direction.DESC, "createDate"));
        return this.queryList(query);
    }

    public void upsertResource(BannerResourceEntity resource) {
        final BasicDBObject dbDoc = new BasicDBObject();
        this.mongoTemplate.getConverter().write(resource, dbDoc);
        this.mongoTemplate.execute(BannerResourceEntity.class, new CollectionCallback<BannerResourceEntity>() {
            @Override
            public BannerResourceEntity doInCollection(DBCollection collection) throws MongoException, DataAccessException {
                Query query = new Query();
                query.addCriteria(Criteria.where("elementId").is(resource.getElementId()));
                collection.update(query.getQueryObject(), dbDoc, true, false);
                return null;
            }
        });
    }

    public void delResource(String elementId) {
        this.delete(new Query(Criteria.where("elementId").is(elementId)));
    }
}
