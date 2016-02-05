package com.toolbox.service;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.mongodb.core.CollectionCallback;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.MongoException;
import com.toolbox.entity.tag.AppTagGroupEntity;
import com.toolbox.framework.spring.mongo.MongoBaseDao;

/**
* @author E-mail:86yc@sina.com
* 
*/
@Service
public class TagEditService extends MongoBaseDao<AppTagGroupEntity> {

    @Override
    protected String getCollection() {
        return null;
    }

    @Override
    protected Class<AppTagGroupEntity> getEntityClass() {
        return AppTagGroupEntity.class;
    }

    public List<AppTagGroupEntity> findAllTag() {
        return this.queryList(null);
    }

    public AppTagGroupEntity findTagByAppType(String appType) {
        Query query = new Query();
        query.addCriteria(Criteria.where("appType").is(appType));
        return this.queryOne(query);
    }

    public void upsertTag(AppTagGroupEntity apptag) {  
        final BasicDBObject dbDoc = new BasicDBObject();
        this.mongoTemplate.getConverter().write(apptag, dbDoc);  
        this.mongoTemplate.execute(AppTagGroupEntity.class, new CollectionCallback<AppTagGroupEntity>() {  
            @Override  
            public AppTagGroupEntity doInCollection(DBCollection collection) throws MongoException, DataAccessException {  
                Query query = new Query();  
                query.addCriteria(Criteria.where("_id").is(apptag.getId()));  
                collection.update(query.getQueryObject(), dbDoc, true, false);
                return null;  
            }  
        });  
    }  
    
    
}
