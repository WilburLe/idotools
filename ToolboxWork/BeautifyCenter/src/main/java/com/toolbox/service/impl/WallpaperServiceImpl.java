package com.toolbox.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toolbox.framework.utils.StringUtility;
import com.toolbox.service.Mongo2JsonService;
import com.toolbox.service.WallpaperService;

/**
* @author E-mail:86yc@sina.com
* 
*/
@Service("WallpaperService")
public class WallpaperServiceImpl implements WallpaperService {
    @Autowired
    private Mongo2JsonService mongo2JsonService;

    @Override
    public void save(JSONArray datas) {
        mongo2JsonService.setCollection("wallpaper");
        mongo2JsonService.save(datas);
    }

    @Override
    public void save(JSONObject data) {
        mongo2JsonService.setCollection("wallpaper");
        mongo2JsonService.save(data);
    }

    @Override
    public void update(JSONObject data) {
        mongo2JsonService.setCollection("wallpaper");
        
        Update update = new Update();
        update.set("tags", data.getJSONArray("tags"));
        mongo2JsonService.updateFirst(Query.query(Criteria.where("_id").is(data.getString("_id"))), update);
    }

    @Override
    public void delete(String id) {
        mongo2JsonService.setCollection("wallpaper");
        
    }

    @Override
    public List<JSONObject> findByPage(String tag, int start, int size) {
        mongo2JsonService.setCollection("wallpaper");
        Query query = new Query();
        if(StringUtility.isNotEmpty(tag) && !"all".equals(tag)) {
            Criteria criteria = Criteria.where("tags").in(tag);
            query.addCriteria(criteria);
        }
        query.with(new Sort(Direction.DESC, "createDate"));
        List<String> ss = mongo2JsonService.getPage(query, (start - 1) * size, size);
        List<JSONObject> result = new ArrayList<JSONObject>();
        for (int i = 0; i < ss.size(); i++) {
            result.add(JSON.parseObject(ss.get(i)));
        }
        return result;
    }

    @Override
    public JSONObject findByElementId(String elementId) {
        mongo2JsonService.setCollection("wallpaper");
        Query query = new Query();
        Criteria criteria = Criteria.where("elementId").is(elementId);
        query.addCriteria(criteria);
        String s = mongo2JsonService.queryOne(query);
        return JSON.parseObject(s);
    }

}
