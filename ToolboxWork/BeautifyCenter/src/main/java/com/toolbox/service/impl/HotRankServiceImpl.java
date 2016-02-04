package com.toolbox.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toolbox.service.HotRankService;
import com.toolbox.service.Mongo2JsonService;

/**
* @author E-mail:86yc@sina.com
* 
*/
@Service("HotRankService")
public class HotRankServiceImpl implements HotRankService {
    private final static String COLLECTION = "hotrank";
    @Autowired
    private Mongo2JsonService   mongo2JsonService;

    @Override
    public void save(JSONArray datas) {

    }

    @Override
    public void save(JSONObject data) {

    }

    @Override
    public void update(JSONObject data) {

    }

    @Override
    public void delete(String id) {

    }

    @Override
    public int count(String tag) {
        return 0;
    }

    @Override
    public List<JSONObject> findByPage(String tag, int start, int size) {
        return null;
    }

    @Override
    public JSONObject findByElementId(String elementId) {
        return null;
    }

}
