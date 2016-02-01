package com.toolbox.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toolbox.service.Mongo2JsonService;
import com.toolbox.service.TagEditService;

/**
* @author E-mail:86yc@sina.com
* 
*/
@Service("TagEditService")
public class TagEditServiceImpl implements TagEditService {
    @Autowired
    private Mongo2JsonService mongo2JsonService;

    @Override
    public List<JSONObject> findAllTag() {
        mongo2JsonService.setCollection("tags");
        Query query = new Query();
        return mongo2JsonService.getList(query);
    }

    @Override
    public JSONObject findTagByName(String name) {
        mongo2JsonService.setCollection("tags");
        Query query = new Query();
        Criteria criteria = Criteria.where("name").is(name);
        query.addCriteria(criteria);
        return mongo2JsonService.getOne(query);
    }

    @Override
    public void save(JSONObject data) {
        mongo2JsonService.setCollection("tags");
        mongo2JsonService.save(data);
    }

    @Override
    public void update(JSONObject data) {
        mongo2JsonService.setCollection("tags");
        Update update = new Update();
        update.set("arr", data.getJSONArray("arr"));
        mongo2JsonService.updateFirst(Query.query(Criteria.where("_id").is(data.getString("_id"))), update);
    }

    @Override
    public void delete(String name, String uuid) {
        mongo2JsonService.setCollection("tags");
        Criteria criteria = Criteria.where("name").is(name);
        Query query = new Query(criteria);
        JSONObject data = mongo2JsonService.getOne(query);
        if (data == null) {
            return;
        }

        JSONArray arr = data.getJSONArray("arr");
        JSONObject remTag = null;
        for (int i = 0; i < arr.size(); i++) {
            JSONObject tag = arr.getJSONObject(i);
            if (uuid.equals(tag.getString("uuid"))) {
                remTag = tag;
            }
        }
        if (remTag == null) {
            return;
        }
        arr.remove(remTag);
        Update update = new Update();
        update.set("arr", arr);
        mongo2JsonService.updateFirst(Query.query(Criteria.where("_id").is(data.getString("_id"))), update);

    }

}
