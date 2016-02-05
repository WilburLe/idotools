package com.toolbox.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toolbox.service.HotRankConfigService;
import com.toolbox.service.Mongo2JsonService;

/**
* @author E-mail:86yc@sina.com
* 
*/
@Service("HotRankConfigService")
public class HotRankConfigServiceImpl implements HotRankConfigService {
    private final static String COLLECTION = "hotrankconfig";
    
    @Autowired
    private Mongo2JsonService   mongo2JsonService;

    @Override
    public void save(JSONArray datas) {
        mongo2JsonService.setCollection(COLLECTION);
        
    }

    @Override
    public void save(JSONObject data) {
        mongo2JsonService.setCollection(COLLECTION);
        
    }

    @Override
    public void update(JSONObject data) {
        mongo2JsonService.setCollection(COLLECTION);
        
    }

    @Override
    public void delete(String id) {
        mongo2JsonService.setCollection(COLLECTION);
        
    }

    @Override
    public int count(String tag) {
        mongo2JsonService.setCollection(COLLECTION);
        
        return 0;
    }

    @Override
    public List<JSONObject> findByPage(String tag, int start, int size) {
        mongo2JsonService.setCollection(COLLECTION);
        
        return null;
    }

    @Override
    public JSONObject findByElementId(String elementId) {
        mongo2JsonService.setCollection(COLLECTION);
        
        return null;
    }

}
