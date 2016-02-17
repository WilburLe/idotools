package com.toolbox.web.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.mongodb.core.CollectionCallback;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

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

}
