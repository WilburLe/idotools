package com.toolbox.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toolbox.framework.spring.mongo.MongoBaseDao;
import com.toolbox.framework.utils.StringUtility;
import com.toolbox.framework.utils.UUIDUtility;

/**
* @author E-mail:86yc@sina.com
* 
*/
@Service("Mongo2JsonService")
public class Mongo2JsonService extends MongoBaseDao<String> {
    private String collection;

    public void setCollection(String collection) {
        this.collection = collection;
    }

    @Override
    protected Class<String> getEntityClass() {
        return String.class;
    }

    @Override
    protected String getCollection() {
        return collection;
    }

    public void save(JSONArray datas) {
        for (int i = 0; i < datas.size(); i++) {
            save(datas.getJSONObject(i));
        }
    }

    /**
     * id的生成规则 时间戳+UUID
     * @param data
     *
     */
    public void save(JSONObject data) {
        data.put("_id", System.currentTimeMillis() + UUIDUtility.uuid22());
        if (StringUtility.isNotEmpty(getCollection())) {
            this.mongoTemplate.save(data, getCollection());
        } else {
            this.mongoTemplate.save(data);
        }
    }

    public JSONObject getOne(Query query) {
        String data = this.queryOne(query);
        if (data == null) {
            return null;
        }
        return JSON.parseObject(data);
    }

    public List<JSONObject> getList(Query query) {
        List<String> data = this.queryList(query);
        if (data == null) {
            return null;
        }
        List<JSONObject> result = new ArrayList<JSONObject>();
        for (int i = 0; i < data.size(); i++) {
            result.add(JSON.parseObject(data.get(i)));
        }
        return result;
    }

}
