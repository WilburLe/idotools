package com.toolbox.web.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.mongodb.core.CollectionCallback;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.mongodb.AggregationOutput;
import com.mongodb.Cursor;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoException;
import com.toolbox.framework.spring.mongo.MongoBaseDao;

/**
* @author E-mail:86yc@sina.com
* 
*/
@Service
public class CommonJSONService extends MongoBaseDao<String> {
    @Override
    protected String getCollection() {
        return null;
    }

    @Override
    protected Class<String> getEntityClass() {
        return null;
    }

    public String findOne(Query query, String collectionName) {
        return this.mongoTemplate.execute(collectionName, new CollectionCallback<String>() {
            @Override
            public String doInCollection(DBCollection collection) throws MongoException, DataAccessException {
                DBObject object = collection.findOne(query.getQueryObject());
                return object == null ? null : object.toString();
            }
        });
    }

    public List<String> findList(Query query, String collectionName) {
        return this.mongoTemplate.execute(collectionName, new CollectionCallback<List<String>>() {
            @Override
            public List<String> doInCollection(DBCollection collection) throws MongoException, DataAccessException {
                Cursor cursor = collection.find(query.getQueryObject());
                if (cursor == null) {
                    return null;
                }
                List<String> result = new ArrayList<String>();
                while (cursor.hasNext()) {
                    DBObject object = cursor.next();
                    result.add(object.toString());
                }
                return result;
            }
        });
    }

    public List<String> findLists(List<DBObject> pipeline, String collectionName) {
        return this.mongoTemplate.execute(collectionName, new CollectionCallback<List<String>>() {
            @Override
            public List<String> doInCollection(DBCollection collection) throws MongoException, DataAccessException {
                List<String> result = new ArrayList<String>();

                AggregationOutput output = collection.aggregate(pipeline);
                Iterator<DBObject> ite = output.results().iterator();
                while (ite.hasNext()) {
                    DBObject object = ite.next();
                    result.add(object.toString());
                }
                return result;
            }
        });
    }

    public void delApp(String collectionName, String elementId) {
        //
        this.mongoTemplate.execute(collectionName, new CollectionCallback<String>() {
            @Override
            public String doInCollection(DBCollection collection) throws MongoException, DataAccessException {
                Query query = new Query();
                query.addCriteria(Criteria.where("elementId").is(elementId));
                collection.remove(query.getQueryObject());
                return null;
            }
        });

        //banner content
        this.mongoTemplate.execute("bannercontent", new CollectionCallback<String>() {
            @Override
            public String doInCollection(DBCollection collection) throws MongoException, DataAccessException {
                Query query = new Query(Criteria.where("appId").is(elementId));
                collection.remove(query.getQueryObject());
                return null;
            }
        });
    }

    public void updateMulti(String collectionName, Query query, Update update) {
        this.mongoTemplate.execute(collectionName, new CollectionCallback<String>() {
            @Override
            public String doInCollection(DBCollection collection) throws MongoException, DataAccessException {
                collection.updateMulti(query.getQueryObject(), update.getUpdateObject());
                return null;
            }
        });

    }
}
