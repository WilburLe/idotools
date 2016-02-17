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
import com.toolbox.web.entity.AppTagEntity;

/**
* @author E-mail:86yc@sina.com
* 
*/
@Service
public class AppTagService extends MongoBaseDao<AppTagEntity> {

    @Override
    protected String getCollection() {
        return null;
    }

    @Override
    protected Class<AppTagEntity> getEntityClass() {
        return AppTagEntity.class;
    }

    public List<AppTagEntity> findAllTag() {
        return this.queryList(null);
    }

    public List<AppTagEntity> findTagByAppType(String appType) {
        Query query = new Query();
        query.addCriteria(Criteria.where("appType").is(appType));
        query.with(new Sort(Direction.ASC, "sortNu"));
        return this.queryList(query);
    }

//    public void upsertTag(AppTagEntity apptag) {
//        final BasicDBObject dbDoc = new BasicDBObject();
//        this.mongoTemplate.getConverter().write(apptag, dbDoc);
//        this.mongoTemplate.execute(AppTagEntity.class, new CollectionCallback<AppTagEntity>() {
//            @Override
//            public AppTagEntity doInCollection(DBCollection collection) throws MongoException, DataAccessException {
//                Query query = new Query();
//                query.addCriteria(Criteria.where("elementId").is(apptag.getElementId()));
//                collection.update(query.getQueryObject(), dbDoc, true, false);
//                return null;
//            }
//        });
//    }

    public void delTag(String elementId) {
        this.delete(new Query(Criteria.where("elementId").is(elementId)));
    }

}
